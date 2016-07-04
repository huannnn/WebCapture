package com.baoxian.crawling;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 爱心保险 http://www.axbxw.com
 * Created by huan on 16/7/2.
 */
public class AxProcessor {

    private static Logger logger = Logger.getLogger(AxProcessor.class);

    private static String baseUrl = "http://www.axbxw.com/agent/";

    public static void main(String[] args) throws Exception {
        process();
    }

    public static void process() throws Exception {
        Document doc = Jsoup.connect(baseUrl).get();
        System.out.println(doc);
    }
}
