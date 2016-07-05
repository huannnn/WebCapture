package com.baoxian.common;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;

public class Util {

    private static Logger logger = Logger.getLogger(Util.class);

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
