package su.invoice.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Terminal extends InvoiceRestClient {

    public enum TerminalType {
        statical, dynamical
    }

    public String id;
    public String name;
    public String description;
    public TerminalType type;

    public Terminal(String login, String apiKey) {
        super(login, apiKey);
    }

    public Response create() throws IOException {
        return (Response) send("CreateTerminal", Response.class);
    }

    public Response get(String id) throws IOException {
        this.id = id;
        return (Response) send("GetTerminal", Response.class);
    }

    public static class Response extends InvoiceServerResponse{
        public String id;
        public String link;
        public String name;
        public String alias;

        public TerminalType type;
        public double defaultPrice;

    }
}
