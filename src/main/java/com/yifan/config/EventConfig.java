package com.yifan.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author: wuyifan
 * @date: 2018年07月02日 10:35
 */
@Configuration
@WithStateMachine
public class EventConfig {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @OnTransition(target = "WAIT_PAYMENT")
    public void create() {
        logger.info("订单创建，待支付");
    }

    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
    public void pay() {
        logger.info("用户完成支付，待发货");
    }

    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    public void deliver() {
        logger.info("发货完成，待收货");
    }

    @OnTransition(source = "WAIT_RECEIVE", target = "FINISH")
    public void received() {
        logger.info("用户收货，订单完成");
    }
}
