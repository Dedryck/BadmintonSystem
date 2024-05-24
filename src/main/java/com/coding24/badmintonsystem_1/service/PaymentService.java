package com.coding24.badmintonsystem_1.service;

import com.coding24.badmintonsystem_1.entity.Payment;

import java.util.List;

public interface PaymentService {
    Payment findById(Integer paymentID);
    List<Payment> findAll();
    void insertPayment(Payment payment);
    void updatePayment(Payment payment);
    void deletePayment(Integer paymentID);
}
