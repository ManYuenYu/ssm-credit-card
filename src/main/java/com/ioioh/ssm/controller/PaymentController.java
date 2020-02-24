package com.ioioh.ssm.controller;

import com.ioioh.ssm.domain.Payment;
import com.ioioh.ssm.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-07 5:21 PM
 */
@RestController
@RequestMapping("/credit_card")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping("/payment_list")
    public List<Payment> paymentList(){
        return paymentService.paymentList();
    }

    /**
     * 创建信用卡支付
     * @param payment
     * @return 支付对象
     */
    @RequestMapping("/new_payment")
    public Payment newPayment(@RequestBody Payment payment) {
        return paymentService.newPayment(payment);
    }

    /**
     * 信用卡预授权
     * @param paymentId
     * @return 支付对象
     */
    @RequestMapping("/pre_auth/{id}")
    public Payment preAuth(@PathVariable("id") Long paymentId) throws Exception {
        return paymentService.preAuth(paymentId);
    }

    /**
     * 信用卡授权
     * @param paymentId
     * @return 支付对象
     */
    @RequestMapping("/authorize_payment/{id}")
    public Payment authorizePayment(@PathVariable("id") Long paymentId) throws Exception {
        return paymentService.authorizePayment(paymentId);
    }

}
