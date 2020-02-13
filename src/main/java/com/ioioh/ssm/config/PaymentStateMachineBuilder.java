package com.ioioh.ssm.config;

import com.ioioh.ssm.domain.PaymentEvent;
import com.ioioh.ssm.domain.PaymentState;
import com.ioioh.ssm.service.PaymentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Random;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-10 5:06 PM
 */
@Slf4j
@Component
public class PaymentStateMachineBuilder extends StateMachineConfigurerAdapter implements CreditCardStateMachineBuilder {

    @Override
    public String getName() {
        return PAYMENT_BUILDER_NAME;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> build(BeanFactory beanFactory) throws Exception {

        StateMachineBuilder.Builder<PaymentState, PaymentEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .beanFactory(beanFactory)
                .machineId(getName())
                .listener(createListenrAdapter());

        builder.configureStates()
                .withStates()
                .initial(PaymentState.NEW)
                .states(EnumSet.allOf(PaymentState.class))
                .end(PaymentState.AUTH)
                .end(PaymentState.PRE_AUTH_ERROR)
                .end(PaymentState.AUTH_ERROR);

        builder.configureTransitions()
                // === 预授权流程 ===
                .withInternal()
                .source(PaymentState.NEW)
                .event(PaymentEvent.PRE_AUTHORIZE)
                // 触发预授权处理
                .action(preAuthAction())
                .and()
                .withExternal()
                .source(PaymentState.NEW)
                .target(PaymentState.PRE_AUTH)
                .event(PaymentEvent.PRE_AUTH_APPROVED)
                .and()
                .withExternal()
                .source(PaymentState.NEW)
                .target(PaymentState.PRE_AUTH_ERROR)
                .event(PaymentEvent.PRE_AUTH_DECLINED)
                // === 预授权到授权 ===
                .and()
                .withExternal()
                .source(PaymentState.PRE_AUTH)
                .target(PaymentState.PRE_AUTH)
                .event(PaymentEvent.AUTHORIZE)
                // 触发授权处理
                .action(authAction())
                .and()
                .withExternal()
                .source(PaymentState.PRE_AUTH)
                .target(PaymentState.AUTH)
                .event(PaymentEvent.AUTH_APPROVED)
                .and()
                .withExternal()
                .source(PaymentState.PRE_AUTH)
                .target(PaymentState.AUTH_ERROR)
                .event(PaymentEvent.AUTH_DELIENED);

        return builder.build();
    }


    public void errorAction(){

    }

    /**
     * 配置状态机监听器
     *
     * @throws Exception
     */
    public StateMachineListenerAdapter<PaymentState, PaymentEvent> createListenrAdapter() throws Exception {
        StateMachineListenerAdapter<PaymentState, PaymentEvent> adapter = new StateMachineListenerAdapter() {

            @Override
            public void stateChanged(State from, State to) {
                log.info("==================状态转换==================");
                log.info(String.format("(从状态:%s, 转变为: %s)", from, to));
            }
        };
        return adapter;
    }

    /**
     * 以随机方式来触发预授权的通过或拒绝
     *
     * @return
     */
    public Action<PaymentState, PaymentEvent> preAuthAction() {
        return context -> {
            System.out.println("正在进行预授权........！");

            if (new Random().nextInt(10) < 6) {
                System.out.println("预授权申请通过");
                context.getStateMachine()
                        .sendEvent(MessageBuilder
                                .withPayload(PaymentEvent.PRE_AUTH_APPROVED)
                                .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
                                .build());
            } else {
                System.out.println("预授权被拒绝，额度不足");
                context.getStateMachine()
                        .sendEvent(MessageBuilder
                                .withPayload(PaymentEvent.PRE_AUTH_DECLINED)
                                .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
                                .build());
            }
        };
    }

    /**
     * 以随机方式来触发授权的通过或拒绝
     *
     * @return
     */
    public Action<PaymentState, PaymentEvent> authAction() {
        return context -> {
            System.out.println("正在进行授权........！");

            if (new Random().nextInt(10) < 6) {
                System.out.println("授权通过");
                context.getStateMachine()
                        .sendEvent(MessageBuilder
                                .withPayload(PaymentEvent.AUTH_APPROVED)
                                .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
                                .build());
            } else {
                System.out.println("授权不通过，额度不足");
                context.getStateMachine()
                        .sendEvent(MessageBuilder
                                .withPayload(PaymentEvent.AUTH_DELIENED)
                                .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
                                .build());
            }
        };


    }
}
