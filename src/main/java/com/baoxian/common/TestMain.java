package com.baoxian.common;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMain {

    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("http://bxd315869109.bxd365.com").get();
        System.out.println(doc);
        /*String str = doc.getElementsByTag("script").html();
        System.out.println(str);
        Pattern p = Pattern.compile("\\.tel'\\)\\.attr\\('href',\\s\"\\d+");
        Matcher m = p.matcher(str);
        if (m.find()) {
            String s = m.group(0);
            s=s.substring(21, s.length());
            System.out.println("===========>>>>>"+s);
        }*/
    }
}
