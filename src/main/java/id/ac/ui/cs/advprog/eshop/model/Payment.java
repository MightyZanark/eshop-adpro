package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

import java.util.Map;

import lombok.Getter;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id;
        this.setMethod(method);
        this.paymentData = paymentData;
        this.validateData();
    }

    private void setMethod(String method) {
        if (PaymentMethod.contains(method)) {
            this.method = method;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void validateData() {
        boolean isValid = false;
        switch (PaymentMethod.valueOf(method)) {
            case PaymentMethod.VOUCHER_CODE:
                isValid = validateVoucherMethod();
                break;
            
            case PaymentMethod.BANK_TRANSFER:
                isValid = validateBankMethod();
                break;
        
            default:
                break;
        }

        if (isValid) {
            status = PaymentStatus.SUCCESS.getValue();
        } else {
            status = PaymentStatus.REJECTED.getValue();
        }
    }

    private boolean validateVoucherMethod() {
        String voucherCode = paymentData.get("voucherCode");
        if (voucherCode == null) {
            return false;
        }

        if (checkVoucherCode(voucherCode)) {
            return true;
        }
        
        return false;
    }

    private boolean validateBankMethod() {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        
        if (bankName == null || bankName.isEmpty() || referenceCode == null || referenceCode.isEmpty()) {
            return false;
        }
        
        return true;
    }

    private boolean checkVoucherCode(String voucherCode) {
        if (voucherCode.length() != 16) {
            return false;
        }

        if (!voucherCode.startsWith("ESHOP")) {
            return false;
        }

        String code = voucherCode.substring(5);
        int numericCharCount = 0;
        for (char character : code.toCharArray()) {
            if (Character.isDigit(character)) {
                numericCharCount++;
            }
        }

        if (numericCharCount != 8) {
            return false;
        }

        return true;
    }
}
