package io.github.k46f.kontakti;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView testTextView;
    public final static String CONTACT_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testTextView = (TextView) findViewById(R.id.testTextView);

        fillData();
    }

    private void fillData() {
        Context context = getApplicationContext();

        try {
            DataBaseManager dbm = new DataBaseManager(context);
            dbm.openDb();
            String result = dbm.read();
            dbm.closeDb();
            testTextView.setText(result);

        } catch (Exception e) {
            Toast kToast = Toast.makeText(context, e.toString(), Toast.LENGTH_LONG);
            kToast.show();
        }

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