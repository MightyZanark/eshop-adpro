package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PaymentRepository {
    private List<Payment> paymentData;
    private Map<String, Order> paymentOrder;

    public Payment save(Order order, Payment payment) {
        payments.add(payment);
        paymentOrder.put(payment.getId(), order);
        return payment;
    }

    public void update(Payment payment, String status) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException();
        }
        
        payment.setStatus(status);
    }

    public Payment findById(String paymentId) {
        for (Payment savedPayment : paymentData) {
            if (savedPayment.getId().equals(paymentId)) {
                return savedPayment;
            }
        }
        return null;
    }

    public List<Payment> findAll() {
        List<Payment> allPayments = new ArrayList<>();
        for (Payment payment : paymentData) {
            allPayments.add(payment);
        }

        return allPayments;
    }
}