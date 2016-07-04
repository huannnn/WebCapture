package com.baoxian.crawling;

import com.baoxian.domain.Agent;
import com.baoxian.domain.AgentRepository;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
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
        Document doc = connect(baseUrl, null, 0);
        //先选省份
        if (!doc.getElementById("prncode").select("a[selected]").text().equals(province)) {
            doc = Jsoup.connect(baseUrl + getHref(doc, "prncode", province)).get();
        }
        //再选城市
        if (city != null && !doc.getElementById("citycode").select("a[selected]").text().equals(city)) {
            doc = Jsoup.connect(baseUrl + getHref(doc, "citycode", city)).get();
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
                    getDesc(connect(baseUrl + page + i, null, 0));
                }
            }
        }
    }

    //获取详情页信息
    public void getDesc(Document document) throws Exception {
        Agent agent = new Agent();
        Elements infos = document.getElementsByClass("info");
        for (Element info : infos) {
            String descUrl = info.select("a[class=weizhan]").attr("href");
            logger.info("target: " + descUrl);
            agent.setDescUrl(descUrl);
            agent.setName(info.select("span[class=name]").text());
            agent.setCompany(info.select("span[class=firm-tit]").text());
            agent.setLocation(info.select("span[class=region]").text());
            agent.setBusiness(info.select("span[class=territory]").text());

            Document doc = connect(descUrl, "51.0.2704.103 Safari", 0);
            Element resume = doc.getElementsByClass("resume-info").first();
            if (resume != null) {
                //自我介绍
                agent.setIntroduce(resume.select("p").text());
            } else {
                resume = doc.getElementsByClass("introduction").first();
                if (resume != null) agent.setIntroduce(resume.select("p").text());
            }
            //手机号码
            Document doc2 = connect(descUrl, null, 0);
            Pattern p = Pattern.compile("\\.tel'\\)\\.attr\\('href',\\s\"\\d+");
            Matcher m = p.matcher(doc2.getElementsByTag("script").html());
            if (m.find()) agent.setPhone(m.group(0).substring(21, m.group(0).length()));

            logger.info(agent.getName() + "\n" + agent.getCompany() + "\n" + agent.getBusiness() + "\n"
                + agent.getLocation() + "\n" + agent.getPhone() + "\n" + agent.getIntroduce());
            //保存数据库
            agentRepository.insert(agent);
            logger.info(agent.getName() + " 保存成功!");
        }
    }

    //Jsoup下载页面
    public static Document connect(String url, String agent, int time) {
        try {
            time += 1;
            logger.info("下载链接页面中,HTTP请求第 " + time + " 次.");
            if (agent == null) {
                return Jsoup.connect(url).get();
            } else {
                return Jsoup.connect(url).userAgent(agent).get();
            }
        } catch (Exception e) {
            //限定重试5次
            if (time < 5) {
                return connect(url, agent, time);
            } else {
                logger.error("页面下载异常!", e);
                return null;
            }
        }
    }

    //获取链接地址
    public static String getHref(Document doc, String id, String target) throws Exception {
        Element links = doc.getElementById(id);
        //若分页元素为空,返回0
        if (links == null) return "0";
        Elements selects = links.select("a:contains(" + target + ")");
        if (selects.isEmpty()) throw new Exception("未查询到目标: " + target);
        String href = selects.first().attr("href");
        return href.substring(7, href.length());
    }

    //I/O文件读取
    public static String read(String filePath) {
        try {
            String fileContent;
            final File file = new File(filePath);
            final Long size = file.length();
            byte[] buff = new byte[size.intValue()];
            final FileInputStream fs = new FileInputStream(file);
            fs.read(buff);
            fs.close();
            fileContent = new String(buff, "utf-8");
            return fileContent;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
