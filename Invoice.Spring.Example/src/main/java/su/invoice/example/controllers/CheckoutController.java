package su.invoice.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import su.invoice.example.entities.Order;
import su.invoice.example.forms.CreateOrderForm;
import su.invoice.example.repos.OrderRepo;
import su.invoice.spring.InvoicePaymentManager;
import su.invoice.spring.database.entities.InvoicePayment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
public class CheckoutController {
    @Autowired
    OrderRepo orderRepo;

    @Autowired
    InvoicePaymentManager paymentManager;

    @GetMapping("/order/{id}/payment")
    public List<InvoicePayment> getPayments(@PathVariable Integer id) {
        Order order = orderRepo.findFirstById(id);
        if(order == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        return paymentManager.getPaymentsByCustomParameters("order", String.valueOf(id));
    }

    @PostMapping("/order/{id}/payment")
    public Map<String, Object> createPayment(@PathVariable Integer id) {
        Order order = orderRepo.findFirstById(id);

        if(order == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        Map<String, String> customParameters = new HashMap<>();
        customParameters.put("mail", order.mail);
        customParameters.put("order", String.valueOf(id));

        InvoicePayment payment = paymentManager.createPayment(order.amount, "Тестовый заказ", new ArrayList<>(), customParameters);

        Map<String,Object> paymentInfo = new HashMap<>();
        paymentInfo.put("payment_url", payment.getPaymentUrl());
        paymentInfo.put("qr_code", payment.getQr());
        paymentInfo.put("parameters", customParameters);

        return paymentInfo;
    }

    @PostMapping("/order")
    public String createOrder(@RequestBody CreateOrderForm form) {
        if(form.amount <= 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        Order order = new Order();
        order.amount = form.amount;
        order.mail = form.mail;

        orderRepo.save(order);
        return String.valueOf(order.id);
    }
}
