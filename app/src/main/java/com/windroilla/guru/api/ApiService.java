package com.windroilla.guru.api;

import java.util.HashMap;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PATCH;
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

    @Headers({"Content-Type:application/json-patch+json"})
    @GET("/v1/users/profile")
    Observable<UserProfile> getUserProfileObservable();

    @Headers({"Content-Type:application/json-patch+json"})
    @POST("/v1/users/register")
    Observable<UserProfile> registerNewUser(@Body RequestNewRegistration body);

    @Headers({"Content-Type:application/json-patch+json"})
    @PATCH("/v1/users/update")
    Observable<UserProfile> updateUserProfile(@Body HashMap<String, String> body);
}
