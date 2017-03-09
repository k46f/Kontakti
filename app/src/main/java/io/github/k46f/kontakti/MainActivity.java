package io.github.k46f.kontakti;

import android.content.Context;
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

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.kontakti_listView);

        String[] myStringArray = new String[] {"hola", "hola2"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, myStringArray);

        listView.setAdapter(adapter);

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