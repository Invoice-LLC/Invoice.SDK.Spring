package su.invoice.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import su.invoice.spring.database.entities.InvoicePayment;
import su.invoice.spring.database.repositories.InvoicePaymentRepo;
import su.invoice.spring.rest.Notification;
import su.invoice.spring.rest.PaymentStatus;
import su.invoice.spring.starter.InvoiceModuleProperties;

@RestController
@RequestMapping("/invoice")
public class InvoiceNotificationController {
    @Autowired
    private InvoiceModuleProperties properties;
    @Autowired
    private InvoicePaymentRepo invoicePaymentRepo;

    @PostMapping("/notification")
    public void notification(@RequestBody Notification notification) {
        if(!notification.checkSignature(properties.getApiKey())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Wrong signature!");
        }

        InvoicePayment payment = invoicePaymentRepo.findFirstById(notification.id);
        if(payment == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        if(payment.amount > notification.order.amount) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }
        setStatus(payment, notification.status, notification.notification_type);
    }

    private void setStatus(InvoicePayment payment, String status, String operation) {
        switch (status) {
            case "successful":
                payment.status = PaymentStatus.successful;
                break;
            case "failed":
                payment.status = PaymentStatus.failed;
                break;
            case "closed":
                payment.status = PaymentStatus.closed;
                break;
            case "refunded" :
                payment.status = PaymentStatus.refunded;
        }

        invoicePaymentRepo.save(payment);
    }
}
