package su.invoice.spring.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Payment extends InvoiceRestClient{
    public String id;
    public Order order;
    public Settings settings;
    public Map<String, String> custom_parameters;
    public List<Item> receipt;

    public Payment(String login, String apiKey) {
        super(login, apiKey);
    }

    public Response create() throws IOException {
        return (Response) send("CreatePayment", Response.class);
    }

    public Response get(String id) throws IOException {
        this.id = id;
        return (Response) send("GetPayment", Response.class);
    }

    public Response close(String id) throws IOException {
        this.id = id;
        return (Response) send("ClosePayment", Response.class);
    }

    public static class Response extends InvoiceServerResponse{
        public String id;
        public Order order;
        public String status;
        public String ip;
        public String status_description;
        public String create_date;
        public String update_date;
        public String expire_date;
        public PaymentMethod payment_method;
        public Map<String, String> custom_parameters;
        public String payment_url;
    }
}
