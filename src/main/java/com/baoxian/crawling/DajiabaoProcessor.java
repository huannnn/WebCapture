package com.baoxian.crawling;

import com.baoxian.common.Util;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 大家保 http://www.dajiabao.com/guwen
 * Created by huan on 16/7/3.
 */
public class DajiabaoProcessor {

    private static Logger logger = Logger.getLogger(DajiabaoProcessor.class);

    public static void main(String[] args) throws Exception {
        process("http://www.dajiabao.com/guwen", "广东", null);
    }

    public static void process(String baseUrl, String province, String city) throws Exception {
        /*Document doc = Util.get(baseUrl, 1, 0);
        String outLink = "";
        String prv = doc.select("[id=selectCityList] > strong[class=font_yellow]").text();
        String ct = doc.select("[id=detailBox_area] > strong[class=font_yellow]").text();
        if (!prv.equals(province) || (prv.equals(province) && city == null)) {
            String p = doc.select("[id=selectCityList] > a:contains(" + province + ")").attr("href");
            outLink = baseUrl + p.substring(6, p.length());
            doc = Util.get(outLink, 1, 0);
        }
        if (city != null && !ct.equals(city)) {
            String c = doc.select("[id=detailBox_area] > a:contains(" + city + ")").attr("href");
            outLink = baseUrl + c.substring(6, c.length());
            doc = Util.get(outLink, 1, 0);
        }*/
        String outLink = "http://www.dajiabao.com/guwen/gd";
        Document doc = Jsoup.parse(Util.read("G:\\workspace\\WebCapture\\src\\main\\resources\\public\\dajiabao.html"));
        logger.info("目标抓取总" + doc.select("[id=listTitle] > h1").text());
        //获取分页数
        String page = doc.select("[id=pageBar_bottom] > span[class=goToBox]").text();
        int num = Integer.parseInt(page.substring(1, page.length() - 6));
        logger.info("共有 " + num + " 页结果列表分页！>>>>>>>>>>第 1 页<<<<<<<<<<");
        for (int i = 1; i <= num; i++) {
            if (i == 1) {
                getDesc(doc);
            } else {
                getDesc(Util.get(outLink + "-p" + num, 1, 0));
            }
        }
    }

    private static String getDesc(Document document) throws Exception {
        return null;
    }
}
