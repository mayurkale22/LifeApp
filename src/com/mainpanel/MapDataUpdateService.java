package com.mainpanel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.location.Geocoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;


import com.google.cloud.backend.core.CloudBackend;
import com.google.cloud.backend.core.CloudBackendFragment.OnListener;
import com.google.cloud.backend.core.CloudBackendFragment;
import com.google.cloud.backend.core.CloudCallbackHandler;
import com.google.cloud.backend.core.CloudEntity;
import com.mainpanel.LifeApp_Map;
import com.szugyi.circlemenu.R;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * PingService creates a notification that includes 2 buttons: one to snooze the
 * notification, and one to dismiss it.
 */
public class MapDataUpdateService extends Service implements OnListener {
	
    public static final String GUESTBOOK_SHARED_PREFS = "GUESTBOOK_SHARED_PREFS";
    public static final String SHOW_INTRO_PREFS_KEY = "SHOW_INTRO_PREFS_KEY";
    public static final String SCOPE_PREFS_KEY = "SCOPE_PREFS_KEY";
	
    @Override
    public IBinder onBind(Intent arg0) {
    	Log.d("ping","onBind call");
        return null;
    }
    protected LocationManager locationManager;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 180* 1000; // in Milliseconds
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.d("ping","in MapDataUpdateService call");
    	
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
                		LocationManager.GPS_PROVIDER,
                        MINIMUM_TIME_BETWEEN_UPDATES,
                        MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                        new MyLocationListener() );
        //initiateFragments();
              
        return START_STICKY;
    }
    
    public class MyLocationListener implements LocationListener {
    public void onLocationChanged(Location location) {
 	   //get address from Latitude and Longitude
 	   //get address from Latitude and Longitude
 	   String address = "";
        //Toast.makeText(MapDataUpdateService.this, address, Toast.LENGTH_SHORT).show();
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	Date date = new Date();
    	
    	String timestamp = dateFormat.format(date);
        String data = location.getLatitude() + "app#map" +  location.getLongitude() + "app#map" +address  + "app#map" + timestamp +"\n";
        
        String file = "location_data2000.txt";
        try {
           FileOutputStream fOut = openFileOutput(file, MODE_APPEND | MODE_WORLD_READABLE);
           fOut.write(data.getBytes());
           fOut.close();
        } catch (Exception e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
        }      
        
    }    

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}

	@Override
	public void onCreateFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBroadcastMessageReceived(List<CloudEntity> message) {
		// TODO Auto-generated method stub
		
	}
}
