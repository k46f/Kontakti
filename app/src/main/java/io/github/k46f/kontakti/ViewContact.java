package io.github.k46f.kontakti;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewContact extends AppCompatActivity {

    public final static String CONTACT_ID = ">>> Pass Contact Id";

    private TextView fullName;
    private EditText phoneView, addressView, emailView, facebookView, birthdayView, locationView;
    private ImageView photoView;

    private final static String NAME_FOR_CONTACT_NAME = "name";
    private final static String NAME_FOR_CONTACT_PHONE = "phone";
    private final static String NAME_FOR_CONTACT_ADDRESS = "address";
    private final static String NAME_FOR_CONTACT_EMAIL = "email";
    private final static String NAME_FOR_CONTACT_FACEBOOK = "facebook";
    private final static String NAME_FOR_CONTACT_BIRTHDAY = "birthday";
    private final static String NAME_FOR_CONTACT_LOCATION = "location";
    private String contactId;
    private final static String EDIT_SUCCESS = "Contact edited success!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        // Show back button in action bar, boolena method to go back is at the end of activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Context context = getApplicationContext();

        phoneView = (EditText) findViewById(R.id.phoneView);
        addressView = (EditText) findViewById(R.id.addressView);
        emailView = (EditText) findViewById(R.id.emailView);
        facebookView = (EditText) findViewById(R.id.facebookView);
        birthdayView = (EditText) findViewById(R.id.birthdayView);
        locationView = (EditText) findViewById(R.id.locationView);
        fullName = (TextView) findViewById(R.id.fullname);
        photoView = (ImageView) findViewById(R.id.photoView);

        DatabaseManager dbm = new DatabaseManager(context);
        dbm.openDb();

        Intent intent = getIntent();

        String contactIdMain = intent.getStringExtra(MainActivity.CONTACT_ID);
        String contactIdEdit = intent.getStringExtra(EditContact.RETURN_EDIT);

        if (contactIdMain != null) {
            contactId = contactIdMain;
        } else {
            contactId = contactIdEdit;

            Toast ktoast = Toast.makeText(this, EDIT_SUCCESS, Toast.LENGTH_LONG);
            ktoast.show();
        }

        fullName.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_NAME));
        addressView.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_ADDRESS));
        emailView.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_EMAIL));
        facebookView.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_FACEBOOK));
        birthdayView.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_BIRTHDAY));
        phoneView.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_PHONE));
        locationView.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_LOCATION));

        byte[] photo = dbm.getContactPhoto(contactId);
        Bitmap contactPhoto = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        photoView.setImageBitmap(contactPhoto);

        dbm.closeDb();
    }

    public void phoneClick(View view){




        
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneView.getText().toString()));
        if (callIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(callIntent);
        }
    }

    public void editContact(MenuItem mi) {
        Intent firstIntent = getIntent();
        String contactId = firstIntent.getStringExtra(MainActivity.CONTACT_ID);

        Context context = getApplicationContext();
        Intent intent = new Intent(context, EditContact.class);
        intent.putExtra(CONTACT_ID, contactId);
        startActivity(intent);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Method to go back
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_contact, menu);
        return true;
    }
}
