package com.calendate.calendate;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Hilay on 18-יוני-2017.
 */

public class User {

    String username;
    String email;
    FirebaseDatabase mDatabase;
    FirebaseUser user;

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
