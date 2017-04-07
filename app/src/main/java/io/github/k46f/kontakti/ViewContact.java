package io.github.k46f.kontakti;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ViewContact extends AppCompatActivity {

    public final static String CONTACT_ID = ">>> Pass Contact Id";

    private TextView fullName;
    private EditText phoneView, addressView, emailView, facebookView, birthdayView;
    private ImageView photoView;

    private final static String NAME_FOR_CONTACT_NAME = "name";
    private final static String NAME_FOR_CONTACT_PHONE = "phone";
    private final static String NAME_FOR_CONTACT_ADDRESS = "address";
    private final static String NAME_FOR_CONTACT_EMAIL = "email";
    private final static String NAME_FOR_CONTACT_FACEBOOK = "facebook";
    private final static String NAME_FOR_CONTACT_BIRTHDAY = "birthday";
    private final static String NAME_FOR_CONTACT_LOCATION = "location";
    private String contactId, accountID, coordinates, favorites;
    private final static String EDIT_SUCCESS = "Contact edited success!";
    private String favSwitch = "false";

    Context ctx = this;
    SharedPreferences gAccountSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);


        gAccountSettings = getSharedPreferences("gAccountSettings", Context.MODE_PRIVATE);
        accountID = gAccountSettings.getString("accountID", null);

        // Show back button in action bar, boolena method to go back is at the end of activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Context context = getApplicationContext();

        phoneView = (EditText) findViewById(R.id.phoneView);
        addressView = (EditText) findViewById(R.id.addressView);
        emailView = (EditText) findViewById(R.id.emailView);
        facebookView = (EditText) findViewById(R.id.facebookView);
        birthdayView = (EditText) findViewById(R.id.birthdayView);
        fullName = (TextView) findViewById(R.id.fullname);
        photoView = (ImageView) findViewById(R.id.photoView);

        Intent intent = getIntent();

        String contactIdMain = intent.getStringExtra(MainActivity.CONTACT_ID);
        String contactIdEdit = intent.getStringExtra(EditContact.RETURN_EDIT);

        if (contactIdMain != null) {
            contactId = contactIdMain;
        } else {
            contactId = contactIdEdit;
        }

        DatabaseReference listViewRef = FirebaseDatabase.getInstance().getReference("users/"+accountID+"/"+contactId);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Contact contact = dataSnapshot.getValue(Contact.class);

                fullName.setText(contact.getName());
                addressView.setText(contact.getAddress());
                emailView.setText(contact.getEmail());
                facebookView.setText(contact.getFacebook());
                birthdayView.setText(contact.getBirthday());
                phoneView.setText(contact.getPhone());
                coordinates = contact.getLocation();
                favorites = contact.getFav();

                byte[] decodedString = Base64.decode(contact.getPhoto(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                photoView.setImageBitmap(decodedByte);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        listViewRef.addValueEventListener(postListener);

    }

    public void phoneClick(View view){

        if (!Objects.equals(phoneView.getText().toString(), "")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("¿What do you wish?");

            builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneView.getText().toString()));
                    if (callIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(callIntent);
                    }

                }
            });
            builder.setNegativeButton("Sms", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    String to = phoneView.getText().toString();

                    Uri smsUri = Uri.parse("tel:" + to);
                    Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
                    intent.putExtra("address", to);
                    intent.setType("vnd.android-dir/mms-sms");//here setType will set the previous data null.
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void emailClick(View view){
        if (!Objects.equals(emailView.getText().toString(), "")) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailView.getText().toString()});
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, ""));
            }
        }
    }

    public void facebookClick(View view){
        if (!Objects.equals(phoneView.getText().toString(), "")) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + facebookView.getText().toString()));
            if (browserIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(browserIntent);
            }
        }
    }

    public void locationClick(MenuItem mi){
        if (!Objects.equals(coordinates, "")) {

            Uri gmmIntentUri = Uri.parse("geo:" + coordinates);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        } else {
            Toast.makeText(ctx, "No location available", Toast.LENGTH_LONG).show();
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

        if (!Objects.equals(favSwitch, favorites)){

            favSwitch = favorites;
            menu.getItem(1).setIcon(R.drawable.ic_star_white_24dp);
        }

        return true;
    }

    public void favButtonVc(MenuItem mi){

        if (Objects.equals(favSwitch, "false")){
            favSwitch = "true";
            mi.setIcon(R.drawable.ic_star_white_24dp);
            DatabaseReference favref = FirebaseDatabase.getInstance().getReference("users/"+accountID+"/"+contactId);
            favref.child("fav").setValue("true");
            Toast addFav = Toast.makeText(ctx, "Contact Added To Favorites", Toast.LENGTH_LONG);
            addFav.show();
        } else {
            if (Objects.equals(favSwitch, "true")){
                favSwitch = "false";
                mi.setIcon(R.drawable.ic_star_border_white_24dp);
                DatabaseReference favref = FirebaseDatabase.getInstance().getReference("users/"+accountID+"/"+contactId);
                favref.child("fav").setValue("false");
                Toast removeFav = Toast.makeText(ctx, "Contact Removed From Favorites", Toast.LENGTH_LONG);
                removeFav.show();
            }
        }

    }
}
