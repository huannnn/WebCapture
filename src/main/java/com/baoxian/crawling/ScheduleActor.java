package com.baoxian.crawling;

import akka.actor.UntypedActor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ScheduleActor extends UntypedActor {

    private static Logger logger = Logger.getLogger(ScheduleActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            logger.info("收到消息通知" + message);
            getSender().tell("已收到！", getSelf());
        } else {
            unhandled(message);
        }
    }
}
