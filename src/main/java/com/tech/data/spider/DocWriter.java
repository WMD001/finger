package com.tech.data.spider;

import com.tech.data.ESOperator;
import com.tech.util.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    public void start() {
        threadPool.submit(() -> {
            Map<String, Object> doc = getDoc();
            log.info("开始处理数据：" + doc.get("docNo"));
            synchronized (new Object()) {
                docs.add(doc);
                if (docs.size() >= 20) {
                    write(index, docs);
                    docs.clear();
                }
            }
        });
    }

    public abstract Map<String, Object> getDoc();

    void write(String index, List<Map<String, Object>> docs) {
        ESOperator esOperator = SpringBeanUtils.getBean(ESOperator.class);
        esOperator.bulkIndex(index, docs);
        log.info("写入数据：" + docs.size());
    }

}
