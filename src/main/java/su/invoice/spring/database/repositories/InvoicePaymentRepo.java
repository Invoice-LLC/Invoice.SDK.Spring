package su.invoice.spring.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import su.invoice.spring.database.entities.InvoicePayment;

public interface InvoicePaymentRepo extends JpaRepository<InvoicePayment, String> {
    InvoicePayment findFirstById(String id);
}
