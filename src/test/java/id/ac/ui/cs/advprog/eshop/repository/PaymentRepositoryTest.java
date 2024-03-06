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
    Map<String, String> paymentRecord;
    List<Payment> payments;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        paymentRecord = new HashMap<>();
        payments = new ArrayList<>();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        Map<String, String> paymentVoucher = new HashMap<>();
        paymentVoucher.put("voucherCode", "ESHOP12345678ABC");
        Payment payment1 = new Payment("id-1-payment", "VOUCHER_CODE", paymentVoucher);
        payments.add(payment1);
        Map<String, String> paymentBank = new HashMap<>();
        paymentBank.put("bankName", "Bank Adpro");
        paymentBank.put("referenceCode", "DEADBEEF");
        Payment payment2 = new Payment("id-2-payment", "BANK_TRANSFER", paymentBank);
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
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("SUCCESS", order.getStatus());

        paymentRepository.update(payment, "REJECTED");
        Payment findResult = paymentRepository.findById(payments.get(0).getId());
        Order findOrder = paymentRepository.getOrder(findResult.getId());
        assertEquals("REJECTED", findResult.getStatus());
        assertEquals("FAILED", findOrder.getStatus());
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
}
