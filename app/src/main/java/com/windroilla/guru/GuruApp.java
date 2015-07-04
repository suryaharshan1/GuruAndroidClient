package com.windroilla.guru;

import android.app.Application;

import com.windroilla.guru.api.ApiModule;

/**
 * Created by Surya Harsha Nunnaguppala on 23/6/15.
 */
public class GuruApp extends Application {
    private static GuruApp sInstance;
    private GuruGraph guruGraph;

    public static GuruApp getsInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        guruGraph = DaggerGuruGraph.builder()
                .apiModule(new ApiModule(this))
                .build();
    }

    public GuruGraph graph() {
        return guruGraph;
    }

}
