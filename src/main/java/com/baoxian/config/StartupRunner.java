package com.baoxian.config;

import akka.actor.ActorSystem;
import com.baoxian.crawling.BxdProcessor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private static Logger logger = Logger.getLogger(StartupRunner.class);

    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private SpringExtension extension;

    @Autowired
    private BxdProcessor bxdProcessor;

    @Override
    public void run(String... strings) throws Exception {
        logger.info(">>>>>>>>>>开始执行网页代理人抓取任务<<<<<<<<<<");
        //bxdProcessor.process("http://www.bxd365.com/agent/", "全国", null);

    }
}
