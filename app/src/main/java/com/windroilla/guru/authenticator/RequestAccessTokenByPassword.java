package com.windroilla.guru.authenticator;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
public class RequestAccessTokenByPassword {
    public static final String grant_type = "password";
    public final String username;
    public final String password;
    @Inject @Named("ClientId") String client_id;
    @Inject @Named("ClientSecret") String client_secret;

    public RequestAccessTokenByPassword(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
