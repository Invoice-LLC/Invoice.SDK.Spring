package su.invoice.spring.database.entities;

import su.invoice.spring.rest.PaymentStatus;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
public class InvoicePayment {
    @Id
    public String id;
    public double amount;
    public PaymentStatus status;
    public double refundAmount;
    public String refundReason;

    @OneToMany
    private Set<InvoiceCustomParameters> customParameters = new HashSet<InvoiceCustomParameters>();

    public String getPaymentUrl() {
        return "https://pay.invoice.su/P"+id;
    }

    public String getQr() {
        return "https://qr.invoice.su/getQR?id=%22https://pay.invoice.su/P"+id+"%22";
    }

    public Set<InvoiceCustomParameters> getCustomParameters() {
        return customParameters;
    }
}
