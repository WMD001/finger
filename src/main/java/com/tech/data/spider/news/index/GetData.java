package com.tech.data.spider.news.index;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.tech.data.spider.news.config.IndexConfig;
import com.tech.data.spider.news.config.IndexDocContentConfig;
import com.tech.data.spider.news.config.IndexDocumentConfig;
import com.tech.util.JsonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author Wang
 * 2023/6/19
 */
@Service
public class GetData {

    private final IndexConfig indexConfig;
    private final RestTemplate restTemplate;

    public GetData(IndexConfig indexConfig, RestTemplate restTemplate) {
        this.indexConfig = indexConfig;
        this.restTemplate = restTemplate;
    }

    public List<Map<String, String>> index() {
        List<Map<String, String>> documentList = new ArrayList<>();
        IndexDocumentConfig[] indexDocumentConfigs = indexConfig.getIndexDocumentConfigs();
        Arrays.stream(indexDocumentConfigs).forEach(docConfig -> {
            String pageUrl = docConfig.getPageUrl();
            DocumentContext parse = JsonPath.parse(getPageData(pageUrl));
            List<Object> list = parse.read(docConfig.getJsonArrayPath());
            list.forEach(obj -> {
                try {
                    Map<String, String> pageData = new HashMap<>();
                    Map<String, Object> map = JsonUtils.objToMap(obj);
                    Arrays.stream(docConfig.getFieldConfigs()).forEach(fieldConfig -> {
                        String read = map.getOrDefault(fieldConfig.getFieldName(), "").toString();
                        pageData.put(fieldConfig.getAlias(), fieldConfig.getPrefix() + read + fieldConfig.getSuffix());
                        if ("dataSource".equals(fieldConfig.getFieldName())) {
                            IndexDocContentConfig docContentConfig = docConfig.getDocContentConfig();
                            pageData.put(docContentConfig.getDefaultContentField(), getDocContent(read, docContentConfig));
                        }
                    });
                    documentList.add(pageData);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        return documentList;
    }

    private String getPageData(String pageUrl) {
        ResponseEntity<String> forEntity = restTemplate.getForEntity(pageUrl, String.class);
        return forEntity.getBody();
    }

    private String getDocContent(String detailUrl, IndexDocContentConfig indexDocContentConfig) {

        return null;
    }

}
