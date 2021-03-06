package com.baoxian.config;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.baoxian.crawling.BxdProcessor;
import com.baoxian.crawling.DajiabaoProcessor;
import com.baoxian.crawling.LifeSkyProcessor;
import com.baoxian.crawling.XrkProcessor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "capture", locations = "classpath:config/seeds.yaml")
public class StartupRunner implements CommandLineRunner {

    private static Logger logger = Logger.getLogger(StartupRunner.class);

    private List<String> seeds = new ArrayList<>();

    public List<String> getSeeds() {
        return this.seeds;
    }

    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private SpringExtension springExtension;

    @Autowired
    private BxdProcessor bxdProcessor;

    @Autowired
    private DajiabaoProcessor dajiabaoProcessor;

    @Autowired
    private LifeSkyProcessor lifeSkyProcessor;

    @Autowired
    private XrkProcessor xrkProcessor;

    @Override
    public void run(String... strings) throws Exception {
        try {
            logger.info(">>>>>>>>>>开始执行网页代理人抓取任务<<<<<<<<<<");

            //bxdProcessor.process("http://www.bxd365.com/agent/", "广东", null);
            //dajiabaoProcessor.process("http://www.dajiabao.com/guwen", "广东", null);
            //lifeSkyProcessor.process("http://www.baoxian360.net/index.asp", "广东", null);
            xrkProcessor.process("http://a.xiangrikui.com", "广东", null);
            ActorSelection schedule = actorSystem.actorSelection("/user/schedule-actor");
            schedule.tell("123", null);
            /*for (String seed : seeds) {
                System.out.println(seed);
            }*/
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
