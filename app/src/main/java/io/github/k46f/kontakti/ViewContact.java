package io.github.k46f.kontakti;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewContact extends AppCompatActivity {

    private TextView phoneView, adressView, emailView, facebookView, birthdayView, fullName;
    private ImageView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
    }

    public void editContact(View v) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, EditContact.class);
        startActivity(intent);
    }
}
