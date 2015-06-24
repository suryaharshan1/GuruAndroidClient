package com.windroilla.guru.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.text.TextUtils;

import javax.inject.Inject;

import retrofit.RequestInterceptor;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
public final class ApiHeaders implements RequestInterceptor{

    @Inject
    AccountManager accountManager;

    @Override
    public void intercept(RequestFacade request) {
        Account[] accounts = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        if (accounts.length != 0) {
            String token = accountManager.peekAuthToken(accounts[0],AuthConstants.AUTHTOKEN_TYPE);
            if (!TextUtils.isEmpty(token)) {
                request.addHeader("Authorization", "Bearer " + token);
            }
        }
        request.addHeader("Accept","application/json");

    }
}
