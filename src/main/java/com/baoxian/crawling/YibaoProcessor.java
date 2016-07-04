package com.baoxian.crawling;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 益保保险 http://www.yibaobx.com
 * Created by huan on 16/7/2.
 */
public class YibaoProcessor {

    private static Logger logger = Logger.getLogger(YibaoProcessor.class);

    private static String baseUrl = "http://www.yibaobx.com/agent-1.html";

    public static void main(String[] args) throws Exception {
        process();
    }

    public static void process() throws Exception {
        Document doc = Jsoup.connect(baseUrl).get();
        System.out.println(doc);
    }
}
