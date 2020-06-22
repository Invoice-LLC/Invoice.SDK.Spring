package su.invoice.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;




@ComponentScans(@ComponentScan(basePackages = {"su.invoice.spring.starter", "su.invoice.spring.controllers"}))
@EntityScan({"su.invoice.spring.database.entities", "su.invoice.example.entities"})
@EnableJpaRepositories(basePackages = {"su.invoice.spring.database", "su.invoice.example.repos"})

@SpringBootApplication()
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
