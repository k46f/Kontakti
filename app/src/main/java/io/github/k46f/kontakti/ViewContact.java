package io.github.k46f.kontakti;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewContact extends AppCompatActivity {

    private TextView phoneView, addressView, emailView, facebookView, birthdayView, fullName;
    private ImageView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        phoneView = (TextView) findViewById(R.id.phoneView);
        addressView = (TextView) findViewById(R.id.addressView);
        emailView = (TextView) findViewById(R.id.emailView);
        facebookView = (TextView) findViewById(R.id.facebookView);
        birthdayView = (TextView) findViewById(R.id.birthdayView);
        fullName = (TextView) findViewById(R.id.fullname);
        photoView = (ImageView) findViewById(R.id.photoView);

        Intent intent = getIntent();
        String contactId = intent.getStringExtra(MainActivity.CONTACT_ID);
    }

    public void editContact(View v) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, EditContact.class);
        startActivity(intent);
    }
}
