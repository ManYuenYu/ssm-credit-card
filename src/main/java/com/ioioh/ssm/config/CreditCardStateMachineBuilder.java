package com.ioioh.ssm.config;

import com.ioioh.ssm.domain.PaymentEvent;
import com.ioioh.ssm.domain.PaymentState;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;

/**
 * 状态机构造器定义
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-10 12:36 AM
 */
public interface CreditCardStateMachineBuilder {

    String getName();

    StateMachine<PaymentState, PaymentEvent> build(BeanFactory beanFactory) throws Exception;

    /**
     * 业务一对应的builder name
      */
    String PAYMENT_BUILDER_NAME = "paymentStateMachineBuilder";


    String REFUND_BUILDER_NAME = "refundStateMachineBuilder";
}
