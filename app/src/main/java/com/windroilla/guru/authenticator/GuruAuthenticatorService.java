package com.windroilla.guru.authenticator;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Surya Harsha Nunnaguppala on 18/6/15.
 */
public class GuruAuthenticatorService extends Service{

    private static GuruAuthenticator mAuthenticator = null;

    @Override
    public IBinder onBind(Intent intent) {
        return intent.getAction().equals(AccountManager.ACTION_AUTHENTICATOR_INTENT) ? getAuthenticator().getIBinder() : null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private GuruAuthenticator getAuthenticator() {
        if (mAuthenticator == null)
            mAuthenticator = new GuruAuthenticator(this);
        return mAuthenticator;
    }

}
