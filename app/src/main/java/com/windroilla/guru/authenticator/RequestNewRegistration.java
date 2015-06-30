package com.windroilla.guru.authenticator;

/**
 * Created by Surya Harsha Nunnaguppala on 30/6/15.
 */
public class RequestNewRegistration {
    public final String email;
    public final String password_hash;
    public final String mobile_number;
    public final String ambition;
    public final String first_name;
    public final String last_name;
    public final String father_name;
    public final String address;

    public RequestNewRegistration(String email, String password_hash, String mobile_number, String ambition, String first_name, String last_name, String father_name, String address) {
        this.email = email;
        this.password_hash = password_hash;
        this.mobile_number = mobile_number;
        this.ambition = ambition;
        this.first_name = first_name;
        this.last_name = last_name;
        this.father_name = father_name;
        this.address = address;
    }
}
