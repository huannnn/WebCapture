package com.baoxian.crawling;

import com.baoxian.common.Util;
import com.baoxian.domain.Agent;
import com.baoxian.domain.AgentRepository;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 大家保 http://www.dajiabao.com/guwen
 * Created by huan on 16/7/3.
 */
@Component
public class DajiabaoProcessor {

    private static Logger logger = Logger.getLogger(DajiabaoProcessor.class);

    @Autowired
    private AgentRepository agentRepository;

    public static void main(String[] args) throws Exception {
        //process("http://www.dajiabao.com/guwen", "广东", null);
    }

    public void process(String baseUrl, String province, String city) throws Exception {
        Document doc = Util.get(baseUrl, 1, 0);
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
        }
        logger.info("目标抓取总" + doc.select("[id=listTitle] > h1").text());
        //获取分页数
        String page = doc.select("[id=pageBar_bottom] > span[class=goToBox]").text();
        int num = Integer.parseInt(page.substring(1, page.length() - 6));
        logger.info("共有 " + num + " 页结果列表分页！>>>>>>>>>>第 1 页<<<<<<<<<<");
        for (int i = 1; i <= num; i++) {
            if (i == 1) {
                getDesc(doc, province);
            } else {
                String url = outLink + "-p" + i;
                logger.info("page: " + url);
                logger.info(">>>>>>>>>>>>>>>>>>>>第 " + i + " 页<<<<<<<<<<<<<<<<<<<<");
                getDesc(Util.get(url, 1, 0), province);
            }
        }
    }

    private void getDesc(Document document, String province) throws Exception {
        Elements els = document.getElementsByClass("table_dailiList");
        int index = 1;
        for (Element el : els) {
            logger.info("抓取该页第 " + index + " 条...");
            String descUrl = el.select("strong[class=cnName] > a").attr(("href"));
            logger.info("target: " + descUrl);
            Agent agent = new Agent();
            String s1 = el.select("strong[class=cnName]").text();
            agent.setName(s1.substring(0, s1.indexOf("/") - 1));
            agent.setLocation(province + " " + s1.substring(s1.indexOf("/") + 1, s1.length()));
            agent.setCompany(el.select("tr").get(2).select("a").text());
            String s2 = el.select("tr").get(3).select("td").last().text();
            agent.setBusiness(s2.substring(s2.indexOf("业务特长") + 5, s2.length()));
            String s3 = el.select("td[colspan=2]").last().text();
            agent.setIntroduce(s3.substring(5, s3.length()));

            Document doc = Util.get(descUrl.replace("www", "m"), 2, 0);
            String phone = doc.select("ul[class=clearfix] > li[class=icon02] > a").attr("href");
            agent.setPhone(phone.substring(4, phone.length()));
            agent.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            agent.setStamp(null);
            logger.info(agent.getName() + "\n" + agent.getCompany() + "\n" + agent.getBusiness() + "\n"
                    + agent.getLocation() + "\n" + agent.getPhone() + "\n" + agent.getIntroduce());
            agentRepository.save(agent);
            index += 1;
        }
    }
}
