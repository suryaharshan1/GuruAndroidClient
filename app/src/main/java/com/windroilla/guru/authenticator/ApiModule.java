package com.windroilla.guru.authenticator;

import android.accounts.AccountManager;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
@Module
public final class ApiModule {
    public static final String PRODUCTION_API_URL = "http://192.168.43.45/guru/";
    private static final String CLIENT_ID = "testclient";
    private static final String CLIENT_SECRET = "testpass";
    public static AccountManager accountManager;
    @Provides
    @Singleton
    @Named("ClientId") public String provideClientId() {
        return CLIENT_ID;
    }

    @Provides @Singleton @Named("ClientSecret") String provideClientSecret() {
        return CLIENT_SECRET;
    }

    @Provides @Singleton
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(PRODUCTION_API_URL);
    }

    @Provides @Singleton
    Client provideClient(OkHttpClient client) {
        return new OkClient(client);
    }

    @Provides @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint, Client client, ApiHeaders headers, Gson gson) {
        return new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(endpoint)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(headers)
                .setErrorHandler(new RestErrorHandler())
                .build();
    }

    @Provides @Singleton ApiService provideApiService(RestAdapter restAdapter) {
        return restAdapter.create(ApiService.class);
    }

    @Provides
    @Singleton
    AccountManager provideAccountManager() {
        return accountManager = AccountManager.get(null);
    }
    //@Provides @Singleton ApiDatabase provideApiDatabase(ApiService service) {
    //    return new ApiDatabase(service);
    //}

}
