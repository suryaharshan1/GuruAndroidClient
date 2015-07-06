package com.windroilla.guru.api.responseclasses;

/**
 * Created by Surya Harsha Nunnaguppala on 5/7/15.
 */
public class SimpleUserProfile {
    public final int id;
    public final String first_name;
    public final String last_name;

    public SimpleUserProfile(int id, String first_name, String last_name) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
    }
}
