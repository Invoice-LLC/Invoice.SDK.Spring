package su.invoice.spring.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import su.invoice.spring.database.entities.InvoiceCustomParameters;

import java.util.List;

public interface InvoiceCustomParametersRepo extends JpaRepository<InvoiceCustomParameters, Integer> {
    List<InvoiceCustomParameters> findAllByNameAndValue(String name, String value);
}
