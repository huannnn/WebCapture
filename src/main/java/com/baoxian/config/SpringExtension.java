package com.baoxian.config;

import akka.actor.Props;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringExtension {

    @Autowired
    private ApplicationContext applicationContext;

    public void initialize(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Props props(String actorBeanName) {
        return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
    }

    public Props props(Class<?> actorClass) {
        String actorBeanName = retrieveBeanName(actorClass);
        return props(actorBeanName);
    }

    private String retrieveBeanName(Class<?> actorClass) {
        String actorClassSimpleName = actorClass.getSimpleName();
        return actorClassSimpleName.substring(0, 1).toLowerCase() + actorClassSimpleName.substring(1);
    }
}
