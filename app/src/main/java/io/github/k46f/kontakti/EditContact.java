package io.github.k46f.kontakti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditContact extends AppCompatActivity {

    private Button changePhotoButton;
    private ImageView photoView;
    private EditText nameText, phoneText, addressText, emailText, facebookText, birthdayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        changePhotoButton = (Button) findViewById(R.id.changePhotoButton);
        photoView = (ImageView) findViewById(R.id.photoView);
        nameText = (EditText) findViewById(R.id.nameText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        addressText = (EditText) findViewById(R.id.addressText);
        emailText = (EditText) findViewById(R.id.emailText);
        facebookText = (EditText) findViewById(R.id.facebookText);
        birthdayText = (EditText) findViewById(R.id.birthdayText);
     }
}
