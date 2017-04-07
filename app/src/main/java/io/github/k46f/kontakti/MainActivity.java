package io.github.k46f.kontakti;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    public final static String CONTACT_ID = ">>> Pass Contact Id";
    private final static String NAME_FOR_CONTACT_ID = "contact_id";
    private String accountID, accountPhoto;
    private GoogleApiClient mGoogleApiClient;
    private String favSwitch = "false";
    ListView kontakti_listView;

    Context ctx = this;

    SharedPreferences gAccountSettings;
    private FirebaseListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        gAccountSettings = getSharedPreferences("gAccountSettings", Context.MODE_PRIVATE);

        accountID = gAccountSettings.getString("accountID", null);
        accountPhoto = gAccountSettings.getString("personPhoto", null);

        if (accountID == null){
            Intent gSignIn = new Intent(ctx, Login.class);
            startActivity(gSignIn);
            finish();
        }

        kontakti_listView = (ListView) findViewById(R.id.kontakti_listView);

        DatabaseReference listViewRef = FirebaseDatabase.getInstance().getReference("users/"+accountID);

        mAdapter = new FirebaseListAdapter<Contact>(this, Contact.class,
                R.layout.contacts_item_list_view, listViewRef) {

            @Override
            protected void populateView(View view, Contact contact, int position) {
                ((TextView)view.findViewById(R.id.kontakti_name)).setText(contact.getName());
                ((TextView)view.findViewById(R.id.kontakti_phone)).setText(contact.getPhone());

                byte[] decodedString = Base64.decode(contact.getPhoto(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ((ImageView)view.findViewById(R.id.kontakti_avatar)).setImageBitmap(decodedByte);
            }
        };
        kontakti_listView.setAdapter(mAdapter);



        kontakti_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String contact_id = mAdapter.getRef(position).getKey();

                Intent intent = new Intent(ctx, ViewContact.class);
                intent.putExtra(CONTACT_ID, contact_id);
                startActivity(intent);
            }
        });

        kontakti_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String contact_id = mAdapter.getRef(position).getKey();

                MainActivityDeleteBar deleteBar = new MainActivityDeleteBar(contact_id, view);
                deleteBar.onLongClick();

                // Step 4 - Setup the listener for this object
                deleteBar.setCustomObjectListener(new MainActivityDeleteBar.DeleteButtonListener() {
                    @Override
                    public void onButtonPressed(final String contactId) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

                        builder.setTitle("Are you sure?");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference editReference = fbDatabase.getReference("users/"+accountID+"/"+contactId);
                                editReference.setValue(null);

                                Toast kToast = Toast.makeText(ctx, "Deleted!", Toast.LENGTH_LONG);
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
                });

                return true;
            }
        });
    }

    public void addNew(MenuItem mi) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, NewContact.class);
        startActivity(intent);
    }

    public void miAccountButton(MenuItem mi){
        View menuItemView = findViewById(R.id.miAccount); // SAME ID AS MENU ID
        PopupMenu popup = new PopupMenu(this, menuItemView);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.menu_popup_account, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.account_sign_out:
                        signOut();
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }

    public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        if (accountPhoto != null) {
            if (isNetworkAvailable()) {
                if (isOnline()) {
                    new ImageDownloadTask(menu, ctx).execute(accountPhoto);
                }
            }
        }
        return true;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    private void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        SharedPreferences.Editor editor = gAccountSettings.edit();
                        editor.putString("accountID", null);
                        editor.putString("personName", null);
                        editor.putString("personEmail", null);
                        editor.putString("personPhoto", null);
                        editor.apply();

                        Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),Login.class);
                        startActivity(i);

                        finish();
                    }
                });
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast connectionFailed = Toast.makeText(this, "Connection failed"+connectionResult, Toast.LENGTH_LONG);
        connectionFailed.show();
    }

    public void favButton(MenuItem mi){
        if (Objects.equals(favSwitch, "false")){
            favSwitch = "true";

            kontakti_listView.setAdapter(null);

            DatabaseReference listViewRef = FirebaseDatabase.getInstance().getReference("users/"+accountID);
            Query listViewRefFav = listViewRef.orderByChild("fav").equalTo("true");

            mAdapter = new FirebaseListAdapter<Contact>(this, Contact.class,
                    R.layout.contacts_item_list_view, listViewRefFav) {

                @Override
                protected void populateView(View view, Contact contact, int position) {
                    ((TextView)view.findViewById(R.id.kontakti_name)).setText(contact.getName());
                    ((TextView)view.findViewById(R.id.kontakti_phone)).setText(contact.getPhone());

                    byte[] decodedString = Base64.decode(contact.getPhoto(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ((ImageView)view.findViewById(R.id.kontakti_avatar)).setImageBitmap(decodedByte);
                }
            };
            kontakti_listView.setAdapter(mAdapter);
        } else {
            if (Objects.equals(favSwitch, "true")){
                favSwitch = "false";

                kontakti_listView.setAdapter(null);
                DatabaseReference listViewRef = FirebaseDatabase.getInstance().getReference("users/"+accountID);

                mAdapter = new FirebaseListAdapter<Contact>(this, Contact.class,
                        R.layout.contacts_item_list_view, listViewRef) {

                    @Override
                    protected void populateView(View view, Contact contact, int position) {
                        ((TextView)view.findViewById(R.id.kontakti_name)).setText(contact.getName());
                        ((TextView)view.findViewById(R.id.kontakti_phone)).setText(contact.getPhone());

                        byte[] decodedString = Base64.decode(contact.getPhoto(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        ((ImageView)view.findViewById(R.id.kontakti_avatar)).setImageBitmap(decodedByte);
                    }
                };
                kontakti_listView.setAdapter(mAdapter);
            }
        }


    }

}