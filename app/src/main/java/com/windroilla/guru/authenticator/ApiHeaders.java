package com.windroilla.guru.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.text.TextUtils;

import javax.inject.Inject;

import retrofit.RequestInterceptor;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
public class ApiHeaders implements RequestInterceptor{

    private Application application;

    @Inject
    public ApiHeaders(Application application) {
        this.application = application;
    }

    @Override
    public void intercept(RequestFacade request) {
        AccountManager accountManager = AccountManager.get(application);
        Account[] accounts = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        if (accounts.length != 0) {
            String token = accountManager.peekAuthToken(accounts[0],AuthConstants.AUTHTOKEN_TYPE);
            if(TextUtils.isEmpty(token)){
                request.addHeader("Authorization", "Bearer " + token);
            }
        }
        request.addHeader("Accept","application/json");

    }
}
