package io.github.k46f.kontakti;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NewContact extends AppCompatActivity {

    private Button selectPhotoButton, saveButton;
    private ImageView photoView;
    private EditText nameText, phoneText, addressText, emailText, facebookText, birthdayText;
    private final static String NEW_SUCCESS_MESSAGE = "Contact Saved!";

    public static final String RETURN_SAVE = "1";

    public final static int PICK_PHOTO_CODE = 1046;

    static final int REQUEST_IMAGE_CAPTURE = 1;


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

                // Convert ImageView Image from resources to a Bitmap
                BitmapDrawable drawablePhoto = (BitmapDrawable) photoView.getDrawable();
                Bitmap contactPhoto = drawablePhoto.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                contactPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte photoInByte[] = stream.toByteArray();

                Context context = getApplicationContext();

                String textName = nameText.getText().toString();
                String textPhone = phoneText.getText().toString();
                String textAddress = addressText.getText().toString();
                String textEmail = emailText.getText().toString();
                String textFacebook = facebookText.getText().toString();
                String textBirthday = birthdayText.getText().toString();

                if (textName.equals("")) {

                    Toast noName = Toast.makeText(context, "Please enter a name", Toast.LENGTH_LONG);
                    noName.show();

                } else {

                    try {
                        DatabaseManager dbm = new DatabaseManager(context);
                        dbm.openDb();
                        long result = dbm.register(textName, textPhone, textAddress, textEmail, textFacebook,
                                textBirthday, photoInByte);
                        dbm.closeDb();
                        if (result > 0) {

                            Intent successIntent = new Intent(context, MainActivity.class);
                            startActivity(successIntent);

                            Toast kToast = Toast.makeText(context, NEW_SUCCESS_MESSAGE, Toast.LENGTH_LONG);
                            kToast.show();

                        }
                    } catch (Exception e) {

                        Toast kToast = Toast.makeText(context, e.toString(), Toast.LENGTH_LONG);
                        kToast.show();
                    }

                }
            }
        });
    }

    public void onSelectPhoto(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select photo from:");

        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });
        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, PICK_PHOTO_CODE);
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {

        photoView = (ImageView) findViewById(R.id.photoView);
        selectPhotoButton.setText("Change Photo");

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap takedImage = (Bitmap) extras.get("data");
            photoView.setImageBitmap(takedImage);
        } else {
            if (data != null) {
                try {
                    Uri photoUri = data.getData();
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    photoView.setImageBitmap(selectedImage);
                } catch (IOException exception) {
                    Toast.makeText(this, "Error ->" + exception, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
