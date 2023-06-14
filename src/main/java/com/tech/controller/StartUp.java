package com.tech.controller;

import com.tech.data.ESOperator;
import com.tech.data.spider.GetNews;
import com.tech.data.spider.GetPics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Wang
 * 2023/6/14
 */
@RestController
@Slf4j
public class StartUp {

    private final GetNews getNews;
    private final GetPics getPics;
    private final ESOperator esOperator;

    public StartUp(GetNews getNews, GetPics getPics, ESOperator esOperator) {
        this.getNews = getNews;
        this.getPics = getPics;
        this.esOperator = esOperator;
    }

    @GetMapping("/startNewsData")
    public void startNewsData() {
        getNews.getPage("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_1.jsonp?cb=news");
        getNews.getPage("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_2.jsonp?cb=news");
        getNews.getPage("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_3.jsonp?cb=news");
        getNews.getPage("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_4.jsonp?cb=news");
        getNews.getPage("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_5.jsonp?cb=news");
        getNews.getPage("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_6.jsonp?cb=news");
        getNews.getPage("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_7.jsonp?cb=news");
        getNews.getPage("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_8.jsonp?cb=news");
        getNews.getPage("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_9.jsonp?cb=news");
    }

    @GetMapping("/startPicsData")
    public void startPicsData() {
        getPics.getPicData("https://photo.cctv.com/data/index.json");
    }

    @GetMapping("/startSetFinger")
    public void startSetFinger() {
        esOperator.setFinger("web_1");
    }

}
