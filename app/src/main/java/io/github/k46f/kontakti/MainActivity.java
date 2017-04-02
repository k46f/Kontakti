package io.github.k46f.kontakti;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    public final static String CONTACT_ID = ">>> Pass Contact Id";
    private final static String NAME_FOR_CONTACT_ID = "contact_id";

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    Context ctx = this;

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

        // DataBasemanager is a SQLiteOpenHelper class connecting to SQLite
        DatabaseManager dbm = new DatabaseManager(this);
        // Get access to the underlying writeable database
        SQLiteDatabase db = dbm.getWritableDatabase();
        // Query for items from the database and get a cursor back
        Cursor contactsFillCursor = db.rawQuery("SELECT rowid _id,* FROM contacts", null);

        // Find ListView to populate
        ListView kontakti_listView = (ListView) findViewById(R.id.kontakti_listView);
        // Setup cursor adapter using cursor from last step
        CursorAdapterManager contactsAdapter = new CursorAdapterManager(this, contactsFillCursor);
        // Attach cursor adapter to the ListView
        kontakti_listView.setAdapter(contactsAdapter);

        kontakti_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Context context = getApplicationContext();

                DatabaseManager dbm = new DatabaseManager(context);
                SQLiteDatabase db = dbm.getWritableDatabase();
                Cursor onClickListView = db.rawQuery("SELECT contact_id FROM contacts", null);
                onClickListView.moveToPosition(position);
                int data = onClickListView.getColumnIndexOrThrow(NAME_FOR_CONTACT_ID);
                String contact_id = onClickListView.getString(data);
                onClickListView.close();

                Intent intent = new Intent(context, ViewContact.class);
                intent.putExtra(CONTACT_ID, contact_id);
                startActivity(intent);
            }
        });

        kontakti_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Context context = getApplicationContext();

                DatabaseManager dbm = new DatabaseManager(context);
                SQLiteDatabase db = dbm.getWritableDatabase();
                Cursor onClickListView = db.rawQuery("SELECT contact_id FROM contacts", null);
                onClickListView.moveToPosition(position);
                int data = onClickListView.getColumnIndexOrThrow(NAME_FOR_CONTACT_ID);
                String contact_id = onClickListView.getString(data);
                onClickListView.close();

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

                                DatabaseManager dbm = new DatabaseManager(ctx);
                                dbm.openDb();
                                dbm.deleteContact(contactId);

                                Intent mainActivityIntent = new Intent(ctx, MainActivity.class);
                                startActivity(mainActivityIntent);

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

    public void signIn(MenuItem mi){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast connectionFailed = Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG);
        connectionFailed.show();
    }

    public void addNew(MenuItem mi) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, NewContact.class);
        startActivity(intent);
    }

    public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }
}