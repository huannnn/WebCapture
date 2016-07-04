package com.baoxian.crawling;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 沃保网 http://www.vobao.com
 * Created by huan on 16/7/2.
 */
public class VobaoProcessor {

    private static Logger logger = Logger.getLogger(VobaoProcessor.class);

    private static String baseUrl = "http://member.vobao.com/";

    public static void main(String[] args) throws Exception {
        process();
    }

    public static void process() throws Exception {
        Document doc = Jsoup.connect(baseUrl).get();
        System.out.println(doc);
    }
}
