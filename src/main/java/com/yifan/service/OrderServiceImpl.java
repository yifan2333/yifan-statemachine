package com.yifan.service;

import com.yifan.entity.Order;
import com.yifan.enums.OrderEvents;
import com.yifan.enums.OrderStates;
import com.yifan.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author: wuyifan
 * @date: 2null18年null7月null1日 15:35
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_ID_MESSAGE_HEADER = "orderId";

    @Resource
    private OrderRepository orderRepository;
    @Resource
    private StateMachineFactory<OrderStates, OrderEvents> factory;

    @Override
    public Order findOne(Long id) {
        return orderRepository.findById(id).orElse(new Order());
    }

    @Override
    public Order create(Date when) {
        return orderRepository.save(new Order(when));
    }

    @Override
    public boolean pay(Long id) {

        StateMachine<OrderStates, OrderEvents> sm = this.build(id);

        Message<OrderEvents> message= MessageBuilder.withPayload(OrderEvents.PAY)
                .setHeader(ORDER_ID_MESSAGE_HEADER, id)
                .build();
        return sm.sendEvent(message);
    }

    @Override
    public boolean deliver(Long id) {
        StateMachine<OrderStates, OrderEvents> sm = this.build(id);

        Message<OrderEvents> message= MessageBuilder.withPayload(OrderEvents.DELIVERY)
                .setHeader(ORDER_ID_MESSAGE_HEADER, id)
                .build();
        return sm.sendEvent(message);
    }

    @Override
    public boolean received(Long id) {
        StateMachine<OrderStates, OrderEvents> sm = this.build(id);

        Message<OrderEvents> message= MessageBuilder.withPayload(OrderEvents.RECEIVED)
                .setHeader(ORDER_ID_MESSAGE_HEADER, id)
                .build();
        return sm.sendEvent(message);
    }

    @Override
    public boolean cancel(Long id) {
        StateMachine<OrderStates, OrderEvents> sm = this.build(id);

        Message<OrderEvents> message= MessageBuilder.withPayload(OrderEvents.CANCEL)
                .setHeader(ORDER_ID_MESSAGE_HEADER, id)
                .build();
        return sm.sendEvent(message);
    }

    private StateMachine<OrderStates,OrderEvents> build(Long id) {

        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            log.info("创建状态机时，订单状态为：{}", order);

            String orderIdKey = Long.toString(order.getId());

            StateMachine<OrderStates, OrderEvents> sm = factory.getStateMachine(orderIdKey);

            sm.stop();
            sm.getStateMachineAccessor()
                    .doWithAllRegions(sma -> {
                        sma.addStateMachineInterceptor(new StateMachineInterceptorAdapter<OrderStates, OrderEvents>() {
                            @Override
                            public void preStateChange(State<OrderStates, OrderEvents> state, Message<OrderEvents> message, Transition<OrderStates, OrderEvents> transition, StateMachine<OrderStates, OrderEvents> stateMachine) {
                                Optional.ofNullable(message)
                                        .flatMap(msg -> Optional.ofNullable((Long) msg.getHeaders().getOrDefault("orderId", -1L)))
                                        .ifPresent(orderId -> {
                                            order.setState(state.getId().name());
                                            orderRepository.save(order);
                                        });
                            }
                        });
                        sma.resetStateMachine(new DefaultStateMachineContext<>(order.getStatusEnum(), null, null, null));
                    });
            sm.start();
            return sm;
        }
        throw new RuntimeException("order id " + id + "not found");
    }
}
