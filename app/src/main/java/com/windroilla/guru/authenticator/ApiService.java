package com.windroilla.guru.authenticator;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
public interface ApiService {
    @POST("/oauth2/token") AccessToken getAccessToken(@Body RequestAccessTokenByPassword body);

    @POST("/oauth2/token") AccessToken refreshAccessToken(@Body RequestAccessTokenByRefresh body);

    @POST("/oauth2/token") Observable<AccessToken> getAccessTokenObservable(@Body RequestAccessTokenByPassword body);
}
