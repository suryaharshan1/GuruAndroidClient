package com.windroilla.guru.authenticator;

import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
public interface ApiService {
    @Headers({"Content-Type:application/json-patch+json"})
    @POST("/oauth2/token") AccessToken getAccessToken(@Body RequestAccessTokenByPassword body);

    @Headers({"Content-Type:application/json-patch+json"})
    @POST("/oauth2/token") AccessToken refreshAccessToken(@Body RequestAccessTokenByRefresh body);

    @Headers({"Content-Type:application/json-patch+json"})
    @POST("/oauth2/token") Observable<AccessToken> getAccessTokenObservable(@Body RequestAccessTokenByPassword body);
}
