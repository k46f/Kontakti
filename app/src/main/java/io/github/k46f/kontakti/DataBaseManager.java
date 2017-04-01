package io.github.k46f.kontakti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.util.ArrayList;

class DatabaseManager extends SQLiteOpenHelper {

    private Context ctx;
    private DatabaseManager databaseManager;
    private SQLiteDatabase dataBase;

    private final static String NAME_FOR_CONTACT_NAME = "name";
    private final static String NAME_FOR_CONTACT_PHONE = "phone";
    private final static String NAME_FOR_CONTACT_ADDRESS = "address";
    private final static String NAME_FOR_CONTACT_EMAIL = "email";
    private final static String NAME_FOR_CONTACT_FACEBOOK = "facebook";
    private final static String NAME_FOR_CONTACT_BIRTHDAY = "birthday";
    private final static String NAME_FOR_CONTACT_LOCATION = "location";
    private final static String NAME_FOR_CONTACT_PHOTO = "photo";
    private final static String NAME_FOR_CONTACT_ID = "contact_id";
    private final static String TABLE_NAME = "contacts";
    private final static String CREATE_TABLE = "CREATE TABLE contacts" +
            "(contact_id Integer PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "phone TEXT," +
            "address TEXT," +
            "email TEXT," +
            "facebook TEXT," +
            "birthday TEXT," +
            "photo BLOB," +
            "location TEXT)";
    private final static String DROP_TABLE = "DROP TABLE IF EXIST db_kontakti";
    private final static String DATABASE_NAME = "db_kontakti";

    DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    void openDb() {
        databaseManager = new DatabaseManager(ctx);
        dataBase = databaseManager.getWritableDatabase();
    }

    void closeDb() {
        dataBase.close();
    }

    Long register(String kname, String kphone, String kaddress, String kemail,
                  String kfacebook, String kbirthday, byte[] kphoto, String klocation)
            throws Exception{

        ContentValues kValues = new ContentValues();
        kValues.put(NAME_FOR_CONTACT_NAME, kname);
        kValues.put(NAME_FOR_CONTACT_PHONE, kphone);
        kValues.put(NAME_FOR_CONTACT_ADDRESS, kaddress);
        kValues.put(NAME_FOR_CONTACT_EMAIL, kemail);
        kValues.put(NAME_FOR_CONTACT_FACEBOOK, kfacebook);
        kValues.put(NAME_FOR_CONTACT_BIRTHDAY, kbirthday);
        kValues.put(NAME_FOR_CONTACT_PHOTO, kphoto);
        kValues.put(NAME_FOR_CONTACT_LOCATION, klocation);

        return dataBase.insert(TABLE_NAME, null, kValues);
    }


    ArrayList<Contact> readAllContacts() throws Exception {

        String[] columns = new String[] {
                NAME_FOR_CONTACT_NAME,
                NAME_FOR_CONTACT_PHONE,
                NAME_FOR_CONTACT_ADDRESS,
                NAME_FOR_CONTACT_EMAIL,
                NAME_FOR_CONTACT_FACEBOOK,
                NAME_FOR_CONTACT_BIRTHDAY,
                NAME_FOR_CONTACT_ID,
                NAME_FOR_CONTACT_PHOTO,
                NAME_FOR_CONTACT_LOCATION
        };

        Cursor cursor = dataBase.query(TABLE_NAME, columns, null, null, null, null, null);

        ArrayList<Contact> contacts = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            Contact contact = new Contact(cursor);
            contacts.add(contact);

        }
        cursor.close();
        return contacts;
    }

    String getSingleField (String idContact, String field) {

        Cursor cursor = dataBase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE contact_id="+idContact+"", null);
        cursor.moveToFirst();
        int data = cursor.getColumnIndex(field);
        String finalData = cursor.getString(data);
        cursor.close();
        return finalData;
    }

    byte[] getContactPhoto(String idContact){
        Cursor cursor = dataBase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE contact_id="+idContact+"", null);
        cursor.moveToFirst();
        int data = cursor.getColumnIndex("photo");
        byte[] finaldata = cursor.getBlob(data);
        cursor.close();
        return finaldata;
    }

    int updateContact(String kname, String kphone, String kaddress, String kemail,
                      String kfacebook, String kbirthday, String kId, byte[] kphoto, String klocation)
            throws Exception{

        ContentValues kValues = new ContentValues();
        kValues.put(NAME_FOR_CONTACT_NAME, kname);
        kValues.put(NAME_FOR_CONTACT_PHONE, kphone);
        kValues.put(NAME_FOR_CONTACT_ADDRESS, kaddress);
        kValues.put(NAME_FOR_CONTACT_EMAIL, kemail);
        kValues.put(NAME_FOR_CONTACT_FACEBOOK, kfacebook);
        kValues.put(NAME_FOR_CONTACT_BIRTHDAY, kbirthday);
        kValues.put(NAME_FOR_CONTACT_PHOTO, kphoto);
        kValues.put(NAME_FOR_CONTACT_LOCATION, klocation);

        return dataBase.update(TABLE_NAME, kValues, "contact_id = "+kId, null);
    }

    public void deleteContact(String idContact) {
        dataBase.delete(TABLE_NAME, NAME_FOR_CONTACT_ID + "=" + idContact, null);
    }
}
