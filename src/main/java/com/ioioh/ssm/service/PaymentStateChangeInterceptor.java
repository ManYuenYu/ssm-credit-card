package com.ioioh.ssm.service;

import com.ioioh.ssm.domain.Payment;
import com.ioioh.ssm.domain.PaymentEvent;
import com.ioioh.ssm.domain.PaymentState;
import com.ioioh.ssm.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * 状态机状态转变拦截器
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-06 9:14 PM
 */
@RequiredArgsConstructor
@Component
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * 转换到次要状态前，先把支付对象更新到数据库
     */
    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message,
                               Transition<PaymentState, PaymentEvent> transition,
                               StateMachine<PaymentState, PaymentEvent> stateMachine) {
        System.out.println("==========状态准备发生改变==========");
        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER, -1L)))
                    .ifPresent(paymentId -> {
                        Payment payment = paymentRepository.getOne(paymentId);
                        System.out.println("当前对象保存前状态:" +  payment.getState());
                        payment.setState(state.getId().toString());
                        payment.setUpdatedTime(new Date());
                        paymentRepository.update(payment);
                        System.out.println("当前对象保存后状态:" +  payment.getState());
                    });
        });
        System.out.println("==========支付状态完成更新==========");
    }
}
