package io.github.k46f.kontakti;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public final static String CONTACT_ID = ">>> Pass Contact Id";
    private final static String NAME_FOR_CONTACT_ID = "contact_id";

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

        kontakti_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Context context = getApplicationContext();

                DatabaseManager dbm = new DatabaseManager(context);
                SQLiteDatabase db = dbm.getWritableDatabase();
                Cursor onClickListView = db.rawQuery("SELECT contact_id FROM contacts", null);
                onClickListView.moveToPosition(position);
                int data = onClickListView.getColumnIndexOrThrow(NAME_FOR_CONTACT_ID);
                String contact_id = onClickListView.getString(data);
                onClickListView.close();

                Intent intent = new Intent(context, ViewContact.class);
                intent.putExtra(CONTACT_ID, contact_id);
                startActivity(intent);
            }
        });

        kontakti_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Context context = getApplicationContext();

                DatabaseManager dbm = new DatabaseManager(context);
                SQLiteDatabase db = dbm.getWritableDatabase();
                Cursor onClickListView = db.rawQuery("SELECT contact_id FROM contacts", null);
                onClickListView.moveToPosition(position);
                int data = onClickListView.getColumnIndexOrThrow(NAME_FOR_CONTACT_ID);
                String contact_id = onClickListView.getString(data);
                onClickListView.close();

                MainActivityDeleteBar deleteBar = new MainActivityDeleteBar();
                deleteBar.onLongClick(view, contact_id);

                return true;
            }
        });
    }

    public void addNew(MenuItem mi) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, NewContact.class);
        startActivity(intent);
    }

    public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }
}