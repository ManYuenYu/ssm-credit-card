package com.ioioh.ssm.mapper;

import com.ioioh.ssm.domain.Payment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Payment record);

    Payment selectByPrimaryKey(Long id);

    List<Payment> selectAll();

    int updateByPrimaryKey(Payment record);
}