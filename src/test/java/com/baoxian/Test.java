package com.baoxian;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/4.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("http://bxd28510.bxd365.com").get();
        String str = doc.getElementsByTag("script").html();
        System.out.println(str);
        Pattern p = Pattern.compile("\\.tel'\\)\\.attr\\('href',\\s\"\\d+");
        Matcher m = p.matcher(str);
        if (m.find()) {
            String s = m.group(0);
            s=s.substring(21, s.length());
            System.out.println("===========>>>>>"+s);
        }
    }
}
