package com.baoxian.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileInputStream;

public class Util {

    private static Logger logger = Logger.getLogger(Util.class);

    private static ObjectMapper mapper = new ObjectMapper();

    //设置超时时间
    private static final int timeout = 60000;
    private static final String pcAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36";
    private static final String wxAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_2 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile";
    private static final String nx5xAgent = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Mobile Safari/537.36";
    private static final String ip6Agent = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1";
    private static final String nx6pAgent = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Mobile Safari/537.36";
    private static final String gs5Agent = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Mobile Safari/537.36";

    //Jsoup下载js
    public static Document get(String url, int time) {
        try {
            time += 1;
            logger.info("下载页面JS中,HTTP请求第 " + time + " 次.");
            return Jsoup.connect(url).ignoreContentType(true).timeout(timeout).get();
        } catch (Exception e) {
            //限定重试5次
            if (time < 5) {
                return get(url, time);
            } else {
                logger.error("JS下载异常!", e);
                return null;
            }
        }
    }

    //Jsoup下载页面
    public static Document get(String url, int agentWay, int time) {
        try {
            time += 1;
            String agent = "", desc = "";
            switch (agentWay) {
                case 1: agent = pcAgent; desc = "PC代理"; break;
                case 2: agent = wxAgent; desc = "iphone 6s plus微信浏览器代理"; break;
                case 3: agent = nx5xAgent; desc = "nexus 5x代理"; break;
                case 4: agent = ip6Agent; desc = "iphone 6代理"; break;
                case 5: agent = nx6pAgent; desc = "nexus 6p代理"; break;
                case 6: agent = gs5Agent; desc = "galaxy s5代理"; break;
            }
            logger.info(desc + "下载链接页面中,HTTP请求第 " + time + " 次.");
            return Jsoup.connect(url).userAgent(agent).timeout(timeout).get();
        } catch (Exception e) {
            //限定重试5次
            if (time < 5) {
                return get(url, agentWay, time);
            } else {
                logger.error("页面下载异常!", e);
                return null;
            }
        }
    }

    //JackSon反序列化Json字符串
    public static <T> T transJson(String json, Class<T> clazz) {
        if (json == null && !"".equals(json) && !"null".equals(json)) return null;
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("JackSon反序列化异常：", e);
            return null;
        }
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
