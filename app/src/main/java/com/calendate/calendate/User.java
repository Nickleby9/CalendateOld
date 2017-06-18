package com.calendate.calendate;

/**
 * Created by Hilay on 18-יוני-2017.
 */

public class User {

    String username;
    String email;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
