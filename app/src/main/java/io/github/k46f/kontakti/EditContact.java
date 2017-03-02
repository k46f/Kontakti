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
    }
}
