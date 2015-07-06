package com.windroilla.guru.api.responseclasses;

/**
 * Created by Surya Harsha Nunnaguppala on 5/7/15.
 */
public class Review {
    public final double rating;
    public final String review;
    public final String first_time;
    public final String last_time;
    public final String rating_time;

    public Review(double rating, String review, String first_time, String last_time, String rating_time) {
        this.rating = rating;
        this.review = review;
        this.first_time = first_time;
        this.last_time = last_time;
        this.rating_time = rating_time;
    }
}
