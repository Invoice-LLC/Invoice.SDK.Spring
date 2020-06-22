package su.invoice.spring.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Refund extends InvoiceRestClient{
    public String id;
    public List<Item> receipt;
    public RefundInfo refund;

    public Refund(String login, String apiKey) {
        super(login, apiKey);
    }

    public Response create() throws IOException {
        return (Response) send("CreateRefund", Response.class);
    }

    public Response get(String id) throws IOException {
        this.id = id;
        return (Response) send("GetRefund", Response.class);
    }

    public static class RefundInfo {
        public double amount;
        public String currency = "RUB";
        public String reason;

        public RefundInfo(double amount) {
            this.amount = amount;
        }
    }

    public static class Response extends InvoiceServerResponse{
        public String id;
        public Order order;
        public RefundInfo refund;
        public String status;
        public String payment_id;
        public String create_date;
        public String update_date;
        public PaymentMethod paymentMethod;
        public Map<String, String> custom_parameters;
    }
}
