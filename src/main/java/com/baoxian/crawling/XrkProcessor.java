package com.baoxian.crawling;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 向日葵 http://www.xiangrikui.com/
 * Created by huan on 16/7/2.
 */
public class XrkProcessor {

    private static Logger logger = Logger.getLogger(XrkProcessor.class);

    private static String baseUrl = "http://a.xiangrikui.com/";

    public static void main(String[] args) throws Exception {
        process();
    }

    public static void process() throws Exception {
        Document doc = Jsoup.connect(baseUrl).get();
        System.out.println(doc);
    }
}
