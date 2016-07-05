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

    public static void main(String[] args) throws Exception {
       //process("http://www.bxd365.com/agent/", "广东", "广州");
    }

    public void process(String baseUrl, String province, String city) throws Exception {
        Document doc = Util.connect(baseUrl, null, 0);
        //先选省份
        if (!doc.getElementById("prncode").select("a[selected]").text().equals(province)) {
            doc = Util.connect(baseUrl + getHref(doc, "prncode", province), null, 0);
        }
        //再选城市
        if (city != null && !doc.getElementById("citycode").select("a[selected]").text().equals(city)) {
            doc = Util.connect(baseUrl + getHref(doc, "citycode", city), null, 0);
        }
        logger.info("当前目标抓取总条数: " + doc.getElementsByClass("result-count").text());

        //获取分页跳转链接
        String page = getHref(doc, "yw0", "末页");
        int num = Integer.parseInt(page.substring(page.indexOf("=") + 1, page.length()));
        if (num == 0) {
            getDesc(doc);
        } else {
            page = page.substring(0, page.indexOf("=") + 1);
            for (int i = 1; i <= num; i++) {
                if (i == 1) {
                    getDesc(doc);
                } else {
                    logger.info("page: " + baseUrl + page + i);
                    getDesc(Util.connect(baseUrl + page + i, null, 0));
                }
            }
        }
    }

    //获取链接地址
    public String getHref(Document doc, String id, String target) throws Exception {
        Element links = doc.getElementById(id);
        //若分页元素为空,返回0
        if (links == null) return "0";
        Elements selects = links.select("a:contains(" + target + ")");
        if (selects.isEmpty()) throw new Exception("未查询到目标: " + target);
        String href = selects.first().attr("href");
        return href.substring(7, href.length());
    }

    //获取详情页信息
    public void getDesc(Document document) throws Exception {
        Agent agent = new Agent();
        Elements infos = document.getElementsByClass("info");
        for (Element info : infos) {
            String descUrl = info.select("a[class=weizhan]").attr("href");
            logger.info("target: " + descUrl);
            //agent.setDescUrl(descUrl);
            agent.setName(info.select("span[class=name]").text());
            agent.setCompany(info.select("span[class=firm-tit]").text());
            agent.setLocation(info.select("span[class=region]").text());
            agent.setBusiness(info.select("span[class=territory]").text());

            Document doc = Util.connect(descUrl, "51.0.2704.103 Safari", 0);
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
            agentRepository.save(agent);
            logger.info(agent.getName() + " 保存成功!");
        }
    }
}
