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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 保险岛 http://www.bxd365.com
 * Created by huan on 16/7/2.
 */
@Component("bxdProcessor")
public class BxdProcessor {

    private static Logger logger = Logger.getLogger(BxdProcessor.class);

    @Autowired
    private AgentRepository agentRepository;

    //页面代理参数
    private static final String agentParam = "51.0.2704.103 Safari";

    public static void main(String[] args) throws Exception {
       //process("http://www.bxd365.com/agent/", "广东", "广州");
    }

    public void process(String baseUrl, String province, String city) throws Exception {
        Document doc = Util.connect(baseUrl, agentParam, 0);
        //先选省份
        if (!doc.getElementById("prncode").select("a[selected]").text().equals(province)) {
            doc = Util.connect(baseUrl + getHref(doc, "prncode", province), agentParam, 0);
        }
        //再选城市
        if (city != null && !doc.getElementById("citycode").select("a[selected]").text().equals(city)) {
            doc = Util.connect(baseUrl + getHref(doc, "citycode", city), agentParam, 0);
        }
        //获取分页跳转链接
        String page = getHref(doc, "yw0", "末页");
        int num = Integer.parseInt(page.substring(page.indexOf("/") + 1, page.length() - 5));
        logger.info("共有 " + num + " 页结果列表分页！>>>>>>>>>>第 1 页<<<<<<<<<<");
        if (num == 0) {
            getDesc(doc);
        } else {
            page = page.substring(0, page.indexOf("/") + 1);
            for (int i = 1; i <= num; i++) {
                if (i == 1) {
                    getDesc(doc);
                } else {
                    String url = baseUrl + page + i + ".html";
                    logger.info("page: " + url);
                    logger.info(">>>>>>>>>>>>>>>>>>>>第 " + i + " 页<<<<<<<<<<<<<<<<<<<<");
                    getDesc(Util.connect(url, agentParam, 0));
                }
            }
        }
    }

    //获取链接地址
    private String getHref(Document doc, String id, String target) throws Exception {
        Element links = doc.getElementById(id);
        //若分页元素为空,返回0
        if (links == null) return "0";
        Elements selects = links.select("a:contains(" + target + ")");
        if (selects.isEmpty()) throw new Exception("未查询到目标: " + target);
        String href = selects.first().attr("href");
        return href.substring(7, href.length());
    }

    //获取详情页信息
    private void getDesc(Document document) throws Exception {
        Elements infos = document.select("[class=list clearfix");
        for (Element info : infos) {
            String descUrl = info.select("a[class=hd-img]").attr("href");
            logger.info("target: " + descUrl);
            //agent.setDescUrl(descUrl);
            Agent agent = new Agent();
            agent.setName(info.select("a[class=name]").text());
            String firm = info.select("span[class=firm]").text();
            agent.setCompany(firm);
            agent.setBusiness(info.select("li:has(span[class=be-good])").text().replaceAll("擅长领域：", ""));
            String place = info.select("li[class=firm-txt]").text();
            place = place.substring(firm.length() + 2, place.length());
            agent.setLocation(place);

            Document doc = Util.connect(descUrl, agentParam, 0);
            Element resume = doc.getElementsByClass("resume-info").first();
            if (resume != null) {
                //自我介绍
                agent.setIntroduce(resume.select("p").text());
            } else {
                resume = doc.getElementsByClass("introduction").first();
                if (resume != null) agent.setIntroduce(resume.select("p").text());
            }
            //手机号码
            Document doc2 = Util.connect(descUrl, null, 0);
            Pattern p = Pattern.compile("\\.tel'\\)\\.attr\\('href',\\s\"\\d+");
            Matcher m = p.matcher(doc2.getElementsByTag("script").html());
            if (m.find()) agent.setPhone(m.group(0).substring(21, m.group(0).length()));

            logger.info(agent.getName() + "\n" + agent.getCompany() + "\n" + agent.getBusiness() + "\n"
                + agent.getLocation() + "\n" + agent.getPhone() + "\n" + agent.getIntroduce());
            //保存数据库
            agent.setStamp(null);
            agent.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            agentRepository.save(agent);
            logger.info(agent.getName() + " 保存成功!");
        }
    }
}
