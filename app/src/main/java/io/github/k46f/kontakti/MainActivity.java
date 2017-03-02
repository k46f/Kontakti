package io.github.k46f.kontakti;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addNew(View v) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, NewContact.class);
        startActivity(intent);
    }
}