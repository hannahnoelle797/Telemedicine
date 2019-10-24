package com.example.telemedicine.ui.logout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logout {

    public static final void logout(FirebaseAuth firebaseAuth) {
        if (firebaseAuth.equals(null)) {
            return;
        } else {
            firebaseAuth.getInstance().signOut();
        }
    }
}
