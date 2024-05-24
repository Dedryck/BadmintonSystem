package com.coding24.badmintonsystem_1.service.impl;

import com.coding24.badmintonsystem_1.entity.Payment;
import com.coding24.badmintonsystem_1.mapper.PaymentMapper;
import com.coding24.badmintonsystem_1.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentServiceImpl(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    @Override
    public Payment findById(Integer paymentID) {
        return paymentMapper.findById(paymentID);
    }

    @Override
    public List<Payment> findAll() {
        return paymentMapper.findAll();
    }

    @Override
    public void insertPayment(Payment payment) {
        paymentMapper.insertPayment(payment);
    }

    @Override
    public void updatePayment(Payment payment) {
        paymentMapper.updatePayment(payment);
    }

    @Override
    public void deletePayment(Integer paymentID) {
        paymentMapper.deletePayment(paymentID);
    }
}
