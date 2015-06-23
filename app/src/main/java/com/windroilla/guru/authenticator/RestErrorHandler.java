package com.windroilla.guru.authenticator;

import android.util.Log;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Surya Harsha Nunnaguppala on 19/6/15.
 */
public class RestErrorHandler implements ErrorHandler{
    private static final String TAG = RestErrorHandler.class.getSimpleName();
    @Override
    public Throwable handleError(RetrofitError cause) {
        Response r = cause.getResponse();
        /*if (r != null && r.getStatus() == 401) {
            //return new UnauthorizedException(cause);
        }*/
        if (r != null) {
            switch (r.getStatus()) {
                case 401:
                    Log.d(TAG, "Unauthorized Exception");
                    break;
                case 403:
                    Log.d(TAG, "Forbidden Exception");
                    break;
                case 404:
                    Log.d(TAG, "Not Found Exception");
                    break;
                case 400:
                    Log.d(TAG, "Bad Request Exception");
                    break;
                case 409:
                    Log.d(TAG, "Conflict Exception");
                    break;
                case 500:
                    Log.d(TAG, "Internal Server Error Exception");
                    break;
                case 503:
                    Log.d(TAG, "Service Unavailable Exception");
                    break;
                case 504:
                    Log.d(TAG, "Gateway Timeout Exception");
                    break;
            }
        }
        return cause;

    }
}
