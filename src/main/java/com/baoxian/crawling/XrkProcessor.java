package com.baoxian.crawling;

import com.baoxian.common.Util;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

/**
 * 向日葵 http://www.xiangrikui.com/
 * Created by huan on 16/7/2.
 */
public class XrkProcessor {

    private static Logger logger = Logger.getLogger(XrkProcessor.class);

    public static void main(String[] args) throws Exception {
        process("http://a.xiangrikui.com", "广东", null);
    }

    public static void process(String baseUrl, String province, String city) throws Exception {

    }
}
