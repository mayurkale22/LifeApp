package com.mainpanel.view;

import java.io.InputStream;

import com.google.android.gms.maps.model.Marker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Marker marker;
    boolean refresh;

    public DownloadImageTask(final ImageView bmImage, final Marker marker) {
        this.bmImage = bmImage;
        this.marker=marker;
        this.refresh=false;
    }

   public void SetRefresh(boolean refresh ){
       this.refresh=true;

   }

  /*  @Override
    protected void onPreExecute() 
    {
        super.onPreExecute();
        bmImage.setImageBitmap(null);
    }*/

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
          InputStream in = new java.net.URL(urldisplay).openStream();
          mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        if(!refresh){
            SetRefresh(refresh);
            bmImage.setImageBitmap(result);
            marker.showInfoWindow();
        }
    }
  }