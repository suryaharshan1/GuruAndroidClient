package com.windroilla.guru.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.windroilla.guru.GuruApp;
import com.windroilla.guru.MainActivity;
import com.windroilla.guru.R;
import com.windroilla.guru.SignUpActivity;
import com.windroilla.guru.api.AccessToken;
import com.windroilla.guru.api.ApiService;
import com.windroilla.guru.api.RequestAccessTokenByPassword;

import javax.inject.Inject;
import javax.inject.Named;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Surya Harsha Nunnaguppala on 20/6/15.
 */
public class GuruAuthenticatorActivity extends AccountAuthenticatorActivity {
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    public final static String PARAM_USER_NAME = "USER_NAME";

    private final int REQ_SIGNUP = 1;

    private final String TAG = this.getClass().getSimpleName();
    @Inject
    @Named("ClientId")
    String clientId;
    @Inject
    @Named("ClientSecret")
    String clientSecret;
    @Inject
    AccountManager mAccountManager;
    @Inject
    ApiService apiService;
    private String mAuthTokenType;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GuruApp.getsInstance().graph().inject(this);
        setContentView(R.layout.activity_login);

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AuthConstants.AUTHTOKEN_TYPE;

        if (accountName != null) {
            ((TextView)findViewById(R.id.email)).setText(accountName);
        }

        findViewById(R.id.email_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        findViewById(R.id.email_sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Since there can only be one GuruAuthenticatorActivity, we call the sign up activity, get his results,
                // and return them in setAccountAuthenticatorResult(). See finishLogin().
                Intent signup = new Intent(getBaseContext(), SignUpActivity.class);
                startActivityForResult(signup, REQ_SIGNUP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishSignUpwithLogin(data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void finishSignUpwithLogin(Intent intent) {
        doLogin(intent.getStringExtra(PARAM_USER_NAME), intent.getStringExtra(PARAM_USER_PASS));
    }

    public void submit() {
        final String userName = ((TextView) findViewById(R.id.email)).getText().toString();
        final String userPass = ((TextView) findViewById(R.id.password)).getText().toString();

        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        /*new AsyncTask<String, Void, Intent>() {

            @Override
            protected Intent doInBackground(String... params) {

                Log.d("Guru", TAG + "> Started authenticating");

                String authtoken = null;
                Bundle data = new Bundle();
                try {
                    //authtoken = sServerAuthenticate.userSignIn(userName, userPass, mAuthTokenType);

                    data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(PARAM_USER_PASS, userPass);

                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                } else {
                    finishLogin(intent);
                }
            }
        }.execute();*/

        doLogin(userName, userPass);
    }

    private void finishLogin(Intent intent) {
        Log.d("Guru", TAG + "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d("Guru", TAG + "> finishLogin > addAccountExplicitly");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            Log.d("Guru", TAG + "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    private Account addOrFindAccount(String email, String password) {
        Account[] accounts = mAccountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        Account account = accounts.length != 0 ? accounts[0] :
                new Account(email, AuthConstants.ACCOUNT_TYPE);

        if (accounts.length == 0) {
            mAccountManager.addAccountExplicitly(account, password, null);
        } else {
            mAccountManager.setPassword(accounts[0], password);
        }
        return account;
    }

    private void finishAccountAdd(String accountName, String authToken, String password) {
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AuthConstants.ACCOUNT_TYPE);
        if (authToken != null)
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
        intent.putExtra(AccountManager.KEY_PASSWORD, password);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(Activity.RESULT_OK, intent);
        finish();

        // Go back to the main activity
        startActivity(new Intent(this, MainActivity.class));
    }

    private void doLogin(final String email, String password) {

        apiService.getAccessTokenObservable(new RequestAccessTokenByPassword(email, password, clientId, clientSecret))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(
                        new Action1<AccessToken>() {
                            @Override
                            public void call(AccessToken accessToken) {
                                Account account = addOrFindAccount(email, accessToken.getRefreshToken());
                                mAccountManager.setAuthToken(account, AuthConstants.AUTHTOKEN_TYPE, accessToken.getAccessToken());
                                finishAccountAdd(email, accessToken.getAccessToken(), accessToken.getRefreshToken());
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if (throwable.toString().equals("retrofit.RetrofitError: 401 Unauthorized")) {
                                    Log.e(TAG, "Wrong Password");
                                }
                            }
                        }
                );
    }

}
