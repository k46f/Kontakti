package io.github.k46f.kontakti;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewContact extends AppCompatActivity {

    public final static String CONTACT_ID = ">>> Pass Contact Id";

    private TextView phoneView, addressView, emailView, facebookView, birthdayView, fullName;
    private ImageView photoView;

    private final static String NAME_FOR_CONTACT_NAME = "name";
    private final static String NAME_FOR_CONTACT_PHONE = "phone";
    private final static String NAME_FOR_CONTACT_ADDRESS = "address";
    private final static String NAME_FOR_CONTACT_EMAIL = "email";
    private final static String NAME_FOR_CONTACT_FACEBOOK = "facebook";
    private final static String NAME_FOR_CONTACT_BIRTHDAY = "birthday";
    private String contactId;
    private final static String EDIT_SUCCESS = "Contact edited success!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        Context context = getApplicationContext();

        phoneView = (TextView) findViewById(R.id.phoneView);
        addressView = (TextView) findViewById(R.id.addressView);
        emailView = (TextView) findViewById(R.id.emailView);
        facebookView = (TextView) findViewById(R.id.facebookView);
        birthdayView = (TextView) findViewById(R.id.birthdayView);
        fullName = (TextView) findViewById(R.id.fullname);
        photoView = (ImageView) findViewById(R.id.photoView);

        DatabaseManager dbm = new DatabaseManager(context);
        dbm.openDb();

        Intent intent = getIntent();

        String contactIdMain = intent.getStringExtra(MainActivity.CONTACT_ID);
        String contactIdEdit = intent.getStringExtra(EditContact.RETURN_EDIT);

        if (contactIdMain != null){
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

        byte[] photo = dbm.getContactPhoto(contactId);
        Bitmap contactPhoto = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        photoView.setImageBitmap(contactPhoto);

        dbm.closeDb();
    }

    public void editContact(View v) {
        Intent firstIntent = getIntent();
        String contactId = firstIntent.getStringExtra(MainActivity.CONTACT_ID);

        Context context = getApplicationContext();
        Intent intent = new Intent(context, EditContact.class);
        intent.putExtra(CONTACT_ID, contactId);
        startActivity(intent);
    }
}
