package io.github.k46f.kontakti;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditContact extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final static int PICK_PHOTO_CODE = 1046;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button changePhotoButton, saveButton, deleteButton;
    private ImageView photoView;
    private EditText nameText, phoneText, addressText, emailText, facebookText, birthdayText, locationText;

    private final static String NAME_FOR_CONTACT_NAME = "name";
    private final static String NAME_FOR_CONTACT_PHONE = "phone";
    private final static String NAME_FOR_CONTACT_ADDRESS = "address";
    private final static String NAME_FOR_CONTACT_EMAIL = "email";
    private final static String NAME_FOR_CONTACT_FACEBOOK = "facebook";
    private final static String NAME_FOR_CONTACT_BIRTHDAY = "birthday";
    private final static String NAME_FOR_CONTACT_LOCATION = "location";
    public final static String RETURN_EDIT = "Return Edit";
    private final static String ACTIVITY_TITLE = "Edit Contact";

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        // Show back button in action bar, boolena method to go back is at the end of activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ACTIVITY_TITLE);

        final Context context = getApplicationContext();

        saveButton = (Button) findViewById(R.id.saveButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        photoView = (ImageView) findViewById(R.id.photoView);
        nameText = (EditText) findViewById(R.id.nameText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        addressText = (EditText) findViewById(R.id.addressText);
        emailText = (EditText) findViewById(R.id.emailText);
        facebookText = (EditText) findViewById(R.id.facebookText);
        locationText = (EditText) findViewById(R.id.locationText);
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
        locationText.setText(dbm.getSingleField(contactId, NAME_FOR_CONTACT_LOCATION));    

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
                String textLocation = locationText.getText().toString();

                // Convert ImageView Image from resources to a Bitmap
                BitmapDrawable drawablePhoto = (BitmapDrawable) photoView.getDrawable();
                Bitmap contactPhoto = drawablePhoto.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                contactPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte photoInByte[] = stream.toByteArray();

                if (textName.equals("")){

                    Toast noName = Toast.makeText(context, "Please enter a name", Toast.LENGTH_LONG);
                    noName.show();

                } else {

                    try {
                        DatabaseManager dbm = new DatabaseManager(context);
                        dbm.openDb();
                        int result = dbm.updateContact(textName, textPhone, textAddress, textEmail,
                                textFacebook, textBirthday, contactId, photoInByte, textLocation);
                        dbm.closeDb();
                        if (result > 0) {

                            Intent successIntent = new Intent(getApplicationContext(), ViewContact.class);
                            successIntent.putExtra(RETURN_EDIT, contactId);
                            startActivity(successIntent);
                        }
                    } catch (Exception exception) {
                        Toast kToast = Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG);
                        kToast.show();
                    }
                }
            }
        });

     }
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

    public void onDelete(final String contactId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Context context = getApplicationContext();

                DatabaseManager dbm = new DatabaseManager(context);
                dbm.openDb();
                dbm.deleteContact(contactId);

                Intent mainActivityIntent = new Intent(context, MainActivity.class);
                startActivity(mainActivityIntent);

                Toast kToast = Toast.makeText(context, "Deleted!", Toast.LENGTH_LONG);
                kToast.show();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Method to go back
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_contact, menu);
        return true;
    }

    protected void onStart() {
        super.onStart();
        // Connect the location client.
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Get last known recent location.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast connectionFailed = Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG);
        connectionFailed.show();
    }

    public void getLocation(View view){

        locationText = (EditText) findViewById(R.id.locationText);
        // Get last known recent location.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
            String latitude = String.valueOf(mCurrentLocation.getLatitude());
            String longitude = String.valueOf(mCurrentLocation.getLongitude());

            locationText.setText(latitude + " " + longitude);
        }
    }
}
