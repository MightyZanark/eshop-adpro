package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    
    @BeforeEach
    void setUp() {}

    @Test
    void testCreatePaymentInvalidMethod() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345678ABC");
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("id-payment-testing-123", "HALO", 
                paymentData);
        });
    }

    @Test
    void testValidVoucherCode() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345678ABC");
        Payment payment = new Payment("id-payment-testing-123", 
            PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testVoucherCodeNotSixteenCharLong() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345678");
        Payment payment = new Payment("id-payment-testing-123", 
            PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testVoucherCodeNotStartWithESHOP() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "RSHOP12345678ABC");
        Payment payment = new Payment("id-payment-testing-123", 
            PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testVoucherCodeNotContainsEightNumbers() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABCDEFG");
        Payment payment = new Payment("id-payment-testing-123", 
            PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testVoucherCodeInvalidData() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Bank Adpro");
        paymentData.put("referenceCode", "DEADBEEF");
        Payment payment = new Payment("id-payment-testing-123", 
            PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testValidBankTransfer() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Bank Adpro");
        paymentData.put("referenceCode", "DEADBEEF");
        Payment payment = new Payment("id-payment-testing-123", 
            PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testBankTransferEmptyNameOrRefCode() {
        Map<String, String> paymentDataEmptyCode = new HashMap<>();
        paymentDataEmptyCode.put("bankName", "Bank Adpro");
        paymentDataEmptyCode.put("referenceCode", "");
        Payment paymentEmptyCode = new Payment("id-payment-testing-123", 
            PaymentMethod.BANK_TRANSFER.getValue(), paymentDataEmptyCode);
        
        Map<String, String> paymentDataEmptyName = new HashMap<>();
        paymentDataEmptyName.put("bankName", "");
        paymentDataEmptyName.put("referenceCode", "DEADBEEF");
        Payment paymentEmptyName = new Payment("id-payment-testing-456", 
            PaymentMethod.BANK_TRANSFER.getValue(), paymentDataEmptyName);
        
        assertEquals(PaymentStatus.REJECTED.getValue(), paymentEmptyCode.getStatus());
        assertEquals(PaymentStatus.REJECTED.getValue(), paymentEmptyName.getStatus());
    }
    
    @Test
    void testBankTransferNullNameOrRefCode() {
        Map<String, String> paymentDataNullCode = new HashMap<>();
        paymentDataNullCode.put("bankName", "Bank Adpro");
        paymentDataNullCode.put("referenceCode", null);
        Payment paymentNullCode = new Payment("id-payment-testing-123", 
            PaymentMethod.BANK_TRANSFER.getValue(), paymentDataNullCode);
        
        Map<String, String> paymentDataNullName = new HashMap<>();
        paymentDataNullName.put("bankName", null);
        paymentDataNullName.put("referenceCode", "DEADBEEF");
        Payment paymentNullName = new Payment("id-payment-testing-456", 
            PaymentMethod.BANK_TRANSFER.getValue(), paymentDataNullName);
        
        assertEquals(PaymentStatus.REJECTED.getValue(), paymentNullCode.getStatus());
        assertEquals(PaymentStatus.REJECTED.getValue(), paymentNullName.getStatus());
    }

    @Test
    void testBankTransferInvalidData() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345678");
        Payment payment = new Payment("id-payment-testing-123", 
            PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }
}
