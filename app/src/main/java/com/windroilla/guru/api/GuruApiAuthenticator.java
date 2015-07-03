package com.windroilla.guru.api;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.windroilla.guru.authenticator.AuthConstants;

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
        String credential;
        Log.d(TAG, "Request URL: " + response.request().url().getPath());
        if (response.request().url().getPath().startsWith("/guru/oauth2")) return null;

        Account[] accounts = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        if (accounts.length != 0) {
            String oldToken = accountManager.peekAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE);
            if (!TextUtils.isEmpty(oldToken)) {
                Log.d(TAG, "Invalidating auth token");
                accountManager.invalidateAuthToken(AuthConstants.ACCOUNT_TYPE, oldToken);
            }
            try {
                Log.d(TAG, "Calling accountManager.blockingGetAuthToken");
                String token = accountManager.blockingGetAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE, false);

                if (token == null) {
                    accountManager.removeAccount(accounts[0], null, null);
                }

                // Do not retry certain URLs
                //if (url.getPath().startsWith("/donotretry")) {
                //  return null;
                //} else if (token != null) {
                if (token != null && !response.request().header("Authorization").equals("Bearer " + token)) {
                    return response.request().newBuilder().header("Authorization", "Bearer " + token).build();
                }

            } catch (AuthenticatorException e) {
                Log.e(TAG, "Authenticator Exception");
                e.printStackTrace();
            } catch (OperationCanceledException e) {
                Log.e(TAG, "Operation Canceled Exception");
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        Log.d(TAG, "authenticateProxy");
        return null;
    }
}
