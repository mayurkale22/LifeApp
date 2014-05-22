/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mainpanel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import com.mainpanel.view.HandleXML;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.cloud.backend.core.CloudBackendFragment;
import com.google.cloud.backend.core.CloudCallbackHandler;
import com.google.cloud.backend.core.CloudBackendFragment.OnListener;
import com.google.cloud.backend.core.CloudEntity;
import com.loopj.android.image.SmartImageView;
import com.mainpanel.view.DownloadImageTask;
import com.mainpanel.view.PlaceJSONParser;
import com.mainpanel.view.UserSettingActivity;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.szugyi.circlemenu.R;
import com.szugyi.circlemenu.R.color;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location. To
 * track changes to the users location on the map, we request updates from the
 * {@link LocationClient}.
 */
public class LifeApp_Map extends FragmentActivity
        implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener,
        OnInfoWindowClickListener,
        OnMyLocationButtonClickListener,
        OnListener {

    public static GoogleMap mMap; 
    public static double my_lat, my_longi;
    public LocationClient mLocationClient;
    public TextView mMessageView;
    
    private static final int RESULT_SETTINGS = 1;
    
    private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT";

    public static final String GUESTBOOK_SHARED_PREFS = "GUESTBOOK_SHARED_PREFS";
    public static final String SHOW_INTRO_PREFS_KEY = "SHOW_INTRO_PREFS_KEY";
    public static final String SCOPE_PREFS_KEY = "SCOPE_PREFS_KEY";

    private FragmentManager mFragmentManager;
    private CloudBackendFragment mProcessingFragment;
    private ProgressBar spinner;
    
    private String food_type;
	int food_radius = 500;
	String event_type = "workshop";
	int event_radius = 1;
	boolean notification_status = true;
    
    public String getfood_type() {
    	return food_type;   
    }

    public void setfood_type(String type) {
        this.food_type = type;
        
        Log.d("ping"," setter "+this.food_type);
    }  
    
    // These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
    public static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location_demo);
        mMessageView = (TextView) findViewById(R.id.message_text);
        
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        Log.d("mymap", "In create");
        
        try{
		Bundle bundle = getIntent().getExtras();
		String pingservice = bundle.getString("pingservice");
		showrestaurant();
		Log.d("datastore","pingservice -- " + pingservice);
        }catch(Exception e){
        	Log.d("datastore","Exception : pingservice -- " + e.getMessage());
        }
    }
    
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}
    
    @Override
    public void onResume() {
    	Log.d("mymap", "In resume");
    	super.onResume();
        setUpMapIfNeeded();
        setUpLocationClientIfNeeded();
        mLocationClient.connect();   
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        mFragmentManager = getFragmentManager();

        initiateFragments();
        
       
        
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
        Log.d("mymap", "In pause");
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent i = new Intent(this, UserSettingActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			overridePendingTransition (R.anim.card_flip_left_in, R.anim.card_flip_left_out);
			break;

		}

		return true;
	}

    public void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        //if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();           
            
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // disable My Location Button
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            // Check if we were successful in obtaining the map.
            //if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
               
            //}
       // }
        Log.d("mymap", "In setUpMapIfNeeded"); 
    }

    
    public void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    getApplicationContext(),
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
        Log.d("mymap", "In setUpLocationClientIfNeeded");   
        
    }
    
    /**
     * Method called via OnListener in {@link CloudBackendFragment}.
     */
    @Override
    public void onCreateFinished() {
//    	String file = "location_data2000.txt";
//        try{
//            FileInputStream fin = openFileInput(file);
//            int c;
//            String temp="";
//            while( (c = fin.read()) != -1){
//               temp = temp + Character.toString((char)c);
//               if (c == '\n'){
//            	   String[] Map_data = temp.split("app#map");
//            	
//            	   String location_lat = Map_data[0];
//            	   String location_longi = Map_data[1];
//            	   String location = "";
//            	   String timestamp = Map_data[3];
//            	   
//            	   //get address from Latitude and Longitude
//            	   Geocoder gcd = new Geocoder(this, Locale.getDefault());
//            	   List<Address> addr = gcd.getFromLocation(Double.parseDouble(Map_data[0]), Double.parseDouble(Map_data[1]),1);
//            	   if (addr.size() > 0 && addr != null) {
//            		   location = addr.get(0).getFeatureName()+"-"+addr.get(0).getLocality()+"-"+addr.get(0).getAdminArea()+"-"+addr.get(0).getCountryName();
//            		   
//            	   }
//            	   
//            	   
//            	   insert_datastore(location_lat,location_longi,location,timestamp);
//            	   Log.d("insert",location_lat +location_longi+location+timestamp);	   
//            	   temp = "";
//               }
//            }
//            
//            fin.close();
//            
//            FileOutputStream fOut = openFileOutput(file, MODE_WORLD_WRITEABLE);
//            fOut.write((new String()).getBytes());
//            fOut.close();
//
//         }catch(Exception e){
//        	 Log.d("insert",e.getMessage());
//         }   	
    	
      
    }

    private void initiateFragments() {
    	Log.d("datastore","initiateFragments");
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // Check to see if we have retained the fragment which handles
        // asynchronous backend calls
        mProcessingFragment = (CloudBackendFragment) mFragmentManager.
                findFragmentByTag(PROCESSING_FRAGMENT_TAG);
        // If not retained (or first time running), create a new one
        if (mProcessingFragment == null) {
            mProcessingFragment = new CloudBackendFragment();
            mProcessingFragment.setRetainInstance(true);
            fragmentTransaction.add(mProcessingFragment, PROCESSING_FRAGMENT_TAG);
        }
            fragmentTransaction.commit();
    }

    public void insert_datastore(String location_lat, String location_longi,String location,String timestamp) {
    	Log.d("datastore","onSendButtonPressed");

        // create a CloudEntity with the new post
        CloudEntity newPost = new CloudEntity("Map_Data");

        newPost.put("latitude", location_lat);
        newPost.put("longitude", location_longi);
        newPost.put("location", location);
        newPost.put("timestamp",timestamp);
        
        // create a response handler that will receive the result or an error
        CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
            @Override
            public void onComplete(final CloudEntity result) {
	                Log.d("datastore","complete");
            }

            @Override
            public void onError(final IOException exception) {
            	Log.d("datastore","onError");
                handleEndpointException(exception);
            }
        };
        mProcessingFragment.getCloudBackend().insert(newPost, handler);

    }

    private void handleEndpointException(IOException e) {
    	Log.d("datastore","handleEndpointException");
               
    }
    
    
    /**
     * Button to get current Location. This demonstrates how to get the current Location as required
     * without needing to register a LocationListener.
     * @throws IOException 
     */
    public void showMyLocation(View view) throws IOException {
    	String location_name = "unknown";
    	if (mLocationClient != null && mLocationClient.isConnected()) {
        	onMyLocationButtonClick();
        	
        	String msg ="";
            my_lat = mLocationClient.getLastLocation().getLatitude();
            my_longi = mLocationClient.getLastLocation().getLongitude();

            
//            my_lat = 40.6944;
//            my_longi = -73.9865;
            
            	   //get address from Latitude and Longitude
            	   Geocoder gcd = new Geocoder(this, Locale.getDefault());
            	   List<Address> addresses = gcd.getFromLocation(my_lat, my_longi,100);
            	   if (addresses.size() > 0 && addresses != null) {
            	                   msg = "Address : " +addresses.get(0).getFeatureName()+"-"+addresses.get(0).getLocality()+"-"+addresses.get(0).getAdminArea()+"-"+addresses.get(0).getCountryName();
            	                   location_name = addresses.get(0).getFeatureName()+"-"+addresses.get(0).getLocality()+"-"+addresses.get(0).getAdminArea()+"-"+addresses.get(0).getCountryName();
            	   }	   
            	  
            	   msg += "\nLatitude : " + my_lat;
            	   msg += "\nLongitude : " + my_longi ;   
            	   
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
        Log.d("mymap", "In showMyLocation");  
        
    }


    /**
     * Implementation of {@link LocationListener}.
     */
    @Override
    public void onLocationChanged(Location location) {
    	/*mMessageView.setText("Location = " + location);*/
    }

    /**
     * Callback called when connected to GCore. Implementation of {@link ConnectionCallbacks}.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        mLocationClient.requestLocationUpdates(
                REQUEST,
                this);  // LocationListener
        
        onMyLocationButtonClick();       
        Log.d("mymap", "In onConnected"); 
    }

    /**
     * Callback called when disconnected from GCore. Implementation of {@link ConnectionCallbacks}.
     */
    @Override
    public void onDisconnected() {
        // Do nothing
    }

    /**
     * Implementation of {@link OnConnectionFailedListener}.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do nothing
    }

    @SuppressLint("NewApi")
	@Override
    public boolean onMyLocationButtonClick() {
        Log.d("mymap", "button click event");
    	//Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        //mMap.clear();
        double lat = mLocationClient.getLastLocation().getLatitude();
        double longi = mLocationClient.getLastLocation().getLongitude();
        
//        lat = 40.6944;
//        longi = -73.9865;
        
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(lat, longi)).zoom(15).build();
 
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        
        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, longi)).title("You're here").icon(BitmapDescriptorFactory.fromResource(R.drawable.home));
        
        
        // adding marker
        mMap.addMarker(marker).showInfoWindow();
        
        CircleOptions circleOptions = new CircleOptions()
        .center(new LatLng(lat, longi))   //set center
        .radius(500)   //set radius in meters
        .fillColor(Color.parseColor("#500084d3"))
        .strokeColor(Color.TRANSPARENT)
        .strokeWidth(2);
        
        mMap.addCircle(circleOptions);
        
        mMap.setOnInfoWindowClickListener(this);
        
        return false;
    }
    
    public void showevents(View view) throws IOException {
    	 spinner.setVisibility(View.VISIBLE);
    	
    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);   	

        double mLatitude = mLocationClient.getLastLocation().getLatitude();
        double mLongitude = mLocationClient.getLastLocation().getLongitude();
    	
    	event_type = sharedPrefs.getString("event_type", "workshop");
    	event_radius = sharedPrefs.getInt("event_radius", 1);
    	
    	String msg ="Searching " + event_type + " within " + event_radius + " radius.";
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    	
    	HandleXML obj;
        String finalUrl ="https://www.eventbrite.com/xml/event_search?app_key=B5KBKT5IZNBOOFXF2G&within="+event_radius+"&longitude="+mLongitude+"&latitude=" + mLatitude;
        obj = new HandleXML(finalUrl);
        obj.fetchXML();
        while(obj.parsingComplete);
                
        String events_data = obj.getevent_details();
        
        String[] event = events_data.split("eventend#");
     
        for (int i = 0; i< event.length-1;i++){
			  			  
			  if (event_type.toLowerCase().equals("all")){
		        	String [] each_event = event[i].split("lifeapp#");
		        	String eTitle = "Event : "  + each_event[0];
		        	double eLat = Double.parseDouble(each_event[1].split("/")[0]);
		        	double eLong = Double.parseDouble(each_event[1].split("/")[1]);
		        	String eDesc = each_event[2] + "lifeapp#" +each_event[3] + "lifeapp#" + each_event[4] + "lifeapp#" +  each_event[5];
		        	
		            // create marker
		            MarkerOptions marker = new MarkerOptions().position(new LatLng(eLat, eLong)).title(eTitle).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_eventmap));
		            marker.snippet(eDesc);
		            // adding marker
		            mMap.addMarker(marker);
			  }else if (event[i].toLowerCase().contains(event_type.toLowerCase()) || event[i].toLowerCase().contains(event_type.toLowerCase())){
		          	
		      	String [] each_event = event[i].split("lifeapp#");
		      	String eTitle = "Event : "  + each_event[0];
		      	double eLat = Double.parseDouble(each_event[1].split("/")[0]);
		      	double eLong = Double.parseDouble(each_event[1].split("/")[1]);
		      	String eDesc = each_event[2] + "lifeapp#" +each_event[3] + "lifeapp#" + each_event[4] + "lifeapp#" +  each_event[5];
		      	
		          // create marker
		          MarkerOptions marker = new MarkerOptions().position(new LatLng(eLat, eLong)).title(eTitle).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_eventmap));
		          marker.snippet(eDesc);
		          // adding marker
		          mMap.addMarker(marker);
			  }      
        }
        spinner.setVisibility(View.GONE);
        
    }
    
    public void showrestaurant(View view) throws IOException {
    	spinner.setVisibility(View.VISIBLE);
    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);   	

    	food_type = sharedPrefs.getString("food_type", "restaurant");
    	notification_status =  sharedPrefs.getBoolean("prefnotification", true);
    	food_radius = sharedPrefs.getInt("food_radius", 500);
    	
    	String msg ="Searching " + food_type + " within " + food_radius + " radius.";
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    	
        double mLatitude = mLocationClient.getLastLocation().getLatitude();
        double mLongitude = mLocationClient.getLastLocation().getLongitude();

//        mLatitude = 40.6944;
//        mLongitude = -73.9865;
        
           
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(mLatitude, mLongitude)).zoom(15).build();
    	
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                
       
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location="+mLatitude+","+mLongitude);
        sb.append("&radius="+food_radius);
        sb.append("&types="+food_type.toLowerCase());
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyAFAFiOe4YPhXPuD9Vdq9Q29m5Iel_imJE");

        // Creating a new non-ui thread task to download Google place json data
        PlacesTask placesTask = new PlacesTask(food_type.toLowerCase());

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());
        spinner.setVisibility(View.GONE);
    }    
    
    public void showrestaurant() {
    	spinner.setVisibility(View.VISIBLE);
    	onResume();
    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);   	

    	food_type = "restaurant";
    	notification_status =  sharedPrefs.getBoolean("prefnotification", true);
    	food_radius = 200;
    	
    	String msg ="Loading suggestions...";
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    	
//        double mLatitude = mLocationClient.getLastLocation().getLatitude();
//        double mLongitude = mLocationClient.getLastLocation().getLongitude();
   
        double mLatitude = 40.6944;
    	double mLongitude = -73.9865;
    	
        Log.d("datastore","resto " + mLatitude + mLongitude);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(mLatitude, mLongitude)).zoom(15).build();
    	
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                
       
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location="+mLatitude+","+mLongitude);
        sb.append("&radius="+food_radius);
        sb.append("&types="+food_type.toLowerCase());
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyAFAFiOe4YPhXPuD9Vdq9Q29m5Iel_imJE");
        sb.append("&opennow=true");
        sb.append("&rankBy=prominence");

        // Creating a new non-ui thread task to download Google place json data
        PlacesTask placesTask = new PlacesTask(food_type.toLowerCase());

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());
        spinner.setVisibility(View.GONE);
    }   
    
   public void showtourismplaces(View view) throws IOException {
    	spinner.setVisibility(View.VISIBLE);
    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);   	

    	food_type = sharedPrefs.getString("food_type", "restaurant");
    	notification_status =  sharedPrefs.getBoolean("prefnotification", true);
    	food_radius = sharedPrefs.getInt("food_radius", 500);
    	
    	String msg ="Searching " + food_type + " within " + food_radius + " radius.";
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    	
        double mLatitude = mLocationClient.getLastLocation().getLatitude();
        double mLongitude = mLocationClient.getLastLocation().getLongitude();

//        mLatitude = 40.6944;
//        mLongitude = -73.9865;
        food_radius = 2000;
           
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(mLatitude, mLongitude)).zoom(15).build();
    	
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                
       
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location="+mLatitude+","+mLongitude);
        sb.append("&radius="+food_radius);
        sb.append("&types=museum|zoo|stadium|shopping_mall|amusement_park");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyAFAFiOe4YPhXPuD9Vdq9Q29m5Iel_imJE");

        // Creating a new non-ui thread task to download Google place json data
        PlacesTask placesTask = new PlacesTask("tourism");

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());
        spinner.setVisibility(View.GONE);
    } 
    
class MyInfoWindowAdapter implements InfoWindowAdapter{

        private View myContentsView;
        private View  eventContentsView;
  
  MyInfoWindowAdapter(){
   myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
  }
  
  @Override
  public View getInfoContents(Marker marker) {

	  if(! (marker.getTitle().equals("You're here")) ){
		  if(!(marker.getTitle().startsWith("Event : ")) ){
	
			   TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
			       tvTitle.setText(marker.getTitle());
		       
		        double lat = mLocationClient.getLastLocation().getLatitude();
		        double longi = mLocationClient.getLastLocation().getLongitude();
		
		       Double distance = cal_distance(lat, longi,marker.getPosition().latitude,marker.getPosition().longitude); 
		       DecimalFormat df=new DecimalFormat("0.00");
		       String geo_dist = df.format(distance); 
		       
		       
			   TextView tvDist = ((TextView)myContentsView.findViewById(R.id.dist));
			   		tvDist.setText(geo_dist + " mi");
			       
			    String snippet = null;
				String ratingstr;
				String photo_reference="";
				String now_open = "Now Closed";
				String types = null;
				try {
					String[] marker_data = marker.getSnippet().split("lifeapp#", 5);
					snippet = marker_data[0];
					ratingstr = marker_data[1];
					photo_reference =  marker_data[2];
					types = marker_data[3];
					
					if (marker_data[4].equals("true")){
						now_open = "Now Open";
					}
		
				} catch (Exception e) {
					ratingstr = "0";
					photo_reference = "";
					
				}
			    
			    TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
		        		 tvSnippet.setText(snippet);	            
		        TextView tvtypes = ((TextView)myContentsView.findViewById(R.id.types));
		        		 tvtypes.setText(types);	    
		        TextView tvnow_open = ((TextView)myContentsView.findViewById(R.id.open_status));
		        		 tvnow_open.setText(now_open);
		        		 tvnow_open.setTextColor(getResources().getColor(R.color.green));
		        		 if(now_open.equals("Now Closed")){
		        			tvnow_open.setTextColor(Color.RED);
		        		}
			            
		        // declare RatingBar object
		        RatingBar rating=(RatingBar)myContentsView.findViewById(R.id.place_rating);// create RatingBar object
		        if( !(ratingstr.equals("null")) ){
		            rating.setRating(Float.parseFloat(ratingstr));
		        }
		        
		//        if ( !(photo_reference.equals(""))){
		//	        String iconurl = photo_reference;      
		//	        
		//	        final DownloadImageTask download = new DownloadImageTask((ImageView)myContentsView.findViewById(R.id.marker_img) ,marker);
		//	        download.execute(iconurl);
		//        
		//	        
		//        }
		        
		      if ( !(photo_reference.equals(""))){
		        String iconurl = photo_reference; 
		        SmartImageView resto_img = (SmartImageView)myContentsView.findViewById(R.id.marker_img);
		        resto_img.setImageUrl(iconurl);  
		        
		    }
		      
		      Log.d("photo_reference",photo_reference);
		       
		         
		        return myContentsView;
		  }else{
			  
				   eventContentsView = getLayoutInflater().inflate(R.layout.eventcustom_info_contents, null);
				   TextView tvTitle = ((TextView)eventContentsView.findViewById(R.id.title));
			       tvTitle.setText(marker.getTitle());
			       
			       String [] getSnip = marker.getSnippet().split("lifeapp#");
				   String start_date = getSnip[1];
				   String venue = getSnip[3];
				   
				   TextView tvStartDate = ((TextView)eventContentsView.findViewById(R.id.Startdate));
				   tvStartDate.setText("Start Date : " + start_date);
				   
				   TextView tvvenue = ((TextView)eventContentsView.findViewById(R.id.venue));
				   tvvenue.setText("Venue : " + venue);
			  return eventContentsView;
		  }
		        
	}else{
		  return null;
	  }
	    
  }

  @Override
  public View getInfoWindow(Marker marker) {
   // TODO Auto-generated method stub
   return null;
  }
 }

@Override
public void onInfoWindowClick(Marker marker) {
	
	if (marker.getSnippet().split("lifeapp#")[3].toLowerCase().contains("museum")){
	    Intent intent = new Intent(LifeApp_Map.this, LifeApp_Map_Places.class);
	    intent.putExtra("title", marker.getTitle());
	    startActivity(intent);
		
	}else if(!(marker.getTitle().startsWith("Event : ")) ){
	    Intent intent = new Intent(LifeApp_Map.this, LifeApp_Map_resto.class);
	    intent.putExtra("title", marker.getTitle());
	    intent.putExtra("latitude", marker.getPosition().latitude);
	    intent.putExtra("longitude", marker.getPosition().longitude);
	    intent.putExtra("rating", marker.getSnippet().split("lifeapp#")[1]);
	    intent.putExtra("main_img", marker.getSnippet().split("lifeapp#")[2]);
	    intent.putExtra("address", marker.getSnippet().split("lifeapp#")[0]);
	    startActivity(intent);
	}else{
		
	    Intent intent = new Intent(LifeApp_Map.this, LifeApp_Map_Event.class);
	    intent.putExtra("title", marker.getTitle());
	    intent.putExtra("main_img", marker.getSnippet().split("lifeapp#")[0]);
	    intent.putExtra("start_date", marker.getSnippet().split("lifeapp#")[1]);	    
	    intent.putExtra("desc", marker.getSnippet().split("lifeapp#")[2]);
	    intent.putExtra("address", marker.getSnippet().split("lifeapp#")[3]);
	    startActivity(intent);
		
	}
	
}
	
	public double cal_distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		
		return (dist);
	}
	
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	
	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}


	@Override
	public void onBroadcastMessageReceived(List<CloudEntity> message) {
		// TODO Auto-generated method stub
		Log.d("ping","in onBroadcastMessageReceived ");
	}
 
    
}





/** A class, to download Google Places */
class PlacesTask extends AsyncTask<String, Integer, String>{

    String data = null;
    String food_type;
    
    public PlacesTask(String food_type) {
		// TODO Auto-generated constructor stub
    	this.food_type = food_type;
	}
    // Invoked by execute() method of this object
    @Override
    public String doInBackground(String... url) {
        try{
            data = downloadUrl(url[0]);
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
        return data;
    }

    public String downloadUrl(String strUrl) throws IOException {
    	String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
 
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
	}

	// Executed after the complete execution of doInBackground() method
    @Override
    public void onPostExecute(String result){
        ParserTask parserTask = new ParserTask(food_type);

        // Start parsing the Google places in JSON format
        // Invokes the "doInBackground()" method of the class ParseTask
        parserTask.execute(result);

    }
}

/** A class to parse the Google Places in JSON format */
class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

    JSONObject jObject;
    String type;
    

    public ParserTask() {
		// TODO Auto-generated constructor stub
	}

        
    public ParserTask(String food_type) {
		// TODO Auto-generated constructor stub
    	this.type = food_type;
	}

	// Invoked by execute() method of this object
    @Override
    public List<HashMap<String,String>> doInBackground(String... jsonData) {

        List<HashMap<String, String>> places = null;
        PlaceJSONParser placeJsonParser = new PlaceJSONParser();

        try{
            jObject = new JSONObject(jsonData[0]);

            /** Getting the parsed data as a List construct */
            places = placeJsonParser.parse(jObject);

        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
        return places;
    }

    // Executed after the complete execution of doInBackground() method
    @Override
    public void onPostExecute(List<HashMap<String,String>> list){
    	Log.d("mymap",Integer.toString(list.size()));
      
                      
        for(int i=0;i<list.size();i++){
        	try{
            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();

            // Getting a place from the places list
            HashMap<String, String> hmPlace = list.get(i);

            // Getting latitude of the place
            double lat = Double.parseDouble(hmPlace.get("lat"));

            // Getting longitude of the place
            double lng = Double.parseDouble(hmPlace.get("lng"));

            // Getting name
            String name = hmPlace.get("place_name");

            // Getting vicinity
            String vicinity = hmPlace.get("vicinity");
            String rating = hmPlace.get("rating");
            
            String preference = "";
            String photo_reference = hmPlace.get("photo_reference");
                        
            if ( !(photo_reference.equals(""))){
            	preference = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&photoreference=" + photo_reference 
            			     +"&sensor=true&key=AIzaSyAFAFiOe4YPhXPuD9Vdq9Q29m5Iel_imJE";
            }else{
            	Log.d("mymap",name + "--------" + photo_reference);
            }
            
            LatLng latLng = new LatLng(lat, lng);
            String types = hmPlace.get("types");
            if(type.equals("tourism")){
            	types = hmPlace.get("types").substring(0,types.length()-15);
            }	
            String now_open = hmPlace.get("now_open");
            
            // Setting the position for the marker
            markerOptions.position(latLng);

            // Setting the title for the marker.
            //This will be displayed on taping the marker
            markerOptions.title(name);
            markerOptions.snippet(vicinity + "lifeapp#" + rating + "lifeapp#" + preference+ "lifeapp#"+ types + "lifeapp#" + now_open);
            
            System.out.println(name + " : " + vicinity);
            // Placing a marker on the touched position
            
            
            // set marker images 
            if (type.equals("cafe")){
            	markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.coffee));
            }else if(type.equals("bar")){
            	markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bar));
        	}else if(type.equals("restaurant")){
            	markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant));
            }else if(type.equals("tourism")){
            	markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tourism_map1));
            }
            
            //MarkerOptions marker = new MarkerOptions().position(latLng).title(name);
            
            // adding marker
            
            
            if (LifeApp_Map.mMap == null) {
            	Log.d("mymap","Is null ------------------------------------");
            }else{
            	LifeApp_Map.mMap.addMarker(markerOptions);
            }

            //Log.d("places_log",preference);
            
            // Linking Marker id and place reference
            //mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));
        	}
        	catch(Exception e){
        		// do nothing       		
        	}
            
        }
    }
    
}



