package su.invoice.example.entities;

import su.invoice.spring.database.entities.InvoicePayment;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public int id;
    public String mail;
    public double amount;
}
