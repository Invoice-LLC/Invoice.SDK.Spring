package su.invoice.spring.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

public abstract class InvoiceRestClient {
    private static final String url = "https://api.invoice.su/api/v2/";
    private String apiKey;
    private String login;

    protected InvoiceRestClient(String login, String apiKey) {
        this.apiKey = apiKey;
        this.login = login;
    }

    protected Object send(String method, Class<? extends Object> responseClass) throws IOException {
        String authKey64 = new String(Base64.encodeBase64((login + ":" + apiKey).getBytes()));
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url+method).openConnection();
        connection.setRequestProperty("content-type", "application/json");
        connection.setRequestProperty("Authorization", "Basic " + authKey64);
        connection.setRequestMethod("POST");

        connection.setDoOutput(true);
        connection.setDoInput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(getJson().getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }
        bufferedReader.close();

        return getObject(response.toString(), responseClass);
    }

    private String getJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private Object getObject(String json, Class<? extends Object> responseClass) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, responseClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            try {
                return responseClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
                return new Object();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Object();
        }
    }
}
