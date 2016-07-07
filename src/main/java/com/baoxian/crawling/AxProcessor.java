package com.baoxian.crawling;

import com.baoxian.common.Util;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 爱心保险 http://www.axbxw.com
 * Created by huan on 16/7/2.
 */
public class AxProcessor {

    private static Logger logger = Logger.getLogger(AxProcessor.class);

    public static void main(String[] args) throws Exception {
        process("http://www.axbxw.com/agent/", "广东", "广州");
    }

    public static void process(String baseUrl, String province, String city) throws Exception {
        Document doc = Util.get(baseUrl, 1, 0);
        //先选省份
        Element proEl = doc.getElementsByClass("brand_list").first();
        if (!proEl.select("a[class=on]").text().equals(province)) {
            String href = proEl.select("a:contains(" + province + ")").attr("href");
            href = href.substring(7, href.length());
            doc = Util.get(baseUrl + href, 1, 0);
        }
        //再选城市
        //Document doc= Jsoup.parse(Util.read("F:\\workspace\\WebCapture\\src\\main\\resources\\public\\ax.html"));
        Element cityEl = doc.getElementsByClass("brand_list").first();
        if (city != null && !cityEl.select("a[class=on]").text().equals(city)) {
            String href = cityEl.select("a:contains(" + city + ")").attr("href");
            doc = Util.get(baseUrl + href, 1, 0);
        }


        //System.out.println(doc);
    }
}
