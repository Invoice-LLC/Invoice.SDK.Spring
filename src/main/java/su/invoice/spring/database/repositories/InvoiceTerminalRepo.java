package su.invoice.spring.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import su.invoice.spring.database.entities.InvoiceTerminal;

import java.util.List;

public interface InvoiceTerminalRepo extends JpaRepository<InvoiceTerminal, String> {
}
