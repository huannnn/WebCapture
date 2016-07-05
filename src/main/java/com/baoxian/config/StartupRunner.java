package com.baoxian.config;

import com.baoxian.domain.Task;
import com.baoxian.domain.TaskRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StartupRunner implements CommandLineRunner {

    private static Logger logger = Logger.getLogger(StartupRunner.class);

    @Override
    public void run(String... strings) throws Exception {
        logger.info(">>>>>>>>>>开始执行网页代理人抓取任务<<<<<<<<<<");

    }
}
