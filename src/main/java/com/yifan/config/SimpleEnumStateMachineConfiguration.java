package com.yifan.config;

import com.yifan.enums.OrderEvents;
import com.yifan.enums.OrderStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author: wuyifan
 * @date: 2018年07月01日 15:37
 */
@EnableStateMachineFactory
@Configuration
public class SimpleEnumStateMachineConfiguration extends StateMachineConfigurerAdapter<OrderStates, OrderEvents> {

    private Logger logger = LoggerFactory.getLogger(SimpleEnumStateMachineConfiguration.class);

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderStates.WAIT_PAYMENT).target(OrderStates.WAIT_DELIVER).event(OrderEvents.PAY)
                .and().withExternal()
                .source(OrderStates.WAIT_DELIVER).target(OrderStates.WAIT_RECEIVE).event(OrderEvents.DELIVERY)
                .and().withExternal()
                .source(OrderStates.WAIT_RECEIVE).target(OrderStates.FINISH).event(OrderEvents.RECEIVED)
                .and().withExternal()
                .source(OrderStates.WAIT_PAYMENT).target(OrderStates.CLOSED).event(OrderEvents.CANCEL)
                .and().withExternal()
                .source(OrderStates.WAIT_DELIVER).target(OrderStates.CLOSED).event(OrderEvents.CANCEL)
                .and().withExternal()
                .source(OrderStates.WAIT_RECEIVE).target(OrderStates.CLOSED).event(OrderEvents.CANCEL);

    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
        states.withStates()
                .initial(OrderStates.WAIT_PAYMENT)
                .states(EnumSet.allOf(OrderStates.class));
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config) throws Exception {
        StateMachineListenerAdapter<OrderStates, OrderEvents> listenerAdapter = new StateMachineListenerAdapter<OrderStates, OrderEvents>(){
            @Override
            public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
                logger.info("状态从 {} 改变为 {}", from + "", to + "");
            }
        };

        config.withConfiguration().autoStartup(false).listener(listenerAdapter);
    }
}
