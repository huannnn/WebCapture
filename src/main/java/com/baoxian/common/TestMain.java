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
        /*Document doc = Jsoup.connect("http://bxd913455993.bxd365.com/").timeout(30000).userAgent("51.0.2704.103 Safari").get();

        System.out.println(doc.select("p[style=text-indent:2em;]").text());*/


        //Akka Future Test
    }
}
