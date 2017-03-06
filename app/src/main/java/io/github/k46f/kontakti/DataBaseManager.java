package io.github.k46f.kontakti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DataBaseManager extends SQLiteOpenHelper {

    private Context ctx;
    private DataBaseManager dataBaseManager;
    private SQLiteDatabase dataBase;

    DataBaseManager(Context context) {
        super(context, "db_kontakti", null, 1);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contacts (contact_id Integer PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL, phone TEXT, address TEXT, email TEXT, facebook TEXT," +
                "birthday TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST db_kontakti");
        onCreate(db);
    }

    void openDb() {
        dataBaseManager = new DataBaseManager(ctx);
        dataBase = dataBaseManager.getWritableDatabase();
    }

    void closeDb() {
        dataBase.close();
    }

    Long register(String kname, String kphone, String kaddress, String kemail,
                           String kfacebook, String kbirthday) throws Exception{
        ContentValues kValues = new ContentValues();
        kValues.put("name", kname);
        kValues.put("phone", kphone);
        kValues.put("address", kaddress);
        kValues.put("email", kemail);
        kValues.put("facebook", kfacebook);
        kValues.put("birthday", kbirthday);

        return dataBase.insert("contacts", null, kValues);
    }



    String read() throws Exception {
        String data = "";
        String[] columns = new String[] {
                "name",
                "phone",
                "address",
                "email",
                "facebook",
                "birthday",
                "contact_id"
        };

        Cursor cursor = dataBase.query("contacts", columns, null, null, null, null, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            data += cursor.getString(cursor.getColumnIndex("name")) +
                    cursor.getString(cursor.getColumnIndex("phone")) +
                    cursor.getString(cursor.getColumnIndex("address")) +
                    cursor.getString(cursor.getColumnIndex("email")) +
                    cursor.getString(cursor.getColumnIndex("facebook")) +
                    cursor.getString(cursor.getColumnIndex("birthday"));
        }
        cursor.close();
        return data;
    }
}
