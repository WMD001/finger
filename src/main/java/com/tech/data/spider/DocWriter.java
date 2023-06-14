package com.tech.data.spider;

import com.tech.data.ESOperator;
import com.tech.util.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author Wang
 * 2023/6/7
 */
@Slf4j
public abstract class DocWriter {

    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            0, Integer.MAX_VALUE,
            10L, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new CustomThreadFactory());

    private List<Map<String, Object>> docs = new ArrayList<>();

    private AtomicInteger size = new AtomicInteger(0);
    private AtomicInteger commited = new AtomicInteger(0);
    protected String pageUrl = "";

    private static class CustomThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger index = new AtomicInteger(1);

        private CustomThreadFactory() {
            SecurityManager sm = System.getSecurityManager();
            group = (sm != null) ? sm.getThreadGroup()
                    : Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, "DocWrite-"
                    + index.getAndIncrement());
            t.setDaemon(true);
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    protected String index;

    public DocWriter addSize(int size) {
        log.info("添加处理条数：{}，当前在处理条数：{}", size, this.size.addAndGet(size));
        return this;
    }

    public void start() {
        threadPool.submit(() -> {
            getPageData(pageUrl, (item) -> {
                Map<String, Object> doc = getDoc(item);
                log.info("开始处理数据：" + doc.get("docNo"));
                synchronized (new Object()) {
                    docs.add(doc);
                    size.getAndDecrement();
                    if (docs.size() >= 20) {
                        write(index, docs);
                        docs.clear();
                    }
                    if (size.get() <= 0 && docs.size() > 0) {
                        write(index, docs);
                    }
                }
            });
        });
    }

    public abstract void getPageData(String pageUrl, Consumer<Map<String, Object>> consumer);

    public abstract Map<String, Object> getDoc(Map<String, Object> item);

    void write(String index, List<Map<String, Object>> docs) {
        ESOperator esOperator = SpringBeanUtils.getBean(ESOperator.class);
        esOperator.bulkIndex(index, docs);
        log.info("写入数据：" + docs.size());
        log.info("累计写入：" + commited.addAndGet(docs.size()));
    }

    String imgUrlToBase64(String imgUrl) {
        URL url;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try{
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();

            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while( (len=is.read(buffer)) != -1 ){
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            return Base64.getEncoder().encodeToString(outStream.toByteArray());
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(is != null)
            {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outStream != null)
            {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpUrl != null)
            {
                httpUrl.disconnect();
            }
        }
        return imgUrl;
    }

}
