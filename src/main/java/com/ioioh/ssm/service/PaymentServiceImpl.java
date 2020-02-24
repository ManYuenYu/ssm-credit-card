package com.ioioh.ssm.service;

import com.ioioh.ssm.config.PaymentStateMachineBuilder;
import com.ioioh.ssm.domain.Payment;
import com.ioioh.ssm.domain.PaymentEvent;
import com.ioioh.ssm.domain.PaymentState;
import com.ioioh.ssm.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-06 8:08 PM
 */
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    public static final String PAYMENT_ID_HEADER = "payment_id";

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private PaymentStateMachineBuilder paymentStateMachineBuilder;
    @Autowired
    private PaymentStateChangeInterceptor paymentStateChangeInterceptor;


    @Override
    public List<Payment> paymentList() {
        List<Payment> payments = paymentRepository.paymentList();
        return payments;
    }

    @Override
    public Payment newPayment(Payment payment) {
        Optional.ofNullable(payment.getAmount()).ifPresent(pm ->{
            payment.setState(PaymentState.NEW.toString());
            payment.setAmount(payment.getAmount());
            payment.setCreatedTime(new Date());
            payment.setUpdatedTime(new Date());
        });
        return paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public Payment preAuth(Long paymentId) throws Exception {

        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, PaymentEvent.PRE_AUTHORIZE);

        return paymentRepository.getOne(paymentId);
    }

    @Transactional
    @Override
    public Payment authorizePayment(Long paymentId) throws Exception {

        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, PaymentEvent.AUTHORIZE);

        return paymentRepository.getOne(paymentId);
    }

    @Deprecated
    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) throws Exception {
        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);

        sendEvent(paymentId, sm, PaymentEvent.AUTH_DELIENED);

        return sm;
    }

    /**
     *  使用Spring Message封装事件
     * @param paymentId
     * @param sm
     * @param event
     */
    private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> sm, PaymentEvent event) {
        Message msg = MessageBuilder
                        .withPayload(event)
                        .setHeader(PAYMENT_ID_HEADER, paymentId)
                        .build();

        sm.sendEvent(msg);
    }


    /**
     * 重设状态机状态
     * @param paymentId
     * @return
     */
    private StateMachine<PaymentState, PaymentEvent> build(Long paymentId) throws Exception {
        Payment payment = paymentRepository.getOne(paymentId);


        // 创建状态机并设置状态机id
        StateMachine<PaymentState, PaymentEvent> sm = paymentStateMachineBuilder.build(beanFactory);

        sm.stop();

        // 重设状态机的初始状态
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(paymentStateChangeInterceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(PaymentState.getByStatus(payment.getState()) , null, null, null));
                });

        sm.start();

        return sm;
    }
}
