package com.windroilla.guru.api.responseclasses;

import java.util.List;

/**
 * Created by Surya Harsha Nunnaguppala on 4/7/15.
 */
public class Course {
    public final int id;
    public final String name;
    public final int amount;
    public final int strength;
    public final double rating;
    public final List<Review> reviews;
    public final SimpleInstitute institute;
    public final SimpleUserProfile instructor;
    public final CourseType coursetype;
    public final String strategy;
    public final int validity;

    public Course(int id, String name, int amount, int strength, double rating, List<Review> reviews, SimpleInstitute institute, SimpleUserProfile instructor, CourseType coursetype, String strategy, int validity) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.strength = strength;
        this.rating = rating;
        this.reviews = reviews;
        this.institute = institute;
        this.instructor = instructor;
        this.coursetype = coursetype;
        this.strategy = strategy;
        this.validity = validity;
    }

}
