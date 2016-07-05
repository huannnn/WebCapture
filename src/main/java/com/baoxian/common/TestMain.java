package com.baoxian.common;

import com.baoxian.crawling.BxdProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMain {

    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("http://www.bxd365.com/agent/").timeout(30000).get();
        System.out.println(doc);
    }
}
