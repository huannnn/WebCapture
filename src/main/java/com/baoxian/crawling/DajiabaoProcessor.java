package com.baoxian.crawling;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 大家保 http://www.dajiabao.com/guwen
 * Created by huan on 16/7/3.
 */
public class DajiabaoProcessor {

    private static Logger logger = Logger.getLogger(DajiabaoProcessor.class);

    private static String baseUrl = "http://www.dajiabao.com/guwen";

    public static void main(String[] args) throws Exception {
        process();
    }

    public static void process() throws Exception {
        Document doc = Jsoup.connect(baseUrl).get();
        System.out.println(doc);
    }
}
