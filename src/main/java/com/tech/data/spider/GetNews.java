package com.tech.data.spider;

import com.tech.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 新闻数据采集
 *
 * @author Wang
 * 2023/6/7
 */
@Slf4j
@Service
public class GetNews {

    private final RestTemplate restTemplate;

    public GetNews(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final static String domain = "https://news.cctv.com";

    public void getPage(String pageUrl) {
        WriteRunner writeRunner = new WriteRunner("web_news", pageUrl);
        writeRunner.start();
    }



    private class WriteRunner extends DocWriter {

        public WriteRunner(String index, String pageUrl) {
            this.index = index;
            this.pageUrl = pageUrl;
        }

        public String getDocContent(Map<String, Object> item) {
            try {
                String url = item.get("url").toString();
                if (url.startsWith(domain)) {
                    Connection connect = Jsoup.connect(url);
                    Document document = connect.get();
                    Element contentArea = document.getElementById("content_area");
                    if (contentArea == null) {
                        contentArea = document.getElementById("text_area");
                    }
                    return contentArea == null ? "" : contentArea.text();

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "";
        }

        @Override
        public void getPageData(String pageUrl, Consumer<Map<String, Object>> consumer) {
            log.info("开始数据查询：" + pageUrl);
            try{
                ResponseEntity<String> forEntity = restTemplate.getForEntity(new URI(pageUrl), String.class);
                if (forEntity.getStatusCode().equals(HttpStatus.OK)) {
                    String body = forEntity.getBody();
                    assert body != null;
                    String json = new String(body.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    json = json.substring(5, json.length()-1);
                    Map<String, Object> map = JsonUtils.jsonToMap(json, String.class, Object.class);
                    Map<String, Object> dataMap = JsonUtils.objToMap(map.get("data"), String.class, Object.class);
                    List<Object> list = JsonUtils.objToList(dataMap.get("list"), Object.class);
                    log.info("查询到数据条数：" + list.size());
                    addSize(list.size());
                    for (Object o : list) {
                        Map<String, Object> item = JsonUtils.objToMap(o);
                        consumer.accept(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public Map<String, Object> getDoc(Map<String, Object> item) {
            Map<String, Object> doc = new HashMap<>();
            doc.put("title", item.get("title"));
            doc.put("content", getDocContent(item));
            doc.put("desc", item.get("brief"));
            doc.put("base64Img", imgUrlToBase64(item.get("image").toString()));
            doc.put("imageUrl", item.get("image"));
            doc.put("dataSource", item.get("url"));
            doc.put("pubDate", item.get("focus_date"));
            doc.put("keywords", item.get("keywords"));
            doc.put("snapshot", item.get("title"));
            doc.put("domain", domain);
            doc.put("docNo", item.get("id"));
            return doc;
        }
    }

}
