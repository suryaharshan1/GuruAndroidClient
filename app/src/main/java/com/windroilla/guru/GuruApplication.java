package com.windroilla.guru;

import android.app.Application;

/**
 * Created by Surya Harsha Nunnaguppala on 20/6/15.
 */
public class GuruApplication extends Application {
    private static GuruGraph graph;
    private static GuruApplication instance;

    public static GuruGraph component() {
        return graph;
    }

    //TODO Complete implementation after GuruComponent
    public static void buildComponentAndInject() {
        //graph = GuruComponent.Initializer.init(instance);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        buildComponentAndInject();
    }
}
