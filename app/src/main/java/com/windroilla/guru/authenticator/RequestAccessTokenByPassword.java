package com.windroilla.guru.authenticator;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
public class RequestAccessTokenByPassword {
    public final String grant_type = "password";
    public final String username;
    public final String password;
    public final String client_id;
    public final String client_secret;

    public RequestAccessTokenByPassword(String username, String password, String client_id, String client_secret) {
        this.username = username;
        this.password = password;
        this.client_id = client_id;
        this.client_secret = client_secret;
    }
}
