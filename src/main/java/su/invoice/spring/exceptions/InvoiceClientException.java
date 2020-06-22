package su.invoice.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class InvoiceClientException extends HttpClientErrorException {
    public enum InvoiceStatus {
        INCORRECT_EMAIL_OR_API_KEY,
        INCORRECT_CURRENCY,
        INCORRECT_AMOUNT,
        INCORRECT_PAYMENT_METHOD,
        INCORRECT_PAYMENT_ADDRESS,
        INCORRECT_ORDER_ID,
        CONNECTION_ERROR,
        OTHER_ERROR,
        WRONG_TERMINAL_ID,
        WRONG_TERMINAL_ALIAS
    }

    public InvoiceClientException(HttpStatus statusCode, InvoiceStatus status) {
        super(statusCode, status.name());
    }

    public InvoiceClientException(InvoiceStatus status) {super(HttpStatus.BAD_REQUEST, status.name());}

    public InvoiceClientException(String error) {

        super(HttpStatus.BAD_REQUEST);
    }
}
