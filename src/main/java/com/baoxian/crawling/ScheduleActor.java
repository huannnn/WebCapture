package com.baoxian.crawling;

import akka.actor.UntypedActor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ScheduleActor extends UntypedActor {

    private static Logger logger = Logger.getLogger(ScheduleActor.class);

    private static int count = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            System.out.println("==========>" + message +"======>>>" + (++count));
        } else {
            unhandled(message);
        }
    }
}
