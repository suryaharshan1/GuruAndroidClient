package com.windroilla.guru;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Surya Harsha Nunnaguppala on 18/6/15.
 */
public class GuruAuthenticatorService extends Service{

    private GuruAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new GuruAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
