package su.invoice.spring.database.entities;

import javax.persistence.*;

@Entity
public class InvoiceCustomParameters {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public int id;

    @ManyToOne
    public InvoicePayment payment;

    public String name;
    public String value;
}
