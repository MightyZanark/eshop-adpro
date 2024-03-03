package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

import lombok.Getter;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
    }
}
