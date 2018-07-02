package com.yifan.test;

import com.yifan.entity.Order;
import com.yifan.enums.OrderEvents;
import com.yifan.enums.OrderStates;
import com.yifan.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author: wuyifan
 * @date: 2018年07月01日 17:15
 */
@Component
@Slf4j
public class Runner implements ApplicationRunner {
    
    @Resource
    private StateMachineFactory<OrderStates, OrderEvents> factory;

    @Resource
    private OrderService orderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Order order = orderService.create(new Date());
        log.info("订单当前状态为：{}", order);
        log.info("支付中。。。");
        boolean payFlag = orderService.pay(order.getId());

        if (payFlag) {
            log.info("支付完成，订单当前状态为：{}", orderService.findOne(order.getId()));
        } else {
            log.info("支付失败");
        }

        log.info("发货中。。。");
        boolean deliverFlag = orderService.deliver(order.getId());
        if (deliverFlag) {
            log.info("发货完成，订单当前状态为：{}", orderService.findOne(order.getId()));
        } else {
            log.info("发货失败");
        }

        log.info("收货中。。。");
        boolean receiveFlag = orderService.received(order.getId());
        if (receiveFlag) {
            log.info("收货完成，订单当前状态为：{}", orderService.findOne(order.getId()));
        } else {
            log.info("收货失败");
        }

    }

    private void testStateMachine() {
        StateMachine<OrderStates, OrderEvents> sm = factory.getStateMachine("12345");

        sm.start();

        log.info("current state machine is {}", sm.getState().getId().name());

        Message<OrderEvents> message = MessageBuilder.withPayload(OrderEvents.PAY).build();

        sm.sendEvent(message);

        log.info("current state machine is {}", sm.getState().getId().name());
    }
}
