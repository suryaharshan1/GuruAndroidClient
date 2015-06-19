package com.windroilla.guru.authenticator;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
public class RequestAccessTokenByPassword {
    public static final String grant_type = "password";
    public final String client_id = "";
    public final String client_secret = "";
    public final String username;
    public final String password;

    public RequestAccessTokenByPassword(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
