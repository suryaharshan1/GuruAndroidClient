package com.windroilla.guru.api;

/**
 * Created by Surya Harsha Nunnaguppala on 4/7/15.
 */
public class Course {
    public final int id;
    public final String name;
    public final int amount;
    public final int institute_id;
    public final int instructor_id;
    public final int coursetype_id;
    public final String strategy;
    public final int validity;

    public Course(int id, String name, int amount, int institute_id, int instructor_id, int coursetype_id, String strategy, int validity) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.institute_id = institute_id;
        this.instructor_id = instructor_id;
        this.coursetype_id = coursetype_id;
        this.strategy = strategy;
        this.validity = validity;
    }
}
