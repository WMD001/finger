package com.tech.controller;

import com.tech.data.ESOperator;
import com.tech.data.spider.GetNews;
import com.tech.data.spider.GetPics;
import com.tech.data.spider.news.index.GetData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


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
    private final GetData getData;

    public StartUp(GetNews getNews, GetPics getPics, ESOperator esOperator, GetData getData) {
        this.getNews = getNews;
        this.getPics = getPics;
        this.esOperator = esOperator;
        this.getData = getData;
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
        esOperator.setFinger("web_news");
    }

    @GetMapping("/startIndex")
    public void startIndex() {
        List<Map<String, String>> index = getData.index();
        log.info("采集数据条数：" + index.size());
    }

}
