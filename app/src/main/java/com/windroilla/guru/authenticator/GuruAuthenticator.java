package com.windroilla.guru.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.windroilla.guru.GuruApp;
import com.windroilla.guru.api.AccessToken;
import com.windroilla.guru.api.ApiService;
import com.windroilla.guru.api.RequestAccessTokenByRefresh;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Surya Harsha Nunnaguppala on 18/6/15.
 */
public class GuruAuthenticator extends AbstractAccountAuthenticator{

    private static final String TAG = GuruAuthenticator.class.getSimpleName();
    private final Context context;
    @Inject @Named("ClientId") String clientId;
    @Inject @Named("ClientSecret") String clientSecret;
    @Inject
    ApiService apiService;

    public GuruAuthenticator (Context context) {
        super(context);
        this.context = context;
        GuruApp.getsInstance().graph().inject(this);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.d(TAG,"addAccount()");
        final Intent intent = new Intent(context,GuruAuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(GuruAuthenticatorActivity.ARG_AUTH_TYPE,authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.d(TAG,"getAuthToken() account="+account.name+ " type="+account.type);

        final Bundle bundle = new Bundle();
        // If the caller requested an authToken type we don't support, then
        // return an error
        if (!authTokenType.equals(AuthConstants.AUTHTOKEN_TYPE)) {
            Log.d(TAG,"invalid authTokenType" + authTokenType);
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return bundle;
        }

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken
        final AccountManager accountManager = AccountManager.get(context);
        // Password is storing the refresh token
        final String password = accountManager.getPassword(account);
        if (password != null) {
            Log.i(TAG,"Trying to refresh access token");
            try {
                AccessToken accessToken = apiService.refreshAccessToken(new RequestAccessTokenByRefresh(password, clientId, clientSecret, AuthConstants.SCOPE_DEFAULT));
                if (accessToken!=null && !TextUtils.isEmpty(accessToken.getAccessToken())) {
                    bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                    bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                    bundle.putString(AccountManager.KEY_AUTHTOKEN, accessToken.getAccessToken());
                    accountManager.setPassword(account, accessToken.getRefreshToken());
                    return bundle;
                }
            } catch (Exception e) {
                Log.e(TAG,"Failed refreshing token.");
            }
        }

        // Otherwise... start the login intent
        Log.i(TAG,"Starting login activity");
        final Intent intent = new Intent(context, GuruAuthenticatorActivity.class);
        intent.putExtra(GuruAuthenticatorActivity.ARG_ACCOUNT_NAME, account.name);
        intent.putExtra(GuruAuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return authTokenType.equals(AuthConstants.AUTHTOKEN_TYPE) ? authTokenType : null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;

    }
}
