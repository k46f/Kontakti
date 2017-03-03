package io.github.k46f.kontakti;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by k46f on 3/03/2017.
 */

public class DataBaseManager extends SQLiteOpenHelper {

    private Context ctx;
    private DataBaseManager dataBaseManager;
    private SQLiteDatabase dataBase;

    public DataBaseManager(Context context) {
        super(context, "db_kontakti", null, 1);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contacts (contact_id Integer PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST db_kontakti");
        onCreate(db);
    }

    public void openDb() {
        dataBaseManager = new DataBaseManager(ctx);
        dataBase = dataBaseManager.getWritableDatabase();
    }

    public void closeDb() {
        dataBase.close();
    }
}
