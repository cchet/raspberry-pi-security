package at.rpisec.client.rest;

import java.io.Serializable;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public final class OAuthCredentials implements Serializable {
    private String userName;
    private String password;
    private String token;
    private String clientId;
    private String clientSecret;

    public OAuthCredentials() {
    }

    public OAuthCredentials(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
