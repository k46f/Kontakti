package io.github.k46f.kontakti;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.Menu;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private Menu menu;
    private Context ctx;

    public ImageDownloadTask(Menu menu, Context context) {
        this.menu = menu;
        this.ctx = context;
    }


    protected Bitmap doInBackground(String... addresses) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            // 1. Declare a URL Connection
            URL url = new URL(addresses[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 2. Open InputStream to connection
            conn.connect();
            in = conn.getInputStream();
            // 3. Download and decode the bitmap using BitmapFactory
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    // Fires after the task is completed, displaying the bitmap into the ImageView
    @Override
    protected void onPostExecute(Bitmap result) {
        // Convert bitmap Image to Drawable
        Drawable icon = new BitmapDrawable(ctx.getResources(), result);
        // Set Drawable for the result
        menu.getItem(0).setIcon(icon);
    }
}
