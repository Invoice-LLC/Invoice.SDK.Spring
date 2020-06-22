package su.invoice.spring.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import su.invoice.spring.InvoicePaymentManager;
import su.invoice.spring.database.repositories.InvoiceCustomParametersRepo;

@Configuration
@EnableConfigurationProperties(InvoiceModuleProperties.class)
@ConditionalOnProperty(prefix = "su.invoice.module", name = {"apiKey", "login", "defaultTerminalName", "successUrl", "failUrl"})
public class InvoiceModuleAutoConfiguration {

    @Bean
    InvoicePaymentManager invoicePaymentManager() {
        return new InvoicePaymentManager();
    }
}
