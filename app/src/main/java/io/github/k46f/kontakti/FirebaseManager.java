package io.github.k46f.kontakti;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    private final static String USERS_REFERENCE = "users";

    private String googleId;
    private FirebaseDatabase fDatabase;
    private DatabaseReference usersReference;

    public FirebaseManager(String gId){
        googleId = gId;
        fDatabase = FirebaseDatabase.getInstance();
        usersReference = fDatabase.getReference(USERS_REFERENCE);
    }
}
