package com.baoxian.crawling;

import com.baoxian.common.HttpSender;
import com.baoxian.common.Util;
import com.baoxian.domain.Agent;
import com.baoxian.domain.AgentRepository;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * 保险中间站 http://www.life-sky.net
 * Created by huan on 16/7/3.
 */
public class LifeSkyProcessor {

    private static Logger logger = Logger.getLogger(LifeSkyProcessor.class);

    @Autowired
    private AgentRepository agentRepository;

    public static void main(String[] args) throws Exception {
        //process("http://www.baoxian360.net/index.asp", "广东", null);
    }

    public void process(String baseUrl, String province, String city) throws Exception {
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
        sb.append("select_sou=index")
                .append("&atplace=" + province + "省")
                .append("&atcity=" + (city == null ? "" : city))
                .append("&type=保险中介")
                .append("&corp=0&corpzj=&nname=&sex=0&age1=18&age2=65&type1=&corp1=&ifuse=0&productkey=&projectkey=");
        Document doc = Jsoup.parse(HttpSender.post(sb.toString(), baseUrl));

        System.out.println(doc);

        String page = doc.select("input[id=tolpage]").attr("value");
        logger.info("共有 " + page + " 页结果列表分页！>>>>>>>>>>第 1 页<<<<<<<<<<");
        int i = 1;
        if (page != null) {
            int num = Integer.parseInt(page);
            for (; i <= num; i++) {
                if (i == 1) {
                    getDesc(doc);
                } else {
                    logger.info(">>>>>>>>>>>>>>>>>>>>第 " + i + " 页<<<<<<<<<<<<<<<<<<<<");
                    String url = baseUrl + "?page=" + i;
                    getDesc(Jsoup.parse(HttpSender.post(sb.toString(), url)));
                }
            }
        }
    }

    private void getDesc(Document document) throws Exception {
        Element el = document.select("td[class=f]").first();
        Elements fonts = el.select("font[size=-1]");
        int index = 1;
        for (Element font : fonts) {
            logger.info("抓取该页第 " + index + " 条...");
            String text = font.ownText();
            String[] strs = text.replaceAll("　", "@").replaceAll(" ", "@").split("@");
            if (strs.length != 9) throw new Exception("文本内容解析数组有误!");
            Agent agent = new Agent();
            agent.setName(strs[0]);
            agent.setSex(strs[1]);
            agent.setLocation(strs[4]);
            agent.setCompany(strs[5]);
            String phone = strs[6].substring(strs[6].indexOf("MP:"), strs[6].indexOf("，Email"));
            agent.setPhone(phone);
            agent.setStamp(null);
            agent.setUuid(UUID.randomUUID().toString());

            logger.info(agent.getName() + "\n" + agent.getSex() + "\n" + agent.getCompany() + "\n"
                    + agent.getLocation() + "\n" + agent.getPhone() + "\n");
            agentRepository.save(agent);
        }
    }
}
