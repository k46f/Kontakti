package io.github.k46f.kontakti;


import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    private String googleId;
    private FirebaseDatabase fDatabase;

    public FirebaseManager(String gId){
        googleId = gId;
        fDatabase = FirebaseDatabase.getInstance();
    }
}
