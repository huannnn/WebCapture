package com.baoxian;

import com.baoxian.crawling.BxdProcessor;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebCaptureApplication.class)
@WebAppConfiguration
public class WebCapture {

	private static Logger logger = Logger.getLogger(WebCapture.class);

	@Autowired
	private BxdProcessor bxdProcessor;

	@Test
	public void crawling() {
		System.out.println("------开始抓取网页代理人-----");
		try {
			logger.info("开始抓取");
			bxdProcessor.process("http://www.bxd365.com/agent/", "广东", "广州");
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
