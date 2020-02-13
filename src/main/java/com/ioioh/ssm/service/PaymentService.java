package com.ioioh.ssm.service;

import com.ioioh.ssm.domain.Payment;
import com.ioioh.ssm.domain.PaymentEvent;
import com.ioioh.ssm.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

/**
 * 信用卡支付服务
 *
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-06 8:04 PM
 */
public interface PaymentService {

    /**
     * 创建信用卡支付
     * @param payment
     * @return 支付对象
     */
    Payment newPayment(Payment payment);

    /**
     *  信用卡预授权
      * @param paymentId
     * @return
     */
    Payment preAuth(Long paymentId) throws Exception;

    /**
     * 信用卡授权
     * @param paymentId
     * @return
     */
    Payment authorizePayment(Long paymentId) throws Exception;

    @Deprecated
    StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) throws Exception;

}
