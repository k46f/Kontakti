package io.github.k46f.kontakti;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public final static String CONTACT_ID = ">>> Pass Contact Id";
    private final static String TABLE_NAME = "contacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DataBasemanager is a SQLiteOpenHelper class connecting to SQLite
        DatabaseManager dbm = new DatabaseManager(this);
        // Get access to the underlying writeable database
        SQLiteDatabase db = dbm.getWritableDatabase();
        // Query for items from the database and get a cursor back
        Cursor contactsFillCursor = db.rawQuery("SELECT rowid _id,* FROM contacts", null);

        // Find ListView to populate
        ListView kontakti_listView = (ListView) findViewById(R.id.kontakti_listView);
        // Setup cursor adapter using cursor from last step
        CursorAdapterManager contactsAdapter = new CursorAdapterManager(this, contactsFillCursor);
        // Attach cursor adapter to the ListView
        kontakti_listView.setAdapter(contactsAdapter);

    }

    public void addNew(View v) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, NewContact.class);
        startActivity(intent);
    }

    public void viewContact(View v) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ViewContact.class);
        intent.putExtra(CONTACT_ID, "1");
        startActivity(intent);
    }
}