package com.baoxian;

import akka.actor.ActorSystem;
import com.baoxian.config.SpringExtension;
import com.baoxian.actor.ScheduleActor;
import com.typesafe.config.ConfigFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebCaptureApplication {

    private static Logger logger = Logger.getLogger(WebCaptureApplication.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SpringExtension springExtension;

    @Bean(destroyMethod = "shutdown")
    public ActorSystem actorSystem() {
        try {
            ActorSystem system = ActorSystem.create("wc-akka-system", ConfigFactory.load("config/akka"));
            system.actorOf(springExtension.props(ScheduleActor.class), "schedule-actor");
            springExtension.initialize(applicationContext);
            logger.info("WebCapture ActorSystem 初始化...");
            return system;
        } catch (Exception e) {
            logger.error("WebCapture ActorSystem 启动失败!", e);
            return null;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(WebCaptureApplication.class, args);
    }
}
