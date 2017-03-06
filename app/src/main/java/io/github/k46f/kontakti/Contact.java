package io.github.k46f.kontakti;

import android.database.Cursor;

import java.util.ArrayList;

public class Contact {
    private final static String NAME_FOR_CONTACT_NAME = "name";
    private final static String NAME_FOR_CONTACT_PHONE = "phone";
    private final static String NAME_FOR_CONTACT_ADDRESS = "address";
    private final static String NAME_FOR_CONTACT_EMAIL = "email";
    private final static String NAME_FOR_CONTACT_FACEBOOK = "facebook";
    private final static String NAME_FOR_CONTACT_BIRTHDAY = "birthday";
    private final static String NAME_FOR_CONTACT_ID = "contact_id";

    private String name;
    private String phone;
    private String address;
    private String email;
    private String facebook;
    private String birthday;
    private String contact_id;

    Contact(Cursor cursor){
        name = cursor.getString(cursor.getColumnIndex(NAME_FOR_CONTACT_NAME));
        phone = cursor.getString(cursor.getColumnIndex(NAME_FOR_CONTACT_PHONE));
        address = cursor.getString(cursor.getColumnIndex(NAME_FOR_CONTACT_ADDRESS));
        email = cursor.getString(cursor.getColumnIndex(NAME_FOR_CONTACT_EMAIL));
        facebook = cursor.getString(cursor.getColumnIndex(NAME_FOR_CONTACT_FACEBOOK));
        birthday = cursor.getString(cursor.getColumnIndex(NAME_FOR_CONTACT_BIRTHDAY));
        contact_id = cursor.getString(cursor.getColumnIndex(NAME_FOR_CONTACT_ID));
    }
}
