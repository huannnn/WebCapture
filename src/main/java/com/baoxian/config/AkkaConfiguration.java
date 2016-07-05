package com.baoxian.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfiguration {

    private static Logger logger = Logger.getLogger(AkkaConfiguration.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SpringExtension springExtension;

    @Bean(destroyMethod = "shutdown")
    public ActorSystem actorSystem() {
        try {
            ActorSystem system = ActorSystem.create("wc-akka-system");
            springExtension.initialize(applicationContext);
            logger.info("WebCapture ActorSystem 初始化...");
            return system;
        } catch (Exception e) {
            logger.error("WebCapture ActorSystem 启动失败!");
            return null;
        }
    }

    /*@Bean
    public Config akkaConfiguration() {
        return ConfigFactory.load();
    }*/
}
