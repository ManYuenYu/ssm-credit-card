package com.ioioh.ssm.repository;

//import org.springframework.data.jpa.repository.JpaRepository;

import com.ioioh.ssm.domain.Payment;
import com.ioioh.ssm.mapper.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-06 5:42 PM
 */
@Repository
public class PaymentRepository {

    @Autowired
    private PaymentMapper paymentMapper;


    public List<Payment> paymentList(){
        List<Payment> payments = paymentMapper.selectAll();
        return payments;
    }

    /**
     * 获取单个支付对象
     * @param id
     * @return
     */
    public Payment getOne(Long id) {
        return paymentMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存支付对象
     * @param payment
     * @return
     */
    public Payment save(Payment payment) {
        paymentMapper.insert(payment);
        Payment newPayment = null;
        if (null != payment.getId()) {
            newPayment = paymentMapper.selectByPrimaryKey(payment.getId());
        }
        return newPayment;
    }


    /**
     * 更新支付对象
     * @param payment
     * @return
     */
    public Payment update(Payment payment) {
        paymentMapper.updateByPrimaryKey(payment);

        return paymentMapper.selectByPrimaryKey(payment.getId());

    }
}
