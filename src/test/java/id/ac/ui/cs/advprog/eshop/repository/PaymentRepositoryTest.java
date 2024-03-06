package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    List<Payment> payments;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        payments = new ArrayList<>();

        Map<String, String> paymentVoucher = new HashMap<>();
        paymentVoucher.put("voucherCode", "ESHOP12345678ABC");
        Payment payment1 = new Payment("id-1-payment", PaymentMethod.VOUCHER_CODE.getValue(), paymentVoucher);
        payments.add(payment1);
        Map<String, String> paymentBank = new HashMap<>();
        paymentBank.put("bankName", "Bank Adpro");
        paymentBank.put("referenceCode", "DEADBEEF");
        Payment payment2 = new Payment("id-2-payment", PaymentMethod.BANK_TRANSFER.getValue(), paymentBank);
        payments.add(payment2);
    }

    @Test
    void testSaveCreate() {
        Order order = new Order("test-id", null, 1709729613L, "author");
        Payment payment = payments.get(0);
        Payment result = paymentRepository.save(order, payment);

        Payment findResult = paymentRepository.findById(payments.get(0).getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getStatus(), findResult.getStatus());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertSame(payment.getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testUpdateStatus() {
        Order order = new Order("test-id", null, 1709729613L, "author");
        Payment payment = payments.get(0);
        Payment result = paymentRepository.save(order, payment);
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());

        paymentRepository.update(payment, PaymentStatus.REJECTED.getValue());
        Payment findResult = paymentRepository.findById(payments.get(0).getId());
        Order findOrder = paymentRepository.getOrder(findResult.getId());
        assertEquals(PaymentStatus.REJECTED.getValue(), findResult.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), findOrder.getStatus());
    }

    @Test
    void testUpdateInvalidStatus() {
        Order order = new Order("test-id", null, 1709729613L, "author");
        Payment payment = payments.get(0);
        Payment result = paymentRepository.save(order, payment);

        assertThrows(IllegalArgumentException.class, () -> {
            paymentRepository.update(payment, "HEHE");
        });
    }

    @Test
    void testFindByIdIfFound() {
        Order order = new Order("test-id", null, 1709729613L, "author");
        for (Payment payment : payments) {
            paymentRepository.save(order, payment);
        }

        Payment findPayment = paymentRepository.findById(payments.get(0).getId());
        assertEquals(payments.get(0).getId(), findPayment.getId());
        assertEquals(payments.get(0).getMethod(), findPayment.getMethod());
        assertEquals(payments.get(0).getStatus(), findPayment.getStatus());
        assertSame(payments.get(0).getPaymentData(), findPayment.getPaymentData());
    }

    @Test
    void testFindByIdIfNotFound() {
        Order order = new Order("test-id", null, 1709729613L, "author");
        for (Payment payment : payments) {
            paymentRepository.save(order, payment);
        }

        Payment findPayment = paymentRepository.findById("hello world");
        assertNull(findPayment);
    }

    @Test
    void testFindAll() {
        Order order = new Order("test-id", null, 1709729613L, "author");
        for (Payment payment : payments) {
            paymentRepository.save(order, payment);
        }

        List<Payment> findPayments = paymentRepository.findAll();
        assertEquals(2, findPayments.size());
    }

    @Test
    void testFindOrderIfFound() {
        Order order = new Order("test-id", null, 1709729613L, "author");
        Payment payment = payments.get(0);
        paymentRepository.save(order, payment);
        
        Payment findResult = paymentRepository.findById(payments.get(0).getId());
        Order findOrder = paymentRepository.getOrder(findResult.getId());
        assertEquals(order.getId(), findOrder.getId());
        assertEquals(order.getStatus(), findOrder.getStatus());
        assertEquals(order.getAuthor(), findOrder.getAuthor());
        assertEquals(order.getOrderTime(), findOrder.getOrderTime());
        assertEquals(order.getProducts(), findOrder.getProducts());
    }

    @Test
    void testFindOrderIfNotFound() {
        Order order = new Order("test-id", null, 1709729613L, "author");
        Payment payment = payments.get(0);
        paymentRepository.save(order, payment);
        
        Payment findResult = paymentRepository.findById(payments.get(0).getId());
        Order findOrder = paymentRepository.getOrder(findResult.getId());
        assertNull(findOrder);
    }
}
