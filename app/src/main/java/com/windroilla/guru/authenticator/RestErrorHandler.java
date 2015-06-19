package com.windroilla.guru.authenticator;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
public class RestErrorHandler implements ErrorHandler{
    @Override
    public Throwable handleError(RetrofitError cause) {
        return null;
    }
}
