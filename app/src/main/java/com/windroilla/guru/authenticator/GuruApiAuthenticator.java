package com.windroilla.guru.authenticator;

import android.accounts.AccountManager;
import android.app.Application;
import android.util.Log;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import javax.inject.Inject;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
public class GuruApiAuthenticator implements Authenticator{

    private static final String TAG = GuruApiAuthenticator.class.getSimpleName();

    AccountManager accountManager;
    Application application;

    @Inject
    public GuruApiAuthenticator(AccountManager accountManager, Application application) {
        this.accountManager = accountManager;
        this.application = application;
    }


    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
        Log.d(TAG, "Authenticating for response: "  + response);
        Log.d(TAG, "Challenges: " + response.challenges());
        return null;
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }
}
