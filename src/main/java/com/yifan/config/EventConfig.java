package com.yifan.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author: wuyifan
 * @date: 2018年07月02日 10:35
 */
@Slf4j
@Configuration
@WithStateMachine
public class EventConfig {

    @OnTransition(target = "WAIT_PAYMENT")
    public void create() {
        log.info("订单创建，待支付");
    }

    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
    public void pay() {
        log.info("用户完成支付，待发货");
    }

    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    public void deliver() {
        log.info("发货完成，待收货");
    }

    @OnTransition(source = "WAIT_RECEIVE", target = "FINISH")
    public void received() {
        log.info("用户收货，订单完成");
    }
}
