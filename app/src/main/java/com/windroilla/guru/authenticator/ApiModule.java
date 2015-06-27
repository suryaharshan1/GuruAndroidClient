package com.windroilla.guru.authenticator;

import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;

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
    public static final String PRODUCTION_API_URL = "http://192.168.56.1/guru/";
    private static final String CLIENT_ID = "testclient";
    private static final String CLIENT_SECRET = "testpass";
    private Context context;

    public ApiModule(Context context) {
        this.context = context;
    }

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

    @Provides
    @Singleton
    GuruApiAuthenticator provideGuruApiAuthenticator(AccountManager accountManager, Application application) {
        return new GuruApiAuthenticator(accountManager, application);
    }

    @Provides @Singleton
    OkHttpClient provideOkHttpClient(GuruApiAuthenticator guruApiAuthenticator) {
        return (new OkHttpClient()).setAuthenticator(guruApiAuthenticator);
    }

    @Provides
    @Singleton
    Client provideClient(OkHttpClient client) {
        return new OkClient(client);
    }


    @Provides @Singleton
    ApiHeaders provideApiHeaders(AccountManager accountManager) {
        return new ApiHeaders(accountManager);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
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
    Application provideApplication() {
        return (Application) context;
    }

    @Provides
    @Singleton
    AccountManager provideAccountManager() {
        return AccountManager.get(context);
    }
    //@Provides @Singleton ApiDatabase provideApiDatabase(ApiService service) {
    //    return new ApiDatabase(service);
    //}

}
