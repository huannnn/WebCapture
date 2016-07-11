package com.baoxian.crawling;

import com.baoxian.common.HttpSender;
import com.baoxian.common.Util;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 保险中间站 http://www.life-sky.net
 * Created by huan on 16/7/3.
 */
public class LifeSkyProcessor {

    private static Logger logger = Logger.getLogger(LifeSkyProcessor.class);

    public static void main(String[] args) throws Exception {
        process("http://www.baoxian360.net/index.asp", "广东", null);
    }

    public static void process(String baseUrl, String province, String city) throws Exception {
        /*Document doc = Util.get(baseUrl, 1, 0);
        String atplace = "", atcity = "";
        String prv = doc.select("select[name=atplace][id=select] > option[selected]").text();
        String ct = doc.select("select[name=atcity][id=atcity] > option[selected]").text();
        if (!prv.equals(province) || (prv.equals(province) && city == null)) {
            atplace = doc.select("select[name=atplace][id=select] > option:contains(" + province + ")").attr("value");
            String jsPath = baseUrl + "/" + doc.select("SCRIPT[language=JavaScript]").first().attr("src");
            String js = Jsoup.connect(jsPath).ignoreContentType(true).get().html();
        }*/
        StringBuilder sb = new StringBuilder();
        sb.append("select_sou=index").append("&atplace=广东省").append("&atcity=").append("&type=保险中介")
                .append("&corp=0&corpzj=&nname=&sex=0&age1=18&age2=65&type1=&corp1=&ifuse=0&productkey=&projectkey=");
        //String rsp = HttpSender.post(sb.toString(), baseUrl);
        Document doc = Jsoup.parse(Util.read("/Users/huan/workspace/WebCapture/src/main/resources/public/zjz.html"));

        String page = doc.select("input[id=tolpage]").attr("value");
        logger.info("共有 " + page + " 页结果列表分页！>>>>>>>>>>第 1 页<<<<<<<<<<");
        int i = 1;
        if (page != null) {
            int num = Integer.parseInt(page);
            for (; i <= num; i++) {
                if (i == 1) {
                    getDesc(doc);
                } else {

                }
            }
        }
    }

    private static void getDesc(Document document) throws Exception {
        Element el = document.select("td[class=f]").first();
        Pattern p = Pattern.compile("[\\u4E00-\\u9FA5]+\\s[\\u4E00-\\u9FA5]\\s\\d+[\\u4E00-\\u9FA5]+\\s[\\u4E00-\\u9FA5]+\\s[\\u4E00-\\u9FA5]+\\s[\\u4E00-\\u9FA5]+<br>");
        Matcher m = p.matcher(el.html());
        if (m.find()) {
            System.out.println("=====>"+m.group());
        }
    }
}
