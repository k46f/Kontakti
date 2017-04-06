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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    public final static String CONTACT_ID = ">>> Pass Contact Id";
    private final static String NAME_FOR_CONTACT_ID = "contact_id";
    private String accountID, accountPhoto;

    Context ctx = this;

    SharedPreferences gAccountSettings;

    FirebaseListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gAccountSettings = getSharedPreferences("gAccountSettings", Context.MODE_PRIVATE);

        accountID = gAccountSettings.getString("accountID", null);
        accountPhoto = gAccountSettings.getString("personPhoto", null);

        if (accountID == null){
            Intent gSignIn = new Intent(ctx, Login.class);
            startActivity(gSignIn);
            this.finish();
        }



        // Find ListView to populate
        ListView kontakti_listView = (ListView) findViewById(R.id.kontakti_listView);

        DatabaseReference listViewRef = FirebaseDatabase.getInstance().getReference("users/"+accountID);

        final FirebaseListAdapter mAdapter = new FirebaseListAdapter<Contact>(this, Contact.class, R.layout.contacts_item_list_view, listViewRef) {
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
                        Toast.makeText(MainActivity.this, "Keyword!", Toast.LENGTH_SHORT).show();
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


}