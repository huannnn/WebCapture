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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 向日葵 http://www.xiangrikui.com/
 * Created by huan on 16/7/2.
 */
@Component
public class XrkProcessor {

    private static Logger logger = Logger.getLogger(XrkProcessor.class);

    @Autowired
    private AgentRepository agentRepository;

    public static void main(String[] args) throws Exception {
        //process("http://a.xiangrikui.com", "广东", null);
    }

    public void process(String baseUrl, String province, String city) throws Exception {
        String js = "http://common.xiangrikui.com/api/v1/locate/provinces/";
        Document prv = Util.get(js, 1);
        List prvList = Util.transJson(prv.text(), List.class);
        String prvCode = "";
        for (Object obj : prvList) {
            Map map = (Map) obj;
            if (map.get("province_name").toString().contains(province)) {
                prvCode = map.get("id").toString();
            }
        }
        Document ct = Util.get(js + prvCode + "/cities", 1);
        List ctList = Util.transJson(ct.text(), List.class);
        Document doc;
        String ctCode = "";
        for (Object obj : ctList) {
            Map map = (Map) obj;
            ctCode = map.get("id").toString();
            if (city != null) {

            } else {
                logger.info(">>>>>>>>>>开始抓取 " + map.get("city_name") + " 地区数据<<<<<<<<<<");
                String url = baseUrl + "/sf" + prvCode + "-cs" + ctCode + "/gs.html";
                Document doc2 = Util.get(url, 1, 0);
                String page = doc2.select("a[class=flow-center-item gray999 pageList-link]").last().attr("href");
                int num = 1;
                if (!"".equals(page)) {
                    num = Integer.parseInt(page.substring(page.indexOf("?page=") + 6, page.length()));
                }
                logger.info("共有 " + num + " 页结果列表分页！>>>>>>>>>>第 1 页<<<<<<<<<<");
                for (int i = 1; i <= num; i++) {
                    if (i == 1) {
                        getDesc(doc2, province);
                    } else {
                        String pgUrl = url + "?page=" + i;
                        logger.info("page: " + url);
                        logger.info(">>>>>>>>>>>>>>>>>>>>第 " + i + " 页<<<<<<<<<<<<<<<<<<<<");
                        getDesc(Util.get(pgUrl, 1, 0), province);
                    }
                }
            }
        }
    }

    private void getDesc(Document document, String province) throws Exception {
        Elements els = document.select("[class=agent-info border-org]");
        int index = 1;
        for (Element el : els) {
            logger.info("抓取该页第 " + index + " 条...");
            Agent agent = new Agent();
            agent.setName(el.select("h2[class=agent-name]").text());
            String str = el.select("p[class=agent-company]").text();
            agent.setLocation(province + " " + str.substring(0, str.indexOf(" ,")));
            agent.setCompany(str.substring(str.indexOf(",") + 1, str.length()).trim());

            String id = el.select("div[class=agent-header]").attr("data-imgid");
            Document tmp = Util.get("http://common.xiangrikui.com/ytx/get_phone_info/" + id + "/a_index", 1);
            Map mp = Util.transJson(tmp.text(), Map.class);
            if (mp.get("status_code") != null && mp.get("status_code").equals("200") && mp.get("phone") != null) {
                agent.setPhone(mp.get("phone").toString());
            }
            agent.setStamp(null);
            agent.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            logger.info(agent.getName() + "\n" + agent.getCompany() + "\n" + agent.getLocation() + "\n" + agent.getPhone());
            agentRepository.save(agent);
            index += 1;
        }
    }
}
