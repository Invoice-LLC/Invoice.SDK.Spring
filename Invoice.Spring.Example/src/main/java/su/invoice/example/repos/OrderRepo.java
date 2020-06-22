package su.invoice.example.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import su.invoice.example.entities.Order;

public interface OrderRepo extends JpaRepository<Order, Integer> {
    Order findFirstById(int id);
}
