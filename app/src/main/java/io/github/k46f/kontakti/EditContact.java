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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditContact extends AppCompatActivity {

    public final static int PICK_PHOTO_CODE = 1046;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button changePhotoButton, saveButton, deleteButton;
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

        final Context context = getApplicationContext();

        changePhotoButton = (Button) findViewById(R.id.changePhotoButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
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
        final String contactId = intent.getStringExtra(ViewContact.CONTACT_ID);

        nameText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_NAME));
        phoneText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_PHONE));
        addressText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_ADDRESS));
        emailText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_EMAIL));
        facebookText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_FACEBOOK));
        birthdayText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_BIRTHDAY));

        byte[] photo = dbm.getContactPhoto(contactId);
        Bitmap contactPhoto = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        photoView.setImageBitmap(contactPhoto);

        dbm.closeDb();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textName = nameText.getText().toString();
                String textPhone = phoneText.getText().toString();
                String textAddress = addressText.getText().toString();
                String textEmail = emailText.getText().toString();
                String textFacebook = facebookText.getText().toString();
                String textBirthday = birthdayText.getText().toString();

                // Convert ImageView Image from resources to a Bitmap
                BitmapDrawable drawablePhoto = (BitmapDrawable) photoView.getDrawable();
                Bitmap contactPhoto = drawablePhoto.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                contactPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte photoInByte[] = stream.toByteArray();

                try {
                    DatabaseManager dbm = new DatabaseManager(context);
                    dbm.openDb();
                    int result = dbm.updateContact(textName, textPhone, textAddress, textEmail,
                            textFacebook, textBirthday, contactId, photoInByte);
                    dbm.closeDb();
                    if (result > 0) {
                        Toast kToast = Toast.makeText(context, "Sucess!!", Toast.LENGTH_LONG);
                        kToast.show();
                    }
                } catch (Exception exception) {
                    Toast kToast = Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG);
                    kToast.show();
                }
            }
        });

     }

    public void onChangePhoto(View view){

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
