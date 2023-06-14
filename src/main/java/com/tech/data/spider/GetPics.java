package com.tech.data.spider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tech.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
 * @author Wang
 * 2023/6/14
 */
@Service
@Slf4j
public class GetPics {

    private final RestTemplate restTemplate;

    public GetPics(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void getPicData(String pageUrl) {
        PicWriterRunner picWriterRunner = new PicWriterRunner("picture", pageUrl);
        picWriterRunner.start();
    }

    private class PicWriterRunner extends DocWriter {

        public PicWriterRunner(String index, String pageUrl) {
            this.index = index;
            this.pageUrl = pageUrl;
        }

        @Override
        public void getPageData(String pageUrl, Consumer<Map<String, Object>> consumer) {
            try {
                String forObject = restTemplate.getForObject(new URI(pageUrl), String.class);
                if (forObject != null) {
                    String json = new String(forObject.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    Map<String, Object> objectMap = JsonUtils.jsonToMap(json, String.class, Object.class);
                    List<Object> rollData = JsonUtils.objToList(objectMap.get("rollData"), Object.class);
                    addSize(rollData.size());
                    rollData.forEach(o -> {
                        try {
                            Map<String, Object> item = JsonUtils.objToMap(o);
                            consumer.accept(item);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public Map<String, Object> getDoc(Map<String, Object> item) {
            Map<String, Object> doc = new HashMap<>();
            doc.put("title", item.get("title"));
            doc.put("content", item.get("content"));
            doc.put("details", getDetails(item));
            doc.put("docNo", item.get("id"));
            doc.put("dataSource", item.get("url"));
            doc.put("base64Img", imgUrlToBase64(item.get("image").toString()));
            doc.put("imageUrl", item.get("image"));
            return doc;
        }

        private List<String> getDetails(Map<String, Object> item) {
            String url = item.get("url").toString().replace(".shtml", ".xml");
            Connection connect = Jsoup.connect(url);
            Document document;
            try {
                document = connect.get();
            } catch (IOException e) {
                throw new RuntimeException(e + url);
            }
            Elements li = document.getElementsByTag("li");
            return li.eachAttr("photourl");
        }
    }

}
