package com.windroilla.guru;

import com.windroilla.guru.authenticator.ApiHeaders;
import com.windroilla.guru.authenticator.ApiModule;
import com.windroilla.guru.authenticator.GuruAuthenticator;
import com.windroilla.guru.authenticator.GuruAuthenticatorActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Surya Harsha Nunnaguppala on 23/6/15.
 */
@Singleton
@Component(modules = {ApiModule.class})

public interface GuruGraph {

    void inject(HelperActivity activity);

    void inject(GuruAuthenticatorActivity activity);

    void inject(ApiHeaders apiHeaders);

    void inject(GuruAuthenticator guruAuthenticator);

    void inject(ProfileFragment profileFragment);
}
