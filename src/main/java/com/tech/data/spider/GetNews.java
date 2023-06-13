package com.tech.data.spider;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void getPage(String pageUrl) throws URISyntaxException, JsonProcessingException, UnsupportedEncodingException {
        log.info("开始数据查询：" + pageUrl);
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
            for (Object o : list) {
                Map<String, Object> item = JsonUtils.objToMap(o);
                WriteRunner writeRunner = new WriteRunner("web", item);
                writeRunner.start();
            }
        }
    }



    private static class WriteRunner extends DocWriter {

        public Map<String, Object> map;

        public WriteRunner(String index, Map<String, Object> map) {
            this.index = index;
            this.map = map;
        }

        public String getDocContent() {
            try {
                String url = map.get("url").toString();
                if (url.startsWith(domain)) {
                    Connection connect = Jsoup.connect(url);
                    Document document = connect.get();
                    Element contentArea = document.getElementById("content_area");
                    assert contentArea != null;
                    return contentArea.text();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "";
        }

        @Override
        public Map<String, Object> getDoc() {
            Map<String, Object> doc = new HashMap<>();
            doc.put("title", map.get("title"));
            doc.put("content", getDocContent());
            doc.put("desc", map.get("brief"));
            doc.put("base64Img", map.get("title"));
            doc.put("imageUrl", map.get("image"));
            doc.put("dataSource", map.get("url"));
            doc.put("pubDate", map.get("focus_date"));
            doc.put("keywords", map.get("keywords"));
            doc.put("snapshot", map.get("title"));
            doc.put("createDate", LocalDateTime.now());
            doc.put("domain", domain);
            doc.put("docNo", map.get("id"));
            return doc;
        }
    }

}
