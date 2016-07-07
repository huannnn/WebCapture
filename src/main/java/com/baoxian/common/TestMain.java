package com.baoxian.common;

import akka.actor.ActorSystem;
import com.baoxian.crawling.BxdProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMain {

    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("http://bxd345784042.bxd365.com/").userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_2 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile").get();
        System.out.println(doc);
    }
}
