package com.windroilla.guru.authenticator;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
public class RequestAccessTokenByRefresh {
    public final String grant_type = "refresh_token";
    public final String refresh_token;
    public final String client_id;
    public final String client_secret;
    public final String scope;

    public RequestAccessTokenByRefresh(String refresh_token, String client_id, String client_secret, String scope) {
        this.refresh_token = refresh_token;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.scope = scope;
    }
}
