package io.github.k46f.kontakti;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditContact extends AppCompatActivity {

    private Button changePhotoButton;
    private ImageView photoView;
    private EditText nameText, phoneText, addressText, emailText, facebookText, birthdayText;

    private final static String NAME_FOR_CONTACT_NAME = "name";
    private final static String NAME_FOR_CONTACT_PHONE = "phone";
    private final static String NAME_FOR_CONTACT_ADDRESS = "address";
    private final static String NAME_FOR_CONTACT_EMAIL = "email";
    private final static String NAME_FOR_CONTACT_FACEBOOK = "facebook";
    private final static String NAME_FOR_CONTACT_BIRTHDAY = "birthday";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        Context context = getApplicationContext();

        changePhotoButton = (Button) findViewById(R.id.changePhotoButton);
        photoView = (ImageView) findViewById(R.id.photoView);
        nameText = (EditText) findViewById(R.id.nameText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        addressText = (EditText) findViewById(R.id.addressText);
        emailText = (EditText) findViewById(R.id.emailText);
        facebookText = (EditText) findViewById(R.id.facebookText);
        birthdayText = (EditText) findViewById(R.id.birthdayText);

        DatabaseManager dbm = new DatabaseManager(context);
        dbm.openDb();

        Intent intent = getIntent();
        String contactId = intent.getStringExtra(ViewContact.CONTACT_ID);

        nameText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_NAME));
        phoneText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_PHONE));
        addressText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_ADDRESS));
        emailText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_EMAIL));
        facebookText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_FACEBOOK));
        birthdayText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_BIRTHDAY));
     }
}
