package io.github.k46f.kontakti;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class SaveContactTask extends AsyncTask<Void, Contact, Boolean> {
    private Contact contact;
    private ProgressBar pb;
    private String accountID;

    SaveContactTask(Contact contact, ProgressBar pb, String accountID) {
        this.contact = contact;
        this.pb = pb;
        this.accountID = accountID;
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
        DatabaseReference editReference = fbDatabase.getReference("users");
        editReference.child(accountID).push().setValue(contact);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        pb.setVisibility(ProgressBar.INVISIBLE);
    }
}
