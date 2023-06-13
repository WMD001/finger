package com.tech.finger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tech.data.ESOperator;
import com.tech.data.spider.GetNews;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URISyntaxException;

@SpringBootTest
class FingerApplicationTests {

    @Autowired
    private ESOperator esOperator;
    @Autowired
    private GetNews getNews;

    @Test
    void contextLoads() {
        esOperator.setFinger("web");
    }

    @Test
    void testGet() throws URISyntaxException, JsonProcessingException, UnsupportedEncodingException {
        getNews.getPage("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_1.jsonp?cb=news");
    }

    @Test
    void testBig() {
        BigInteger a = new BigInteger("56682685688368769975687158456712639985994238723884836982688868825888411257229812387269856872691286347123882339844563157664");
        BigInteger b = new BigInteger("2915334725239712155517715887936541884369523698741368742369874333987413687211705332887598866969993998568526714742687136874368712");
        //165249001907149532482550242022784787653849745013342696900860949111551366396982094238531028765094923632252619582397903457280856996440237710321768494910444483752125438126532094387139934651339030339794739894570321985234492602209038762029578673324608768
        System.out.println(a.multiply(b));
    }
}
