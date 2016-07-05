package com.baoxian.config;

import akka.actor.ActorSystem;
import com.baoxian.crawling.BxdProcessor;
import com.baoxian.domain.Agent;
import com.baoxian.domain.AgentRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StartupRunner implements CommandLineRunner {

    private static Logger logger = Logger.getLogger(StartupRunner.class);

    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private SpringExtension extension;

    @Autowired
    private BxdProcessor bxdProcessor;

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public void run(String... strings) throws Exception {
        logger.info(">>>>>>>>>>开始执行网页代理人抓取任务<<<<<<<<<<");
        bxdProcessor.process("http://www.bxd365.com/agent/", "全国", null);
    }
}
