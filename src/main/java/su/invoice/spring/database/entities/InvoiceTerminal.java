package su.invoice.spring.database.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InvoiceTerminal {
    @Id
    public String id;
}
