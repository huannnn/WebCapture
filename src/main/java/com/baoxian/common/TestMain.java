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
        String s = "<p class=\"agent-company\">广州\n" +
                "\t\t\t\t\t, </p>";
        Document doc = Jsoup.parse(s);
        String str = doc.text();
        System.out.println(str);
        System.out.println(str.substring(0, str.indexOf(" ,")));
    }
}
