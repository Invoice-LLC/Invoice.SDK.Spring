package su.invoice.spring.rest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Notification {
    public String notification_type;
    public String id;
    public Order order;
    public String status;
    public String status_description;
    public String ip;
    public String create_date;
    public String update_date;
    public String expire_date;
    public String signature;
    public Map<String, String> custom_parameters;
    public PaymentMethod payment_method;

    public boolean checkSignature(String apiKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            String signature = new String(digest.digest((id+status+apiKey).getBytes()));

            return this.signature.equals(signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
}
