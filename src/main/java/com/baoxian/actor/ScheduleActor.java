package com.baoxian.actor;

import akka.actor.UntypedActor;
import akka.dispatch.Futures;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import scala.concurrent.Future;
import java.util.Random;
import java.util.concurrent.Callable;

@Component
public class ScheduleActor extends UntypedActor {

    private static Logger logger = Logger.getLogger(ScheduleActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            logger.info("收到消息通知" + message);
            Future f = Futures.future(new Callable() {
                @Override
                public Object call() throws Exception {
                    Thread.sleep(6000);
                    if (new Random(System.currentTimeMillis()).nextBoolean()){
                        return "Hello"+"World!";
                    }else {
                        throw new IllegalArgumentException("参数错误");
                    }
                }
            }, getContext().dispatcher());

            System.out.println("this is the outside!!!");

            f.onSuccess(new OnSuccess() {
                @Override
                public void onSuccess(Object result) throws Throwable {
                    System.out.println("======success==>>" + result);
                }
            }, getContext().dispatcher());

            f.onFailure(new OnFailure() {
                @Override
                public void onFailure(Throwable failure) throws Throwable {
                    System.out.println("=====failure==>>");
                }
            }, getContext().dispatcher());
        } else {
            unhandled(message);
        }
    }
}
