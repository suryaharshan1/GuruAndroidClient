package com.windroilla.guru;

import com.windroilla.guru.authenticator.GuruAuthenticatorActivity;

/**
 * Created by Surya Harsha Nunnaguppala on 20/6/15.
 */
public interface GuruGraph {
    void inject(MainActivity mainActivity);

    void inject(HelperActivity helperActivity);

    void inject(GuruAuthenticatorActivity guruAuthenticatorActivity);
}
