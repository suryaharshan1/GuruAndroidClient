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
        if (cause.getKind().equals(RetrofitError.Kind.NETWORK)) {
            //TODO Handle network errors
        }

        if (r != null) {
            switch (r.getStatus()) {
                case 401:
                    Log.d(TAG, "Unauthorized Exception: " + ((GuruApiErrorResponse) cause.getBodyAs(GuruApiErrorResponse.class)).message);
                    break;
                case 403:
                    Log.d(TAG, "Forbidden Exception: " + ((GuruApiErrorResponse) cause.getBodyAs(GuruApiErrorResponse.class)).message);
                    break;
                case 404:
                    Log.d(TAG, "Not Found Exception: " + ((GuruApiErrorResponse) cause.getBodyAs(GuruApiErrorResponse.class)).message);
                    break;
                case 400:
                    Log.d(TAG, "Bad Request Exception: " + ((GuruApiErrorResponse) cause.getBodyAs(GuruApiErrorResponse.class)).message);
                    break;
                case 409:
                    Log.d(TAG, "Conflict Exception: " + ((GuruApiErrorResponse) cause.getBodyAs(GuruApiErrorResponse.class)).message);
                    break;
                case 500:
                    Log.d(TAG, "Internal Server Error Exception: " + ((GuruApiErrorResponse) cause.getBodyAs(GuruApiErrorResponse.class)).message);
                    break;
                case 503:
                    Log.d(TAG, "Service Unavailable Exception: " + ((GuruApiErrorResponse) cause.getBodyAs(GuruApiErrorResponse.class)).message);
                    break;
                case 504:
                    Log.d(TAG, "Gateway Timeout Exception: " + ((GuruApiErrorResponse) cause.getBodyAs(GuruApiErrorResponse.class)).message);
                    break;
            }
        }
        return cause;

    }
}
