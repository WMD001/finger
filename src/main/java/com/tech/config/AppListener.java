package com.tech.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tech.data.spider.GetNews;
import com.tech.util.SpringBeanUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

/**
 * @author Wang
 * 2023/6/8
 */
@Component
public class AppListener {

    @EventListener(value = ApplicationReadyEvent.class)
    public void setApplicationContext(ApplicationReadyEvent event) throws UnsupportedEncodingException, URISyntaxException, JsonProcessingException {
        SpringBeanUtils.setApplicationContext(event.getApplicationContext());
        GetNews getNews = SpringBeanUtils.getBean(GetNews.class);
//        getNews.getPage("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_1.jsonp?cb=news");
    }

}
