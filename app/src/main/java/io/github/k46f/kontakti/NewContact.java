package io.github.k46f.kontakti;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NewContact extends AppCompatActivity {

    private Button selectPhotoButton, saveButton;
    private ImageView photoView;
    private EditText nameText, phoneText, addressText, emailText, facebookText, birthdayText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        selectPhotoButton = (Button) findViewById(R.id.selectPhotoButton);
        photoView = (ImageView) findViewById(R.id.photoView);
        nameText = (EditText) findViewById(R.id.nameText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        addressText = (EditText) findViewById(R.id.addressText);
        emailText = (EditText) findViewById(R.id.emailText);
        facebookText = (EditText) findViewById(R.id.facebookText);
        birthdayText = (EditText) findViewById(R.id.birthdayText);
        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();

                String textName = nameText.getText().toString();
                String textPhone = phoneText.getText().toString();
                String textAddress = addressText.getText().toString();
                String textEmail = emailText.getText().toString();
                String textFacebook = facebookText.getText().toString();
                String textBirthday = birthdayText.getText().toString();

                try {
                    DatabaseManager dbm = new DatabaseManager(context);
                    dbm.openDb();
                    long result = dbm.register(textName, textPhone, textAddress, textEmail, textFacebook,
                            textBirthday);
                    dbm.closeDb();
                    if (result > 0) {
                        Toast kToast = Toast.makeText(context, "Success!!!", Toast.LENGTH_LONG);
                        kToast.show();
                    }
                } catch (Exception e) {
                    Toast kToast = Toast.makeText(context, e.toString(), Toast.LENGTH_LONG);
                    kToast.show();
                }
            }
        });
    }
}
