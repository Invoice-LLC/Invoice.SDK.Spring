package su.invoice.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import su.invoice.spring.database.entities.InvoiceCustomParameters;
import su.invoice.spring.database.entities.InvoicePayment;
import su.invoice.spring.database.entities.InvoiceTerminal;
import su.invoice.spring.database.repositories.InvoiceCustomParametersRepo;
import su.invoice.spring.database.repositories.InvoicePaymentRepo;
import su.invoice.spring.database.repositories.InvoiceTerminalRepo;
import su.invoice.spring.exceptions.InvoiceClientException;
import su.invoice.spring.rest.*;
import su.invoice.spring.starter.InvoiceModuleProperties;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InvoicePaymentManager {
    @Autowired
    private InvoiceModuleProperties properties;
    @Autowired
    private InvoiceCustomParametersRepo customParametersRepo;
    @Autowired
    private InvoicePaymentRepo paymentRepo;
    @Autowired
    private InvoiceTerminalRepo terminalRepo;

    public InvoicePayment createPayment(double amount, @Nullable String description , @Nullable List<Item> receipt, @NotNull Map<String, String> customParameters) {
        InvoicePayment payment = new InvoicePayment();
        Payment restPayment = new Payment(properties.getLogin(), properties.getApiKey());

        Order order = new Order();
        order.amount = amount;
        order.description = description;

        Settings settings = new Settings();
        settings.fail_url = properties.getFailUrl();
        settings.success_url = properties.getSuccessUrl();
        settings.terminal_id = getCurrentTerminal().id;
        if(settings.terminal_id == null) {
            createTerminal();
            settings.terminal_id = getCurrentTerminal().id;
        }

        restPayment.custom_parameters = customParameters;
        restPayment.order = order;
        restPayment.receipt = receipt;
        restPayment.settings = settings;

        try {
            Payment.Response response = restPayment.create();
            //System.out.println(new ObjectMapper().writeValueAsString(response));
            if(response == null) throw new InvoiceClientException(InvoiceClientException.InvoiceStatus.OTHER_ERROR);
            if(response.error != null) throw new InvoiceClientException(response.getErrorType(response.error));

            payment.status = PaymentStatus.processed;
            payment.amount = amount;
            payment.id = response.id;

            paymentRepo.save(payment);
            saveCustomParameters(customParameters, payment);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvoiceClientException(InvoiceClientException.InvoiceStatus.CONNECTION_ERROR);
        }

        return payment;
    }

    public InvoicePayment createPayment(double amount) {
        return createPayment(amount, null, new ArrayList<>(), new HashMap<>());
    }

    public void cancelPayment(String id) {
        InvoicePayment payment = paymentRepo.findFirstById(id);
        if(payment == null) return;
        Payment restPayment = new Payment(properties.getLogin(), properties.getApiKey());
        try {
            restPayment.close(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        payment.status = PaymentStatus.closed;
        paymentRepo.save(payment);
    }

    public void cancelPayment(InvoicePayment payment) {
        cancelPayment(payment.id);
    }

    public void createRefund(String id, double amount, @Nullable  String reason, @Nullable List<Item> receipt) {
        InvoicePayment payment = paymentRepo.findFirstById(id);
        if(payment == null) return;

        Refund refund = new Refund(properties.getLogin(), properties.getApiKey());
        refund.receipt = receipt;
        Refund.RefundInfo refundInfo = new Refund.RefundInfo(amount);
        refundInfo.reason = reason;

        payment.refundAmount = amount;
        payment.refundReason = reason;

        try {
            Refund.Response response = refund.create();

            if(response == null) throw new InvoiceClientException(InvoiceClientException.InvoiceStatus.OTHER_ERROR);
            if(response.error != null) throw new InvoiceClientException(response.getErrorType());
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvoiceClientException(InvoiceClientException.InvoiceStatus.CONNECTION_ERROR);
        }

        paymentRepo.save(payment);
    }

    public String createTerminal() {
        Terminal terminal = new Terminal(properties.getLogin(), properties.getApiKey());
        terminal.name = properties.getDefaultTerminalName();
        terminal.type = Terminal.TerminalType.dynamical;

        try {
            System.out.println(new ObjectMapper().writeValueAsString(terminal));
            Terminal.Response response = terminal.create();
            if(response == null || response.error != null) {
                System.out.println(new ObjectMapper().writeValueAsString(response));
                assert response != null;
                throw new InvoiceClientException(response.getErrorType());
            }

            InvoiceTerminal invoiceTerminal = new InvoiceTerminal();
            invoiceTerminal.id = response.id;
            terminalRepo.save(invoiceTerminal);

            return response.id;
        } catch (IOException e) {
            throw new InvoiceClientException(HttpStatus.BAD_REQUEST, InvoiceClientException.InvoiceStatus.CONNECTION_ERROR);
        }
    }

    public @Nullable InvoiceTerminal getCurrentTerminal() {
        List<InvoiceTerminal> savedTerminals = terminalRepo.findAll();
        if(savedTerminals.size() <= 0) return new InvoiceTerminal();
        return savedTerminals.get(0);
    }

    public boolean terminalIsAvailable() {
        List<InvoiceTerminal> savedTerminals = terminalRepo.findAll();
        if(savedTerminals.size() <= 0) return false;
        InvoiceTerminal terminal = savedTerminals.get(0);

        Terminal terminalRequest = new Terminal(properties.getLogin(), properties.getApiKey());
        try {
            Terminal.Response response = terminalRequest.get(terminal.id);
            if(response == null || response.error != null) {
                terminalRepo.delete(terminal);
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<InvoicePayment> getPaymentsByCustomParameters(String key, String value) {
        List<InvoiceCustomParameters> parameters = customParametersRepo.findAllByNameAndValue(key, value);
        List<InvoicePayment> payments = new ArrayList<>();

        for(InvoiceCustomParameters parameter : parameters) {
            payments.add(parameter.payment);
        }

        return payments;
    }

    private void saveCustomParameters(Map<String, String> parameters, InvoicePayment payment) {
        for(Map.Entry<String, String> entry : parameters.entrySet()) {
            InvoiceCustomParameters parameter = new InvoiceCustomParameters();
            parameter.name = entry.getKey();
            parameter.value = entry.getValue();
            parameter.payment = payment;

            customParametersRepo.save(parameter);
        }
    }
}
