package su.invoice.spring.rest;

import su.invoice.spring.exceptions.InvoiceClientException;

public abstract class InvoiceServerResponse {
    public String error;
    public String description;

    public InvoiceClientException.InvoiceStatus getErrorType() {
        return getErrorType(error);
    }

    public InvoiceClientException.InvoiceStatus getErrorType(String error) {
        switch (error) {
            case "0":
                return InvoiceClientException.InvoiceStatus.INCORRECT_EMAIL_OR_API_KEY;
            case "1":
                return InvoiceClientException.InvoiceStatus.INCORRECT_CURRENCY;
            case "2":
                return InvoiceClientException.InvoiceStatus.INCORRECT_AMOUNT;
            case "3":
                return InvoiceClientException.InvoiceStatus.WRONG_TERMINAL_ID;
            case "4":
                return InvoiceClientException.InvoiceStatus.INCORRECT_PAYMENT_METHOD;
            case "5":
            case "6":
                return InvoiceClientException.InvoiceStatus.INCORRECT_PAYMENT_ADDRESS;
            case "7":
                return InvoiceClientException.InvoiceStatus.INCORRECT_ORDER_ID;
            case "11":
                return InvoiceClientException.InvoiceStatus.WRONG_TERMINAL_ALIAS;
            default:
                return InvoiceClientException.InvoiceStatus.OTHER_ERROR;
        }
    }
}
