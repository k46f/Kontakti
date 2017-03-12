package io.github.k46f.kontakti;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class CursorAdapterManager extends CursorAdapter{

    private final static String NAME_FOR_CONTACT_NAME = "name";
    private final static String NAME_FOR_CONTACT_PHOTO = "photo";

    CursorAdapterManager(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.contacts_item_list_view, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView kontakti_name = (TextView) view.findViewById(R.id.kontakti_name);
        ImageView kontakti_avatar = (ImageView) view.findViewById(R.id.kontakti_avatar);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_FOR_CONTACT_NAME));
        byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(NAME_FOR_CONTACT_PHOTO));
        // Populate fields with extracted properties
        kontakti_name.setText(name);

        Bitmap contactPhoto = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        kontakti_avatar.setImageBitmap(contactPhoto);
    }
}
