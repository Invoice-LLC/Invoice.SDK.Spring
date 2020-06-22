package su.invoice.spring.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "su.invoice.module")
public class InvoiceModuleProperties {
    private String apiKey;
    private String login;
    private String defaultTerminalName = "Invoice Spring Integration";
    private String successUrl;
    private String failUrl;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getDefaultTerminalName() {
        return defaultTerminalName;
    }

    public void setDefaultTerminalName(String defaultTerminalName) {
        this.defaultTerminalName = defaultTerminalName;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getFailUrl() {
        return failUrl;
    }

    public void setFailUrl(String failUrl) {
        this.failUrl = failUrl;
    }
}
