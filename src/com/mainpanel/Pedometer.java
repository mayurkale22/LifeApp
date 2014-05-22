package com.mainpanel;

import com.szugyi.circlemenu.R;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.echo.holographlibrary.PieGraph.OnSliceClickedListener;


//import com.fitness.GetDirectionsAsyncTask;
import com.example.android.activityrecognition.*;
import com.example.android.activityrecognition.ActivityUtils.REQUEST_TYPE;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
/*
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
*/


//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.api.client.util.Value;
//import com.google.cloud.backend.R;
import com.google.cloud.backend.core.CloudBackendFragment;
import com.google.cloud.backend.core.CloudCallbackHandler;
import com.google.cloud.backend.core.CloudEntity;
import com.google.cloud.backend.core.CloudBackendFragment.OnListener;
import com.google.cloud.backend.core.CloudQuery.Order;
import com.google.cloud.backend.core.CloudQuery.Scope;

import android.app.Activity;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.preference.PreferenceManager;

public class Pedometer extends FragmentActivity implements OnListener,LocationListener{

	private static final String TAG = "Pedometer";
    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;
    private Utils mUtils;
    
    //private TextView mStepValueView;
    //private TextView mPaceValueView;
    //private TextView mDistanceValueView;
    //private TextView mSpeedValueView;
    //private TextView mCaloriesValueView;
    //TextView mDesiredPaceView;
    private int mStepValue;
    private int mPaceValue;
    private float mDistanceValue;
    private float mSpeedValue;
    private int mCaloriesValue;
    //private float mDesiredPaceOrSpeed;
    private int mMaintain;
    private boolean mIsMetric;
    private float mMaintainInc;
    private boolean mQuitting = false; // Set when user selected Quit from menu, can be used by onPause, onStop, onDestroy
    private boolean back_button_press=false;
    
    
    
    /**
     * True, when service is running.
     */
    private boolean mIsRunning;
    private boolean IsRunning_activity_recognition; //act rec service
		
    //activity recog code
    
    private static final int MAX_LOG_SIZE = 5000;

    // Instantiates a log file utility object, used to log status updates
    private LogFile mLogFile;

    // Store the current request type (ADD or REMOVE)
    private REQUEST_TYPE mRequestType;

    // Holds the ListView object in the UI
    private ListView mStatusListView;

    /*
     * Holds activity recognition data, in the form of
     * strings that can contain markup
     */
    private ArrayAdapter<Spanned> mStatusAdapter;

    /*
     *  Intent filter for incoming broadcasts from the
     *  IntentService.
     */
    IntentFilter mBroadcastFilter;

    // Instance of a local broadcast manager
    private LocalBroadcastManager mBroadcastManager;

    // The activity recognition update request object
    private DetectionRequester mDetectionRequester;

    // The activity recognition update removal object
    private DetectionRemover mDetectionRemover;
    
    //activity recognition code
    
    //mediaplayer
    MediaPlayer pop_sound;
    
 // Animation
    Animation animFadein;
    
    //map code
	private GoogleMap map;
	private MapFragment fragment;
	private LatLngBounds latlngBounds;
	private Button bNavigation;
	private Polyline newPolyline;
	private int width=1000, height=500;
	
	
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	
	private boolean onResume_flag=false;
	private String User_ID="3";
	private ProgressDialog progressDialog;
	private Context context;
	
	
	//cloud
	private int limit=1000;
	private String kindname_steps_total="Steps_Total";
	private String sortpropertyname_steps_total="Created_Date";
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		System.out.println("is running value "+mIsRunning);
			System.out.println("Transfer run flag "+Transfer.get_app_running_flag());
		
		Log.i(TAG, "[ACTIVITY] onCreate");		
		
        super.onCreate(savedInstanceState);
        
        mStepValue = 0;
        mPaceValue = 0;
          
        setContentView(R.layout.fragment_main);
        
        /*
        LinearLayout map_layout=((LinearLayout) findViewById(R.id.linear_layout_map));
        //map_layout.setVisibility(LinearLayout.GONE);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(100,100);
        map_layout.setLayoutParams(parms);
        
        LinearLayout linear_layout_bar_graph=((LinearLayout) findViewById(R.id.linear_layout_bar_graph));
        parms = new LinearLayout.LayoutParams(620,600);
        linear_layout_bar_graph.setLayoutParams(parms);
        */
        
        context = Pedometer.this;
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading data from datastore...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        
      //GPS service call
        if(!isGPSCheckServiceRunning())
        {
            System.out.println("Starting GPS service");
            Intent i = new Intent(Pedometer.this, GPSCheckService.class);
            //i.putExtra("name", "SurvivingwithAndroid");        
            Pedometer.this.startService(i); 
        }  
        
      //ActivityTimeCountService service call
        if(!isActivityTimeCountServiceRunning())
        {
            System.out.println("Starting ActivityTimeCount service");
            Intent i = new Intent(Pedometer.this, ActivityTimeCountService.class);
            //i.putExtra("name", "SurvivingwithAndroid");        
            Pedometer.this.startService(i); 
        }  
        
        if(Transfer.getGPSstatus()==0) {
        	//((TextView) findViewById(R.id.gps_status)).setText("GPS is disabled. Data Accuracy is poor.");;
        	//((TextView) findViewById(R.id.gps_status)).setTextColor(Color.RED);
        }
        else
        {
        	//((TextView) findViewById(R.id.gps_status)).setText("GPS : Active");;
        	//((TextView) findViewById(R.id.gps_status)).setTextColor(Color.WHITE);
        }
        
        
        //get location updates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
               
        System.out.println("map value "+fragment);
        
        
        //map code
           	fragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));	
           
        	System.out.println("fragment value ped : "+fragment.toString());
        
        map = fragment.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        
        //map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        //map.setMyLocationEnabled(true);
        //map.setIndoorEnabled(true);
                      
        
	        if(Transfer.getGPSstatus()==1) {
	        Location loc=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	        System.out.println("map value ped : "+map.toString());
	        
	        MarkerOptions m=new MarkerOptions();
	        m.position(new LatLng(loc.getLatitude(), loc.getLongitude()));
	    	m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
	    	map.addMarker(m);
	       
	        
	        //latlngBounds = createLatLngBoundsObject(AMSTERDAM,PARIS);
	        		
	        //map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
	        
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(
				    new LatLng(loc.getLatitude(), loc.getLongitude()), 18));
	        								}
        
        
        
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        
        //map code
        
        mUtils = Utils.getInstance();
		
		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
        
        IsRunning_activity_recognition=Transfer.getIsActivityRecognition();
        System.out.println("activity recog bool "+IsRunning_activity_recognition);
        
        //set activity text
        	/* TextView tv=(TextView) findViewById(R.id.live);
        	tv.setText("Current Activity : "
            		+Transfer.getCurrentactivity()+" "+Transfer.get_current_activity_confidence()+"%"
            		+"\nLastUpdated : "+Transfer.get_current_activity_timestamp()); */
        System.out.println("Current Activity : "+Transfer.getCurrentactivity()+" "+Transfer.get_current_activity_confidence()+"%"
        		+"\nLastUpdated : "+Transfer.get_current_activity_timestamp());
        
        
     // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.spring); 
        
        //if(!IsRunning_activity_recognition)
        //{
            //act recog code
            // Get a handle to the activity update list
               //mStatusListView = (ListView) findViewById(R.id.log_listview);

               // Instantiate an adapter to store update data from the log
        /*       
        mStatusAdapter = new ArrayAdapter<Spanned>(
                       this,
                       R.layout.item_layout,
                       R.id.log_text
               );
               */

               // Bind the adapter to the status list
               //mStatusListView.setAdapter(mStatusAdapter);

               // Set the broadcast receiver intent filer
               mBroadcastManager = LocalBroadcastManager.getInstance(this);

               // Create a new Intent filter for the broadcast receiver
               mBroadcastFilter = new IntentFilter(ActivityUtils.ACTION_REFRESH_STATUS_LIST);
               mBroadcastFilter.addCategory(ActivityUtils.CATEGORY_LOCATION_SERVICES);

               // Get detection requester and remover objects
               mDetectionRequester = new DetectionRequester(this);
               mDetectionRemover = new DetectionRemover(this);

               // Create a new LogFile object
               //mLogFile = LogFile.getInstance(this);
               //act recog code
        	
        	
        	System.out.println("Starting activity recognition engine....");
        	onStartUpdates();
        	Transfer.setIsActivityRecognition();
        	System.out.println("Activity recognition engine started");
       // }
        
        System.out.println("activity recog bool "+Transfer.getIsActivityRecognition());
        
        
             
             
        //app engine code
        System.out.println("mFragmentManager value"+mFragmentManager);
        
        mFragmentManager = getFragmentManager();

        initiateFragments();
        
        
        
        System.out.println("Step value after running retrive data from cloud "+mStepValue);
        
        
        //interactive button
        if(Transfer.getbtn_steps_dist_cal_flag()==0)
        	{
        	Button btn_steps_dist_cal=(Button) findViewById(R.id.btn_steps_dist_cal);
        	//btn_steps_dist_cal.startAnimation(animFadein);
        	Transfer.setbtn_steps_dist_cal_flag();
    		Spannable span = new SpannableString(mStepValue + "\n" +  "Steps");
	        span.setSpan(new AbsoluteSizeSpan(70), 0, (mStepValue+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        btn_steps_dist_cal.setText(span);
        	}
        	
        
      //interactive button2
        if(Transfer.getbtn_stepsmin_mileshr_flag()==0)
        	{
        	Button btn_stepsmin_mileshr=(Button) findViewById(R.id.btn_stepsmin_mileshr);
        	//btn_stepsmin_mileshr.startAnimation(animFadein);
        	Transfer.setbtn_stepsmin_mileshr_flag();
    		Spannable span = new SpannableString(mPaceValue + "\n" +  "steps/min");
	        span.setSpan(new AbsoluteSizeSpan(50), 0, (mPaceValue+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        btn_stepsmin_mileshr.setText(span);
        	}
        
      //interactive button3
        if(Transfer.get_btn_times_flag()==0)
        	{
        	Button btn_times=(Button) findViewById(R.id.btn_times);
        	//btn_times.startAnimation(animFadein);
        	Transfer.set_btn_times_flag();
    		Spannable span = new SpannableString(Transfer.getstilltime_string() + "\n" +  "still time");
	        span.setSpan(new AbsoluteSizeSpan(30), 0, (Transfer.getstilltime_string()+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        btn_times.setText(span);
        	}
        
        //mediaplayer initiate
        pop_sound = MediaPlayer.create(Pedometer.this, R.raw.blopsound);     
        
        Transfer.set_app_running_flag(true);

	}
	
	double max_latitude=0;
	double max_longitude=0;
	double min_latitude=0;
	double min_longitude=0;
	double prev_latitude=0;
	double prev_longitude=0;
	double current_latitude;
	double current_longitude;
	Marker marker;
	int count_markers=0;
	
	
	//location updates
	@Override
	public void onLocationChanged(Location location) {
	System.out.println("Onlocationchanged Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
	
	
	MarkerOptions mp = new MarkerOptions();
	
	
	
	if(marker!=null)
		{
		System.out.println("removing marker");
		marker.remove();	
		}
	
	
	current_latitude=location.getLatitude();
	current_longitude=location.getLongitude();
	//current_latitude=PARIS.latitude;
	//current_longitude=PARIS.longitude;
	
	mp.position(new LatLng(current_latitude, current_longitude));
	//mp.flat(true);
	//mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.purpledot));
	mp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
	mp.alpha(0.7f);
	marker = map.addMarker(mp);
	
	
	
	count_markers++;
	//((TextView) findViewById(R.id.step_units)).setText(count_markers+"");
	
	
	if(prev_latitude==0)
		{
			prev_latitude=current_latitude;
			prev_longitude=current_longitude;
			
			min_latitude=prev_latitude;
			max_latitude=prev_latitude;
			min_longitude=prev_longitude;
			max_longitude=prev_longitude;
			
			//prev_latitude= AMSTERDAM.latitude;
			//prev_longitude=AMSTERDAM.longitude;
			
			//map.animateCamera(CameraUpdateFactory.newLatLngZoom(
			  //  new LatLng(current_latitude, current_longitude), 18));
			
	        //latlngBounds = createLatLngBoundsObject(new LatLng(current_latitude, current_longitude),new LatLng(current_latitude, current_longitude));
			
	        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(current_latitude, current_longitude), 18));
		}
	else
		{
			PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.argb(180, 178, 102, 255));
			rectLine.add(new LatLng(current_latitude,current_longitude));
			rectLine.add(new LatLng(prev_latitude,prev_longitude));
			map.addPolyline(rectLine);
			
			//findDirections( prev_latitude,prev_longitude,current_latitude, current_longitude, GMapV2Direction.MODE_WALKING );
			
			//find largest bounds
			if(current_latitude>max_latitude)
				max_latitude=current_latitude;
			if(current_latitude<min_latitude)
				min_latitude=current_latitude;
			
			if(current_longitude>max_longitude)
				max_longitude=current_longitude;
			if(current_longitude<min_longitude)
				min_longitude=current_longitude;
			
			
			//map.animateCamera(CameraUpdateFactory.newLatLngZoom(
			//    new LatLng(current_latitude, current_longitude), 18));
			
		    latlngBounds = createLatLngBoundsObject(new LatLng(min_latitude, min_longitude),new LatLng(max_latitude, max_longitude));
		    		    
			map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, height, width, 150));
			
			prev_latitude=current_latitude;
			prev_longitude=current_longitude;
			
		}
	
	}
	
	@Override
	public void onProviderDisabled(String provider) {
	Log.d("Latitude","disable");
	}
	
	@Override
	public void onProviderEnabled(String provider) {
	Log.d("Latitude","enable");
	}
	 
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	Log.d("Latitude","status");
	}
	//location updates
	
	//map code
	private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation)
	{
		if (firstLocation != null && secondLocation != null)
		{
			LatLngBounds.Builder builder = new LatLngBounds.Builder();    
			builder.include(firstLocation).include(secondLocation);
			
			return builder.build();
		}
		return null;
	}
	
	private AsyncTask getdirections_async_task; 
	
	//map code
	public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);
		
		System.out.println("finddirections map values: "+map.toString());
		
		GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
		getdirections_async_task=asyncTask.execute(map);	
		
	}
	
	//map code
	public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
		
		System.out.println("inside handleGetDirectionsResult () ");
		PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);
		System.out.println("rectline value "+rectLine.toString());
		System.out.println("directionPoints value "+directionPoints.toString());
		System.out.println("directionPoints size value "+directionPoints.size());
		
		//rectLine.add(directionPoints.get(0));
		
		for(int i = 0 ; i < directionPoints.size() ; i++) 
		{          
			rectLine.add(directionPoints.get(i));
		}
		if (newPolyline != null)
		{
			newPolyline.remove();
		}
		newPolyline = map.addPolyline(rectLine);

		
		//if(prev_latitude==0)
			//latlngBounds = createLatLngBoundsObject(AMSTERDAM, PARIS);
		//else
			latlngBounds = createLatLngBoundsObject(new LatLng(prev_latitude,prev_longitude), new LatLng(current_latitude,current_longitude));
        //map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
		
	}
	
	
	
	private boolean isGPSCheckServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (GPSCheckService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}	
	
	private boolean isActivityTimeCountServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (ActivityTimeCountService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}	
	
	//gps notify user
	private void buildAlertMessageNoGps() {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                    dialog.cancel();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
	//gps notify user
	
	
	/*
     * Handle results returned to this Activity by other Activities started with
     * startActivityForResult(). In particular, the method onConnectionFailed() in
     * DetectionRemover and DetectionRequester may call startResolutionForResult() to
     * start an Activity that handles Google Play services problems. The result of this
     * call returns here, to onActivityResult.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case ActivityUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        // If the request was to start activity recognition updates
                        if (ActivityUtils.REQUEST_TYPE.ADD == mRequestType) {

                            // Restart the process of requesting activity recognition updates
                            mDetectionRequester.requestUpdates();

                        // If the request was to remove activity recognition updates
                        } else if (ActivityUtils.REQUEST_TYPE.REMOVE == mRequestType ){

                                /*
                                 * Restart the removal of all activity recognition updates for the 
                                 * PendingIntent.
                                 */
                                mDetectionRemover.removeUpdates(
                                    mDetectionRequester.getRequestPendingIntent());

                        }
                    break;

                    // If any other result was returned by Google Play services
                    default:

                        // Report that Google Play services was unable to resolve the problem.
                        Log.d(ActivityUtils.APPTAG, getString(R.string.no_resolution));
                }

            // If any other request code was received
            default:
               // Report that this Activity received an unknown requestCode
               Log.d(ActivityUtils.APPTAG,
                       getString(R.string.unknown_activity_request_code, requestCode));

               break;
        }
    }
	
	
	 @Override
	    protected void onStart() {
	        Log.i(TAG, "[ACTIVITY] onStart");
	        super.onStart();
	        
	    }
	 
	 /*
	 @Override
	    protected void onStop() {
	        Log.i(TAG, "[ACTIVITY] onStop");
	        super.onStop();
	        
	    }
	 */
	 
	 //check if back button is pressed
	 public boolean onKeyDown(int keyCode, KeyEvent event)
	 {

	     if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)
	     {
	    	 moveTaskToBack(true);
	    	 System.out.println("back button / home button was pressed");
	    	 back_button_press=true;
	         
	     }
	     
	     return super.onKeyDown(keyCode, event);
	 }
	 
	 protected void quitActivityRecognition()
	 {
		 stopService(new Intent(Pedometer.this,
	        		ActivityRecognitionIntentService.class));
	        
	     // Check for Google Play services
	        if (!servicesConnected()) {

	            return;
	        }

	        /*
	         * Set the request type. If a connection error occurs, and Google Play services can
	         * handle it, then onActivityResult will use the request type to retry the request
	         */
	        mRequestType = ActivityUtils.REQUEST_TYPE.REMOVE;

	        // Pass the remove request to the remover object
	        mDetectionRemover.removeUpdates(mDetectionRequester.getRequestPendingIntent());

	        /*
	         * Cancel the PendingIntent. Even if the removal request fails, canceling the PendingIntent
	         * will stop the updates.
	         */
	        mDetectionRequester.getRequestPendingIntent().cancel();
	 }
	 
	 protected void terminate_GPS_Service()
	 {
		//terminate GPS service
	        System.out.println("terminating GPS service");
	        Intent i = new Intent(Pedometer.this, GPSCheckService.class);
	        Pedometer.this.stopService(i);
	 }
	 
	 protected void terminate_ActivityTimeCountService()
	 {
		//terminate GPS service
	        System.out.println("terminating ActivityTimeCount service");
	        Intent i = new Intent(Pedometer.this, ActivityTimeCountService.class);
	        Pedometer.this.stopService(i);
	 }
	 
	 protected void start_ActivityTimeCountService()
	 {
		//terminate GPS service
	        System.out.println("terminating ActivityTimeCount service");
	        Intent i = new Intent(Pedometer.this, ActivityTimeCountService.class);
	        Pedometer.this.startService(i);

	 }
	 
	 protected void stop_location_updates()
	 {
		//terminate GPS service
	        System.out.println("stopping location updates");
	        locationManager.removeUpdates(this);
	        locationManager=null;
	 }
	 
	 protected void stop_getdirectionsasynctask()
	 {
			//terminate GPS service
		        //System.out.println("stopping getdirectionsasynctask status : "+getdirections_async_task.getStatus());
		        
		        
		        //getdirections_async_task.cancel(true);
		 }
	 
	 @Override
	    protected void onDestroy() {
	        Log.i(TAG, "[ACTIVITY] onDestroy");
	        
	        
	        if(back_button_press==false && mQuitting!=true)
	        {
	        	System.out.println("application destroyed. sending values to google datastore");
	        	store_fitness_data_in_cloud();
		    	System.out.println("application destroyed. values sent to google datastore");
		    	
		    	terminate_GPS_Service();
		    	terminate_ActivityTimeCountService();
		    	stop_location_updates();
		    	stop_getdirectionsasynctask();
		    	
		    	Transfer.set_app_running_flag(false);
		    	//onStopUpdates();
	        }
	        
	        
	        //to handle destruction of app
	        back_button_press=false;
	        
	        pop_sound.release();        
	        
	        
	        LocalBroadcastManager.getInstance(this).unregisterReceiver(updatebtntimesReceiver);
	        
	        super.onDestroy();
	        
	        
	        
	        
	      
	    }
	 
	 
	 @Override
	    protected void onResume() {
	        Log.i(TAG, "[ACTIVITY] onResume");
	        
	        
	        
	        super.onResume();
	        
	        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
	        mPedometerSettings = new PedometerSettings(mSettings);
	        
	        //((TextView) findViewById(R.id.weight_kgs)).setText(String.valueOf(mSettings.getString("body_weight", "50").trim())+" kgs");
	        
	        mUtils.setSpeak(mSettings.getBoolean("speak", false));
	        
	        // Read from preferences if the service was running on the last onPause
	        mIsRunning = mPedometerSettings.isServiceRunning();
	        
	        // Start the service if this is considered to be an application start (last onPause was long ago)
	        if (!mIsRunning && mPedometerSettings.isNewStart()) {
	            startStepService();
	            bindStepService();
	        }
	        else if (mIsRunning) {
	            bindStepService();
	        }
	        
	        mPedometerSettings.clearServiceRunning();

	        
	        
	        //from top
	        // Register the broadcast receiver
	           mBroadcastManager.registerReceiver( //--commented on 01/23/2014 12:02 PM
	                   updateListReceiver,
	                   mBroadcastFilter);
	           
	           LocalBroadcastManager.getInstance(this).registerReceiver((updatebtntimesReceiver), new IntentFilter(ActivityTimeCountService.COPA_RESULT));
	           
	               
	        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

	        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	            //buildAlertMessageNoGps();
	        	System.out.println("GPS is not on");
	        	//((TextView) findViewById(R.id.gps_status)).setText("GPS is disabled. Data Accuracy is poor.");;
	        	//((TextView) findViewById(R.id.gps_status)).setTextColor(Color.RED);
	        }
	        else
	        {
	        	System.out.println("GPS is on");
	        	//((TextView) findViewById(R.id.gps_status)).setText("GPS : Active");;
	        	//((TextView) findViewById(R.id.gps_status)).setTextColor(Color.WHITE);
	        }
	        
	        //displayDesiredPaceOrSpeed();
	        
	    }
	    
	 

	    private StepService mService;
	    
	    private ServiceConnection mConnection = new ServiceConnection() {
	        public void onServiceConnected(ComponentName className, IBinder service) {
	            mService = ((StepService.StepBinder)service).getService();

	            mService.registerCallback(mCallback);
	            mService.reloadSettings();
	            
	        }

	        public void onServiceDisconnected(ComponentName className) {
	            mService = null;
	        }
	    };
	    

	    private void startStepService() {
	        if (! mIsRunning) {
	            Log.i(TAG, "[SERVICE] Start");
	            mIsRunning = true;
	            startService(new Intent(Pedometer.this,
	                    StepService.class));
	        }
	    }
	    
	    private void bindStepService() {
	        Log.i(TAG, "[SERVICE] Bind");
	        bindService(new Intent(Pedometer.this, 
	                StepService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
	    }

	    private void unbindStepService() {
	        Log.i(TAG, "[SERVICE] Unbind");
	        unbindService(mConnection);
	    }
	    
	    private void stopStepService() {
	        Log.i(TAG, "[SERVICE] Stop");
	        if (mService != null) {
	            Log.i(TAG, "[SERVICE] stopService");
	            stopService(new Intent(Pedometer.this,
	                  StepService.class));
	        }
	        mIsRunning = false;
	    }
	    
	    private void resetValues(boolean updateDisplay) {
	    	
	    	System.out.println("reset button clicked. sending values to google datastore");
	    	//sync code to be put
	    	System.out.println("reset button clicked. values sent to google datastore");
	    	
	        if (mService != null && mIsRunning) {
	            mService.resetValues();         
	            System.out.println("hello1");
	        }
	        else {
	        	System.out.println("hello2");
	            //mStepValueView.setText("0");
	            //mPaceValueView.setText("0");
	            //mDistanceValueView.setText("0");
	            //mSpeedValueView.setText("0");
	            //mCaloriesValueView.setText("0");
	            SharedPreferences state = getSharedPreferences("state", 0);
	            SharedPreferences.Editor stateEditor = state.edit();
	            if (updateDisplay) {
	                //stateEditor.putInt("steps", 0);
	                //stateEditor.putInt("pace", 0);
	                //stateEditor.putFloat("distance", 0);
	                //stateEditor.putFloat("speed", 0);
	                //stateEditor.putFloat("calories", 0);
	                //stateEditor.commit();
	            }
	        }
	        
		      //button 3 reset values
	        Transfer.setstilltime(0);
	    	Transfer.setonfoottime(0);
	    	Transfer.setbicycletime(0);
	    	Transfer.setvehicle_time(0);
	    	
	    	Transfer.set_mStepValue_from_datastore(0);
	    	Transfer.set_mDistanceValue_from_datastore(0);
	    	Transfer.set_mCaloriesValue_from_datastore(0);
	    	
	    	mStepValue=0;
	    	mDistanceValue=0;
	    	mCaloriesValue=0;
	    	mSpeedValue=0;
	    	mPaceValue=0;
	    	
	    	if(updateDisplay)
	    	{
	    		btn_times_refresh();
	    		btn_steps_dist_cal_refresh();
	    		btn_stepsmin_mileshr_refresh();
	    	}
	    }
	    

	    private static final int MENU_SETTINGS = 8;
	    private static final int MENU_QUIT     = 9;

	    private static final int MENU_PAUSE = 1;
	    private static final int MENU_RESUME = 2;
	    private static final int MENU_RESET = 3;
	    
	    /* Creates the menu items */
	    public boolean onPrepareOptionsMenu(Menu menu) {
	        menu.clear();
	        if (mIsRunning) {
	            menu.add(0, MENU_PAUSE, 0, R.string.pause)
	            .setIcon(android.R.drawable.ic_media_pause)
	            .setShortcut('1', 'p');
	        }
	        else {
	            menu.add(0, MENU_RESUME, 0, R.string.resume)
	            .setIcon(android.R.drawable.ic_media_play)
	            .setShortcut('1', 'p');
	        }
	        menu.add(0, MENU_RESET, 0, R.string.reset)
	        .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
	        .setShortcut('2', 'r');
	        menu.add(0, MENU_SETTINGS, 0, R.string.settings)
	        .setIcon(android.R.drawable.ic_menu_preferences)
	        .setShortcut('8', 's')
	        .setIntent(new Intent(this, Settings.class));
	        menu.add(0, MENU_QUIT, 0, R.string.quit)
	        .setIcon(android.R.drawable.ic_lock_power_off)
	        .setShortcut('9', 'q');
	        return true;
	    }

	    /* Handles item selections */
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case MENU_PAUSE:
	                unbindStepService();
	                stopStepService();
	                terminate_ActivityTimeCountService();
	                return true;
	            case MENU_RESUME:
	                startStepService();
	                bindStepService();
	                start_ActivityTimeCountService();
	                return true;
	            case MENU_RESET:
	            	onclickupdategraph_flag=true;
	            	store_fitness_data_in_cloud();
	            	terminate_ActivityTimeCountService();
	                resetValues(true);
	                start_ActivityTimeCountService();
	                //retrieve_fitness_data_from_cloud();
	                populate_time_hash();
	                return true;
	            case MENU_QUIT:
	            	store_fitness_data_in_cloud();
	                resetValues(false);
	                unbindStepService();
	                stopStepService();
	                quitActivityRecognition();
	                terminate_GPS_Service();
	                terminate_ActivityTimeCountService();
	                stop_location_updates();
	                stop_getdirectionsasynctask();
	                Transfer.set_app_running_flag(false);
	                //onStopUpdates();
	                mQuitting = true;
	                finish();
	                return true;
	        }
	        return false;
	    }
	 
	    // TODO: unite all into 1 type of message
	    private StepService.ICallback mCallback = new StepService.ICallback() {
	        public void stepsChanged(int value) {
	            mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0));
	        }
	        public void paceChanged(int value) {
	            mHandler.sendMessage(mHandler.obtainMessage(PACE_MSG, value, 0));
	        }
	        public void distanceChanged(float value) {
	            mHandler.sendMessage(mHandler.obtainMessage(DISTANCE_MSG, (int)(value*1000), 0));
	        }
	        public void speedChanged(float value) {
	            mHandler.sendMessage(mHandler.obtainMessage(SPEED_MSG, (int)(value*1000), 0));
	        }
	        public void caloriesChanged(float value) {
	            mHandler.sendMessage(mHandler.obtainMessage(CALORIES_MSG, (int)(value), 0));
	        }
	    };
	    
	    private static final int STEPS_MSG = 1;
	    private static final int PACE_MSG = 2;
	    private static final int DISTANCE_MSG = 3;
	    private static final int SPEED_MSG = 4;
	    private static final int CALORIES_MSG = 5;
	    
	    private Handler mHandler = new Handler() {
	        @Override public void handleMessage(Message msg) {
	            switch (msg.what) {
	                case STEPS_MSG:
	                    mStepValue = (int)msg.arg1+Transfer.get_mStepValue_from_datastore();
	                    //mStepValueView.setText("" + mStepValue);
	                    btn_steps_dist_cal_refresh();
	                    break;
	                case PACE_MSG:
	                    mPaceValue = msg.arg1;
	                    if (mPaceValue <= 0) { 
	                        //mPaceValueView.setText("0");
	                    }
	                    else {
	                        //mPaceValueView.setText("" + (int)mPaceValue);
	                    }
	                    btn_stepsmin_mileshr_refresh();
	                    break;
	                case DISTANCE_MSG:
	                    mDistanceValue = (((int)msg.arg1)/1000f)+(Transfer.get_mDistanceValue_from_datastore());
	                    System.out.println("DISTANCE_MSG : "+msg.arg1);
	                    System.out.println("DISTANCE_MSG : msg.arg1/1000f "+(((int)msg.arg1)/1000f));
	                    System.out.println("DISTANCE_MSG distance value : "+mDistanceValue);
	                    if (mDistanceValue <= 0) { 
	                        //mDistanceValueView.setText("0");
	                    	System.out.println("mDistanceValue <=0 : "+mDistanceValue);
	                    }
	                    else {
	                        //mDistanceValueView.setText(("" + (mDistanceValue + 0.000001f)).substring(0, 5));
	                    }
	                    btn_steps_dist_cal_refresh();
	                    break;
	                case SPEED_MSG:
	                    mSpeedValue = ((int)msg.arg1)/1000f;
	                    if (mSpeedValue <= 0) { 
	                        //mSpeedValueView.setText("0");
	                    }
	                    else {
	                        //mSpeedValueView.setText(("" + (mSpeedValue + 0.000001f)).substring(0, 4));
	                    }
	                    btn_stepsmin_mileshr_refresh();
	                    break;
	                case CALORIES_MSG:
	                    mCaloriesValue = msg.arg1 + Transfer.get_mCaloriesValue_from_datastore();
	                    if (mCaloriesValue <= 0) { 
	                        //mCaloriesValueView.setText("0");
	                    }
	                    else {
	                        //mCaloriesValueView.setText("" + (int)mCaloriesValue);
	                    }
	                    btn_steps_dist_cal_refresh();
	                    break;
	                default:
	                    super.handleMessage(msg);
	            }
	            
	            
	            
	        }
	        
	    };
	    
	    
	    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	*/
	
	
	
	/*
     * Handle selections from the menu
     */
    /*
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {

            // Clear the log display and remove the log files
            case R.id.menu_item_clearlog:
                // Clear the list adapter
                mStatusAdapter.clear();

                // Update the ListView from the empty adapter
                mStatusAdapter.notifyDataSetChanged();

                // Remove log files
                if (!mLogFile.removeLogFiles()) {
                    Log.e(ActivityUtils.APPTAG, getString(R.string.log_file_deletion_error));

                // Display the results to the user
                } else {

                    Toast.makeText(
                            this,
                            R.string.logs_deleted,
                            Toast.LENGTH_LONG).show();
                }
                // Continue by passing true to the menu handler
                return true;

            // Display the update log
            case R.id.menu_item_showlog:

                // Update the ListView from log files
                updateActivityHistory();

                // Continue by passing true to the menu handler
                return true;

            // For any other choice, pass it to the super()
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */

    /*
     * Unregister the receiver during a pause
     */
	
    @Override
    protected void onPause() {

    	Log.i(TAG, "[ACTIVITY] onPause");
        if (mIsRunning) {
            unbindStepService();
        }
        if (mQuitting) {
            mPedometerSettings.saveServiceRunningWithNullTimestamp(mIsRunning);
        }
        else {
            mPedometerSettings.saveServiceRunningWithTimestamp(mIsRunning);
        }

        super.onPause();
        //savePaceSetting();
    	
    	
        // Stop listening to broadcasts when the Activity isn't visible.
        //mBroadcastManager.unregisterReceiver(updateListReceiver);

        //super.onPause();
        
    }
	

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {

            // In debug mode, log the status
            Log.d(ActivityUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;

        // Google Play services was not available for some reason
        } else {

            // Display an error dialog
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
            return false;
        }
    }
    /**
     * Respond to "Start" button by requesting activity recognition
     * updates.
     * @param view The view that triggered this method.
     */
    public void onStartUpdates() {

        // Check for Google Play services
        if (!servicesConnected()) {

            return;
        }

        /*
         * Set the request type. If a connection error occurs, and Google Play services can
         * handle it, then onActivityResult will use the request type to retry the request
         */
        mRequestType = ActivityUtils.REQUEST_TYPE.ADD;

        // Pass the update request to the requester object
        mDetectionRequester.requestUpdates();
    }
    
    
    /**
     * Respond to "Stop" button by canceling updates.
     * @param view The view that triggered this method.
     */
    public void onStopUpdates() {

        // Check for Google Play services
        if (!servicesConnected()) {

            return;
        }

        /*
         * Set the request type. If a connection error occurs, and Google Play services can
         * handle it, then onActivityResult will use the request type to retry the request
         */
        mRequestType = ActivityUtils.REQUEST_TYPE.REMOVE;

        // Pass the remove request to the remover object
        mDetectionRemover.removeUpdates(mDetectionRequester.getRequestPendingIntent());

        /*
         * Cancel the PendingIntent. Even if the removal request fails, canceling the PendingIntent
         * will stop the updates.
         */
        mDetectionRequester.getRequestPendingIntent().cancel();
    		}


    /**
     * Display the activity detection history stored in the
     * log file
     */
    private void updateActivityHistory() {

    	
    	System.out.println("updateActivityHistory() Current Activity : "
        		+Transfer.getCurrentactivity()+" "+Transfer.get_current_activity_confidence()+"%"
        		+"\nLastUpdated : "+Transfer.get_current_activity_timestamp());
    	
    }
            
    
            /**
             * Broadcast receiver that receives activity update intents
             * It checks to see if the ListView contains items. If it
             * doesn't, it pulls in history.
             * This receiver is local only. It can't read broadcast Intents from other apps.
             */
            BroadcastReceiver updateListReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    /*
                     * When an Intent is received from the update listener IntentService, update
                     * the displayed log.
                     */
                		//intent.putExtra("test", "");

	                	System.out.println("Running onReceive function of updatelist");
	                    updateActivityHistory();
	                    

                }
            };
            
            BroadcastReceiver updatebtntimesReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    /*
                     * When an Intent is received from the update listener IntentService, update
                     * the displayed log.
                     */
                		//intent.putExtra("test", "");
                	
                	//code to change display of button when value is updated
                	//Transfer.btn_times_flag=Integer.parseInt(intent.getCharSequenceExtra("activity")+"");
                	
	                	System.out.println("Running onReceive function of btntimes");
	                    btn_times_refresh();

                }
            };
            
        
            private static final int INTRO_ACTIVITY_REQUEST_CODE = 1;

            private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT";

            public static final String GUESTBOOK_SHARED_PREFS = "GUESTBOOK_SHARED_PREFS";
            public static final String SHOW_INTRO_PREFS_KEY = "SHOW_INTRO_PREFS_KEY";
            public static final String SCOPE_PREFS_KEY = "SCOPE_PREFS_KEY";
            private ImageView mSendBtn;
            private FragmentManager mFragmentManager;
            private CloudBackendFragment mProcessingFragment;
                    
            
     /**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
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
            Log.d("datastore","initiateFragments" + mProcessingFragment);
            
            //retrieve_data_from_cloud();
    }
	
	 private void handleEndpointException(IOException e) {
	    	Log.d("datastore","handleEndpointException");
	        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
	        mSendBtn.setEnabled(true);
	    }

		@Override
		public void onBroadcastMessageReceived(List<CloudEntity> message) {
			// TODO Auto-generated method stub
			Log.d("datastore","onBroadcastMessageReceived");
		}
		
		
		/**
         * Method called via OnListener in {@link CloudBackendFragment}.
         */
        @Override
        public void onCreateFinished() {
        	Log.d("datastore","onCreateFinished");
        	        	
        	retrieve_fitness_data_from_cloud();
        	//retrieve_fitness_data_from_cloud_test();
        	//for graph
        	populate_time_hash();
        }
		
        
        private void store_fitness_data_in_cloud() 
		{
			// create a CloudEntity with the new post
        	
			System.out.println("inside store_fitness_data_in_cloud function");
			
	    	CloudEntity newPost = new CloudEntity("Fitness");
	    	
	    	//temp
	    	//Calendar today = Calendar.getInstance();  
	    	//today.add(Calendar.DATE, -3); 
	    	//Date yesterday = new Date(today.getTimeInMillis());  
	    	
	    	newPost.put("User_ID", User_ID);	
	        newPost.put("Steps_Total", mStepValue+"");
	        newPost.put("Distance_Total", mDistanceValue+"");
	        newPost.put("Calories_Total", mCaloriesValue+"");
	        newPost.put("Still_Time_Total", Transfer.getstilltime());
	        newPost.put("On_Foot_Time_Total", Transfer.getonfoottime());
	        newPost.put("Vehicle_Time_Total", Transfer.getvehicle_time());
	        newPost.put("Bicyle_Time_Total", Transfer.getbicycletime());
	        newPost.put("Created_Date", (new Date())+"");
	        
			
			
	    	//temp
	    	//Calendar today = Calendar.getInstance();  
	    	//today.add(Calendar.DATE, -3); 
	    	//Date yesterday = new Date(today.getTimeInMillis());  
	        

	        // create a response handler that will receive the result or an error
	        CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
	            @Override
	            public void onComplete(final CloudEntity result) {
		                Log.d("mymap","complete");
		                mPosts.add(0, result);
		                
		                if(onclickupdategraph_flag==true)
		                	{
		                	//retrieve_fitness_data_from_cloud();
		                	populate_time_hash();
		                	onclickupdategraph_flag=false;
		                	}
		                
	            }

	            @Override
	            public void onError(final IOException exception) {
	            	
	            	System.out.println("inside store_steps_data_in_cloud exception");
	                handleEndpointException(exception);
	            }
	        };

	        Log.d("mymap","PF : " + mProcessingFragment);
	        // execute the insertion with the handler
	        mProcessingFragment.getCloudBackend().update(newPost, handler);
		}
		
        
        private void store_data(String a,String b,String c,String d,String e,String f,String g,String h,String i) 
		{
			// create a CloudEntity with the new post
	    	
			System.out.println("inside store_fitness_data_in_cloud function");
			
	    	CloudEntity newPost = new CloudEntity("Fitness");
	    	
	    	//temp
	    	//Calendar today = Calendar.getInstance();  
	    	//today.add(Calendar.DATE, -3); 
	    	//Date yesterday = new Date(today.getTimeInMillis());  
	    	
	    	newPost.put("User_ID", a);	
	        newPost.put("Steps_Total", b);
	        newPost.put("Distance_Total", c);
	        newPost.put("Calories_Total", d);
	        newPost.put("Still_Time_Total", e);
	        newPost.put("On_Foot_Time_Total", f);
	        newPost.put("Vehicle_Time_Total", g);
	        newPost.put("Bicyle_Time_Total", h);
	        newPost.put("Created_Date", i);
	        
			
			
	    	//temp
	    	//Calendar today = Calendar.getInstance();  
	    	//today.add(Calendar.DATE, -3); 
	    	//Date yesterday = new Date(today.getTimeInMillis());  
	        

	        // create a response handler that will receive the result or an error
	        CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
	            @Override
	            public void onComplete(final CloudEntity result) {
		                Log.d("mymap","complete");
		                mPosts.add(0, result);
		                
		                if(onclickupdategraph_flag==true)
		                	{
		                	//retrieve_fitness_data_from_cloud();
		                	populate_time_hash();
		                	onclickupdategraph_flag=false;
		                	}
		                
	            }

	            @Override
	            public void onError(final IOException exception) {
	            	
	            	System.out.println("inside store_steps_data_in_cloud exception");
	                handleEndpointException(exception);
	            }
	        };

	        Log.d("mymap","PF : " + mProcessingFragment);
	        // execute the insertion with the handler
	        mProcessingFragment.getCloudBackend().update(newPost, handler);
		}
        
		
		private SimpleDateFormat formatter= new SimpleDateFormat("MM/dd/yyyy");;
		private String curr_date=formatter.format(new Date());
		
		private void retrieve_fitness_data_from_cloud() {
			
			System.out.println("inside retrieve_fitness_data_from_cloud function");
			
			
	        // create a response handler that will receive the result or an error
	        CloudCallbackHandler<List<CloudEntity>> handler =
	                new CloudCallbackHandler<List<CloudEntity>>() {
	                    @Override
	                    public void onComplete(List<CloudEntity> results) {
	                        //mAnnounceTxt.setText(R.string.announce_success);
	                        //mPosts = results;
	                        //System.out.println("MPOSTS value : "+mPosts.toString());
	                        //mPosts.
	                        //animateArrival();
	                        //updateGuestbookView();
	                        
	                        System.out.println("inside cloudentity results before for loop");
	                        
	                        for (CloudEntity temp : results) {
	                        	
	                        	System.out.println("inside cloudentity results for loop");
	                        	//String created_date=formatter.format(temp.getCreatedAt());
	                        	
	                        	
	                        	//d.parse(string);
	                        	String created_date=formatter.format(Date.parse((temp.getProperties().get("Created_Date").toString()))); 
	                        	//String created_date=formatter.format(new Date);
	                        	
	                        	System.out.println("curr_date : "+curr_date+" and "+"created_date : "+created_date+" "+temp.getCreatedAt());
	                        	
	                        	  //System.out.println("GET PROPERTIES : STEPS "+temp.getProperties().get("Steps_Total"));
	                        	  //System.out.println("GET PROPERTIES : _createdAt "+temp.getCreatedAt());
	                        	  if(temp.getProperties().get("User_ID").equals(User_ID) && curr_date.equals(created_date))
	                        		  { 
	                        		  
	                        		  System.out.println("inside cloudentity results if condition");
	                        		  
	                        		  Transfer.set_mStepValue_from_datastore(Integer.parseInt(temp.getProperties().get("Steps_Total").toString()));
	                        		  mStepValue=Transfer.get_mStepValue_from_datastore();
	                        		  System.out.println("GET PROPERTIES : STEPS "+Transfer.get_mStepValue_from_datastore());
	                        		  
	                        		  
	                        		  Transfer.set_mDistanceValue_from_datastore(Float.parseFloat(temp.getProperties().get("Distance_Total").toString()));
	                        		  mDistanceValue=Transfer.get_mDistanceValue_from_datastore();
	                        		  System.out.println("GET PROPERTIES : DISTANCE "+Transfer.get_mDistanceValue_from_datastore());
	                        		  
	                        		  Transfer.set_mCaloriesValue_from_datastore(Integer.parseInt(temp.getProperties().get("Calories_Total").toString()));
	                        		  mCaloriesValue=Transfer.get_mCaloriesValue_from_datastore();
	                        		  System.out.println("GET PROPERTIES : CALORIES "+Transfer.get_mCaloriesValue_from_datastore());
	                        		  
	                        		  Transfer.setstilltime(Long.parseLong(temp.getProperties().get("Still_Time_Total")+""));
	                        		  System.out.println("GET PROPERTIES : STILL TIME "+Transfer.getstilltime());
	                        		  
	                        		  Transfer.setonfoottime(Long.parseLong(temp.getProperties().get("On_Foot_Time_Total")+""));
	                        		  System.out.println("GET PROPERTIES : ON FOOT TIME "+Transfer.getonfoottime());
	                        		  	                       		  
	                        		  }
	                        	}
	                        btn_steps_dist_cal_refresh();
	                        btn_times_refresh();
	                        progressDialog.dismiss();
	                        
	                    }

	                    @Override
	                    public void onError(IOException exception) {
	                        //mAnnounceTxt.setText(R.string.announce_fail);
	                        //animateArrival();
	                    	System.out.println("inside retrieve_fitness_data_from_cloud exception "+exception.toString());
	                        handleEndpointException(exception);
	                    }
	                };

	             // execute the query with the handler
	    	        mProcessingFragment.getCloudBackend().listByKind(
	    	                "Fitness", CloudEntity.PROP_CREATED_AT, Order.ASC, 100,
	    	                Scope.FUTURE_AND_PAST, handler);
	        
	        //System.out.println("MPOSTS value : "+mPosts.toString());

	    }
		
		
		private void retrieve_fitness_data_from_cloud_test() {
			System.out.println("inside retrieve_fitness_data_from_test_cloud function");
			
			
	        // create a response handler that will receive the result or an error
	        CloudCallbackHandler<List<CloudEntity>> handler =
	                new CloudCallbackHandler<List<CloudEntity>>() {
	                    @Override
	                    public void onComplete(List<CloudEntity> results) {
	                        //mAnnounceTxt.setText(R.string.announce_success);
	                        mPosts = results;
	                        System.out.println("MPOSTS value : "+mPosts.toString());
	                        //mPosts.
	                        //animateArrival();
	                        //updateGuestbookView();
	                        
	                        for (CloudEntity temp : results) {
	                        	
	                        	System.out.println("inside cloudentity results for loop");
	                        	//String created_date=formatter.format(temp.getCreatedAt());
	                        	      	  
	                        		  
	                        		  System.out.println("retrieve_fitness_data_from_cloud_test values : "+(temp.getProperties().get("User_ID")+""));
	                        		  	                        		  
	                        		  
	                        	}
	                        
	                        progressDialog.dismiss();
	                        
	                    }

	                    @Override
	                    public void onError(IOException exception) {
	                        //mAnnounceTxt.setText(R.string.announce_fail);
	                        //animateArrival();
	                    	System.out.println("inside retrieve_fitness_data_from_test_cloud exception");
	                        handleEndpointException(exception);
	                    }
	                };

	        // execute the query with the handler
	        mProcessingFragment.getCloudBackend().listByKind(
	                "Fitness_Datastore", CloudEntity.PROP_CREATED_AT, Order.ASC, 50,
	                Scope.FUTURE_AND_PAST, handler);
	        
	        //System.out.println("MPOSTS value : "+mPosts.toString());

	    }
		
		
		
		private Hashtable<String,Long> still_time_hash=new Hashtable<String,Long>();
		private Hashtable<String,Long> on_foot_time_hash=new Hashtable<String,Long>();
		private Hashtable<String,Long> vehicle_time_hash=new Hashtable<String,Long>();
		private Hashtable<String,Long> bicycle_time_hash=new Hashtable<String,Long>();
		private Hashtable<String,Integer> steps_total_hash=new Hashtable<String,Integer>();
		private TreeMap<String,Integer> steps_total_treemap=new TreeMap<String,Integer>();
		private Hashtable<String,Integer> calories_total_hash=new Hashtable<String,Integer>();
		private TreeMap<String,Integer> calories_total_treemap=new TreeMap<String,Integer>();
		private Hashtable<String,Float> distance_total_hash=new Hashtable<String,Float>();
		private TreeMap<String,Float> distance_total_treemap=new TreeMap<String,Float>();
		
				
		private void populate_time_hash()
		{
			System.out.println("inside populate_time_hash function "); 
					
			System.out.println("formatted date : "+curr_date);
			
	        // create a response handler that will receive the result or an error
	        CloudCallbackHandler<List<CloudEntity>> handler =
	                new CloudCallbackHandler<List<CloudEntity>>() {
	                    @Override
	                    public void onComplete(List<CloudEntity> results) {
	                        //mAnnounceTxt.setText(R.string.announce_success);
	                        mPosts = results;
	                        System.out.println("MPOSTS value : "+mPosts.toString());
	                        
	                        for (CloudEntity temp : results) {
	                        	
	                        	System.out.println("inside cloudentity results for loop");
	                        	String created_date=formatter.format(Date.parse((temp.getProperties().get("Created_Date").toString())));
	                        	System.out.println("created_date formatted : "+created_date);

	                        	  if(temp.getProperties().get("User_ID").equals(User_ID))
	                        		  {
	                        		  
	                        		  if(curr_date.equals(created_date))
	                        			  System.out.println("match found");
	                        			  
	                        		  //still time
	                        		  if(still_time_hash.get(created_date)==null)
	                        		  {
	                        			  still_time_hash.put(created_date,
	                        					  Long.parseLong(temp.getProperties().get("Still_Time_Total")+""));
	                        		  }
	                        		  else
	                        		  {
	                        			  if(Long.parseLong(temp.getProperties().get("Still_Time_Total")+"")>
	                        			  (long) still_time_hash.get(created_date))
	                        			  {
		                        			  still_time_hash.put(created_date,Long.parseLong(temp.getProperties().get("Still_Time_Total")+""));
	                        			  }
	                        		  }
	                        		  
	                        		  //on foot time
	                        		  if(on_foot_time_hash.get(created_date)==null)
	                        		  {
	                        			  on_foot_time_hash.put(created_date,
	                        					  Long.parseLong(temp.getProperties().get("On_Foot_Time_Total")+""));
	                        		  }
	                        		  else
	                        		  {
	                        			  if(Long.parseLong(temp.getProperties().get("On_Foot_Time_Total")+"")>
	                        			  (long) on_foot_time_hash.get(created_date))
	                        			  {
	                        				  on_foot_time_hash.put(created_date,
		                        					  Long.parseLong(temp.getProperties().get("On_Foot_Time_Total")+""));
	                        			  }
	                        		  }
	                        		  
	                        		  
	                        		//vehicle time
	                        		  if(vehicle_time_hash.get(created_date)==null)
	                        		  {
	                        			  vehicle_time_hash.put(created_date,
	                        					  Long.parseLong(temp.getProperties().get("Vehicle_Time_Total")+""));
	                        		  }
	                        		  else
	                        		  {
	                        			  if(Long.parseLong(temp.getProperties().get("Vehicle_Time_Total")+"")>
	                        			  (long) vehicle_time_hash.get(created_date))
	                        			  {
	                        				  vehicle_time_hash.put(created_date,
		                        					  Long.parseLong(temp.getProperties().get("Vehicle_Time_Total")+""));
	                        			  }
	                        		  }
	                        		  
	                        		  
	                        		//bicycle time
	                        		  if(bicycle_time_hash.get(created_date)==null)
	                        		  {
	                        			  bicycle_time_hash.put(created_date,
	                        					  Long.parseLong(temp.getProperties().get("Bicyle_Time_Total")+""));
	                        		  }
	                        		  else
	                        		  {
	                        			  if(Long.parseLong(temp.getProperties().get("Bicyle_Time_Total")+"")>
	                        			  (long) bicycle_time_hash.get(created_date))
	                        			  {
	                        				  bicycle_time_hash.put(created_date,
		                        					  Long.parseLong(temp.getProperties().get("Bicyle_Time_Total")+""));
	                        			  }
	                        		  }
	                        		  
	                        		//steps_total
	                        		  if(steps_total_hash.get(created_date)==null)
	                        		  {
	                        			  steps_total_hash.put(created_date,
	                        					  Integer.parseInt(temp.getProperties().get("Steps_Total")+""));
	                        			  steps_total_treemap.put(created_date, Integer.parseInt(temp.getProperties().get("Steps_Total")+""));
	                        		  }
	                        		  else
	                        		  {
	                        			  if(Long.parseLong(temp.getProperties().get("Steps_Total")+"")>
	                        			  (int) steps_total_hash.get(created_date))
	                        			  {
	                        				  steps_total_hash.put(created_date,
		                        					  Integer.parseInt(temp.getProperties().get("Steps_Total")+""));
	                        				  steps_total_treemap.put(created_date, Integer.parseInt(temp.getProperties().get("Steps_Total")+""));
	                        			  }
	                        		  }
	                        		  
	                        		//calories_total
	                        		  if(calories_total_hash.get(created_date)==null)
	                        		  {
	                        			  calories_total_hash.put(created_date,
	                        					  Integer.parseInt(temp.getProperties().get("Calories_Total")+""));
	                        			  calories_total_treemap.put(created_date,
	                        					  Integer.parseInt(temp.getProperties().get("Calories_Total")+""));
	                        		  }
	                        		  else
	                        		  {
	                        			  if(Long.parseLong(temp.getProperties().get("Calories_Total")+"")>
	                        			  (int) calories_total_hash.get(created_date))
	                        			  {
	                        				  calories_total_hash.put(created_date,
		                        					  Integer.parseInt(temp.getProperties().get("Calories_Total")+""));
	                        				  calories_total_treemap.put(created_date,
		                        					  Integer.parseInt(temp.getProperties().get("Calories_Total")+""));
	                        			  }
	                        		  }
	                        		  
	                        		  
	                        		//distance_total
	                        		  if(distance_total_hash.get(created_date)==null)
	                        		  {
	                        			  distance_total_hash.put(created_date,
	                        					  Float.parseFloat(temp.getProperties().get("Distance_Total")+""));
	                        			  distance_total_treemap.put(created_date,
	                        					  Float.parseFloat(temp.getProperties().get("Distance_Total")+""));
	                        		  }
	                        		  else
	                        		  {
	                        			  if(Float.parseFloat(temp.getProperties().get("Distance_Total")+"")>
	                        			  (float) distance_total_hash.get(created_date))
	                        			  {
	                        				  distance_total_hash.put(created_date,
	                        						  Float.parseFloat(temp.getProperties().get("Distance_Total")+""));
	                        				  distance_total_treemap.put(created_date,
	                        						  Float.parseFloat(temp.getProperties().get("Distance_Total")+""));
	                        			  }
	                        		  }
	                        		  
	                        		  	                        		  
	                        		  }
	                        	}

	                        System.out.println("still_time_hash values : "+still_time_hash.toString());
	                        System.out.println("on_foot_time_hash values : "+on_foot_time_hash.toString());         
	                        System.out.println("vehicle_time_hash values : "+vehicle_time_hash.toString());         
	                        System.out.println("bicycle_time_hash values : "+bicycle_time_hash.toString());
	                        System.out.println("steps_total_hash values : "+steps_total_hash.toString());
	                        System.out.println("calories_total_hash values : "+calories_total_hash.toString());
	                        System.out.println("distance_total_hash values : "+distance_total_hash.toString());
	                        
	                        System.out.println("steps_total_treemap values : "+steps_total_treemap.toString());
	                        System.out.println("calories_total_treemap values : "+calories_total_treemap.toString());
	                        System.out.println("distance_total_treemap values : "+distance_total_treemap.toString());
	                        
	                        
	                        progressDialog.dismiss();
	                        populate_graph();
	                        
	                    }

	                    @Override
	                    public void onError(IOException exception) {
	                        //mAnnounceTxt.setText(R.string.announce_fail);
	                        //animateArrival();
	                    	System.out.println("inside populate_graph exception");
	                        handleEndpointException(exception);
	                    }
	                };

	        // execute the query with the handler
	    	        mProcessingFragment.getCloudBackend().listByKind(
	    	                "Fitness", CloudEntity.PROP_CREATED_AT, Order.ASC, 50,
	    	                Scope.FUTURE_AND_PAST, handler);
	        
	        //System.out.println("MPOSTS value : "+mPosts.toString());
	        
	        
		}
		
		
private void populate_graph() {
			
	        Enumeration items = still_time_hash.keys();
	        long total_still_time=0;
	        long total_on_foot_time=0;
	        long total_vehicle_time=0;
	        long total_bicycle_time=0;
	        
	        int steps_total_from_hash=0;
	        int calories_total_from_hash=0;
	        float distance_total_from_hash=0;
	        
	     // Now iterate
	     while(items.hasMoreElements()){
	     		
	         // Get the key (convert to string)
	         String strKey = (String)items.nextElement();
	     			
	         // Use key to fetch the item and print out its value
	         total_still_time +=(Long) still_time_hash.get(strKey);
	         //System.out.println(""+still_time_hash.get(strKey).toString());
	     	}

	     System.out.println("total still time from hash "+total_still_time);
	     
	     
	     items = on_foot_time_hash.keys();
	  // Now iterate
	     while(items.hasMoreElements()){
	     		
	         // Get the key (convert to string)
	         String strKey = (String)items.nextElement();
	     			
	         // Use key to fetch the item and print out its value
	         total_on_foot_time +=(Long) on_foot_time_hash.get(strKey);
	         //System.out.println(""+still_time_hash.get(strKey).toString());
	     	}

	     System.out.println("total on foot time from hash "+total_on_foot_time);
	     
	     
	     items = vehicle_time_hash.keys();
		  // Now iterate
		     while(items.hasMoreElements()){
		     		
		         // Get the key (convert to string)
		         String strKey = (String)items.nextElement();
		     			
		         // Use key to fetch the item and print out its value
		         total_vehicle_time +=(Long) vehicle_time_hash.get(strKey);
		         //System.out.println(""+still_time_hash.get(strKey).toString());
		     	}

		     System.out.println("total vehicle time from hash "+total_vehicle_time);
		     
		     
		     items = bicycle_time_hash.keys();
			  // Now iterate
			     while(items.hasMoreElements()){
			     		
			         // Get the key (convert to string)
			         String strKey = (String)items.nextElement();
			     			
			         // Use key to fetch the item and print out its value
			         total_bicycle_time +=(Long) bicycle_time_hash.get(strKey);
			         //System.out.println(""+still_time_hash.get(strKey).toString());
			     	}

			     System.out.println("total bicycle time from hash "+total_bicycle_time);
			     
			     
			     
			     
			     
			long total_time=total_bicycle_time+total_vehicle_time+total_on_foot_time+total_still_time;
			
			
			System.out.println("total of all times "+(float)total_time);
			
			if(total_time>0)
			{
			System.out.println("percentage value :"+(int)((total_still_time*100)/total_time));
			System.out.println("percentage value :"+(int)((total_on_foot_time*100)/total_time));
			System.out.println("percentage value :"+(int)((total_vehicle_time*100)/total_time));
			System.out.println("percentage value :"+(int)((total_bicycle_time*100)/total_time));
			}
			
	     
			final Resources resources = getResources();
	        /*
			final PieGraph pg = (PieGraph) findViewById(R.id.piegraph);
	        
	        pg.setPadding(5);
	        PieSlice slice = new PieSlice();
	        
	        slice.setColor(resources.getColor(R.color.green_light));
	        slice.setSelectedColor(resources.getColor(R.color.transparent_orange));
	        slice.setTitle("still time");
	        
	        if(((int) ((total_still_time*100)/total_time))==0)
	            slice.setValue((int) ((total_still_time*100)/total_time)+2);
	        else
	        	slice.setValue((int) ((total_still_time*100)/total_time));
	        
	        pg.addSlice(slice);
	        
	        
	        slice = new PieSlice();
	        slice.setColor(resources.getColor(R.color.orange));
	        slice.setTitle("on foot time");
	        
	        if(((int) ((total_on_foot_time*100)/total_time))==0)
	            slice.setValue((int) ((total_on_foot_time*100)/total_time)+2);
	        else
	        	slice.setValue((int) ((total_on_foot_time*100)/total_time)+2);
	        
		    pg.addSlice(slice);
	        
		    
	        slice = new PieSlice();
	        slice.setColor(resources.getColor(R.color.purple));
	        slice.setTitle("vehicle time");
	        if(((int) ((total_vehicle_time*100)/total_time))==0)
	            slice.setValue((int) ((total_vehicle_time*100)/total_time)+2);
	        else
	        	slice.setValue((int) ((total_vehicle_time*100)/total_time));
	        	
		    pg.addSlice(slice);
	        
	        
	        slice = new PieSlice();
	        slice.setColor(resources.getColor(R.color.blue));
	        slice.setTitle("bicycle time");
	        if(((int) ((total_bicycle_time*100)/total_time))==0)
	            slice.setValue((int) ((total_bicycle_time*100)/total_time)+2);
	        else
	        	slice.setValue((int) ((total_bicycle_time*100)/total_time));
	        	
		    pg.addSlice(slice);
		    */
		    
			SimpleDateFormat formatter_dd_mm= new SimpleDateFormat("MMM-dd");
			//String curr_date=formatter_dd_mm.format(new Date());
			
		    //bar code
		    ArrayList<Bar> aBars = new ArrayList<Bar>();
	        Bar bar;
		    
	        /* hashmap has been changed to treemap now
		    items = steps_total_hash.keys();
	        
			  // Now iterate
			     while(items.hasMoreElements()){
			     		
			         // Get the key (convert to string)
			         String strKey = (String)items.nextElement();
			     			
			         // Use key to fetch the item and print out its value
			         //steps_total_from_hash +=(int) steps_total_hash.get(strKey);
			         //System.out.println(""+still_time_hash.get(strKey).toString());
			         
			         bar=new Bar();
			         bar.setColor(resources.getColor(R.color.red));
				        bar.setSelectedColor(resources.getColor(R.color.transparent_orange));
				        //bar.setName(strKey);
				        bar.setName(formatter_dd_mm.format((new Date()).parse(strKey)));
				        bar.setValue((int) steps_total_hash.get(strKey));
				        bar.setValueString(steps_total_hash.get(strKey)+"");
				        aBars.add(bar);
			         
			         
//				        steps_total_treemap.put((new Date()).parse(strKey), (int) steps_total_hash.get(strKey));
				        
			     	}

			     System.out.println("total steps from hash "+steps_total_from_hash);
			     
			     hashmap has been changed to treemap now */ 
	        
	        double highest_value=0;
	        
	        if(Transfer.getbtn_graph_heading()==1)
	        {
	        	Button btn_graph_heading=(Button) findViewById(R.id.btn_graph_heading);
	        	btn_graph_heading.setText("Steps :");
	        	
		        for(Map.Entry<String,Integer> entry : steps_total_treemap.entrySet()) {
		        	  String strKey = entry.getKey();
		        	  int value = entry.getValue();
		        	  
		        	  if(highest_value<value)
		        		  highest_value=value;
		        	}
		        
		        System.out.println("total steps value => "+highest_value);
		        
		        //treemap
		        for(Map.Entry<String,Integer> entry : steps_total_treemap.entrySet()) {
		        	  String strKey = entry.getKey();
		        	  int value = entry.getValue();
	
		        	  System.out.println(strKey + " => " + value+"total steps :"+highest_value+" percent :"+ (value/highest_value));
		        	  			   
		        	  bar=new Bar();
				         
					        bar.setSelectedColor(resources.getColor(R.color.transparent_orange));
					        //bar.setName(strKey);
					        bar.setName(formatter_dd_mm.format((new Date()).parse(strKey)));
					        bar.setValue(value);
					        bar.setValueString(value+"");
					        
		        	  
		        	  
		        	  if((int) ((value/highest_value)*100.0)>=0 && (int) ((value/highest_value)*100.0)<=30)
		        	  {	        	  
		        		  bar.setColor(resources.getColor(R.color.red));
		        	  }
		        	  else if((int) ((value/highest_value)*100.0)>30 && (int) ((value/highest_value)*100.0)<=60)
		        	  {
		        		  //bar.setColor(resources.getColor(R.color.blue));
		        	  }
		        	  else if((int) ((value/highest_value)*100.0)>60)
		        	  {
		        		  if((int) ((value/highest_value)*100.0)==100)
			        	  {
			        		  bar.setColor(resources.getColor(R.color.purple));
			        	  }
		        		  else
		        			  bar.setColor(resources.getColor(R.color.green_light));
		        	  }
		        		  
		        	  aBars.add(bar);
		        	  
		        	}
			    
			    //bar graph
		        
	        }
	        else if(Transfer.getbtn_graph_heading()==2)
			{
	        	Button btn_graph_heading=(Button) findViewById(R.id.btn_graph_heading);
	        	btn_graph_heading.setText("Calories :");
	        	
	        	for(Map.Entry<String,Integer> entry : calories_total_treemap.entrySet()) {
		        	  String strKey = entry.getKey();
		        	  int value = entry.getValue();
		        	  
		        	  if(highest_value<value)
		        		  highest_value=value;
		        	}
		        
		        System.out.println("calories_total_treemap value => "+highest_value);
		        
		        //treemap
		        for(Map.Entry<String,Integer> entry : calories_total_treemap.entrySet()) {
		        	  String strKey = entry.getKey();
		        	  int value = entry.getValue();
	
		        	  System.out.println(strKey + " => " + value+"total calories :"+highest_value+" percent :"+ (value/highest_value));
		        	  			   
		        	  bar=new Bar();
				         
					        bar.setSelectedColor(resources.getColor(R.color.transparent_orange));
					        //bar.setName(strKey);
					        bar.setName(formatter_dd_mm.format((new Date()).parse(strKey)));
					        bar.setValue(value);
					        bar.setValueString(value+"");
					        
		        	  
		        	  
		        	  if((int) ((value/highest_value)*100.0)>=0 && (int) ((value/highest_value)*100.0)<=30)
		        	  {	        	  
		        		  bar.setColor(resources.getColor(R.color.red));
		        	  }
		        	  else if((int) ((value/highest_value)*100.0)>30 && (int) ((value/highest_value)*100.0)<=60)
		        	  {
		        		  //bar.setColor(resources.getColor(R.color.blue));
		        	  }
		        	  else if((int) ((value/highest_value)*100.0)>60)
		        	  {
		        		  if((int) ((value/highest_value)*100.0)==100)
			        	  {
			        		  bar.setColor(resources.getColor(R.color.purple));
			        	  }
		        		  else
		        			  bar.setColor(resources.getColor(R.color.green_light));
		        	  }
		        		  
		        	  aBars.add(bar);
		        	  
		        	}
			}
	        else if(Transfer.getbtn_graph_heading()==3)
			{
	        	Button btn_graph_heading=(Button) findViewById(R.id.btn_graph_heading);
	        	btn_graph_heading.setText("Distance :");
	        	
	        	for(Map.Entry<String,Float> entry : distance_total_treemap.entrySet()) {
		        	  String strKey = entry.getKey();
		        	  float value = entry.getValue();
		        	  
		        	  if(highest_value<value)
		        		  highest_value=value;
		        	}
		        
		        System.out.println("distance_total_treemap value => "+highest_value);
		        
		        //treemap
		        for(Map.Entry<String,Float> entry : distance_total_treemap.entrySet()) {
		        	  String strKey = entry.getKey();
		        	  float value = entry.getValue();
		        	  System.out.println("Distance value in treemap : "+value);
		        	  
	
		        	  System.out.println(strKey + " => " + value+"total distance :"+highest_value+" percent :"+ (value/highest_value));
		        	  			   
		        	  bar=new Bar();
				         
					        bar.setSelectedColor(resources.getColor(R.color.transparent_orange));
					        //bar.setName(strKey);
					        bar.setName(formatter_dd_mm.format((new Date()).parse(strKey)));
					        
					        if((value+"").length()>=5)
					        	{
					        	bar.setValue(Float.parseFloat((value+"").substring(0,5)));
					        	bar.setValueString((value+"").substring(0,5));
					        	}
					        else
					        	{
					        	bar.setValue(Float.parseFloat((value+"")));
					        	bar.setValueString((value+""));
					        	}	
					        //bar.setValueString((value+"").substring(0,5));
					        
		        	  
		        	  
		        	  if((int) ((value/highest_value)*100.0)>=0 && (int) ((value/highest_value)*100.0)<=30)
		        	  {	        	  
		        		  bar.setColor(resources.getColor(R.color.red));
		        	  }
		        	  else if((int) ((value/highest_value)*100.0)>30 && (int) ((value/highest_value)*100.0)<=60)
		        	  {
		        		  //bar.setColor(resources.getColor(R.color.blue));
		        	  }
		        	  else if((int) ((value/highest_value)*100.0)>60)
		        	  {
		        		  if((int) ((value/highest_value)*100.0)==100)
			        	  {
			        		  bar.setColor(resources.getColor(R.color.purple));
			        	  }
		        		  else
		        			  bar.setColor(resources.getColor(R.color.green_light));
		        	  }
		        		  
		        	  aBars.add(bar);
		        	  
		        	}
			}
	        
	        
	        BarGraph barGraph = (BarGraph) findViewById(R.id.bargraph);
	        barGraph.setBars(aBars);
	        
	        
	        //LinearLayout barGraph_ll = (LinearLayout) findViewById(R.id.linear_layout_bar_graph);

	    }
		


		
		public void onclickgraph(View view)
		{
			System.out.println("graph heading button clicked");
			Transfer.setbtn_graph_heading();
			populate_graph();
		}
		
		boolean onclickupdategraph_flag=false;
		public void onclickupdategraph(View view)
		{
			System.out.println("graph update button clicked");
			//Transfer.setbtn_graph_heading();
			onclickupdategraph_flag=true;
			store_fitness_data_in_cloud();
            
		}
		
		
		private List<CloudEntity> mPosts = new LinkedList<CloudEntity>();
		
		
		
		/**
	     * Retrieves the list of all posts from the backend and updates the UI. For
	     * demonstration in this sample, the query that is executed is:
	     * "SELECT * FROM Guestbook ORDER BY _createdAt DESC LIMIT 50" This query
	     * will be re-executed when matching entity is updated.
	     */
	    /*
		private void listPosts() {
	        // create a response handler that will receive the result or an error
	        CloudCallbackHandler<List<CloudEntity>> handler =
	                new CloudCallbackHandler<List<CloudEntity>>() {
	                    @Override
	                    public void onComplete(List<CloudEntity> results) {
	                        //mAnnounceTxt.setText(R.string.announce_success);
	                        mPosts = results;
	                        System.out.println("MPOSTS value : "+mPosts.toString());
	                        //mPosts.
	                        //animateArrival();
	                        //updateGuestbookView();
	                        System.out.println("MPOSTS value at 2 : "+mPosts.get(2));
	                        
	                        for (CloudEntity temp : mPosts) {
	                        	  System.out.println("GET PROPERTIES : STEPS "+temp.getProperties().get("steps"));
	                        	  System.out.println("GET PROPERTIES : calories_burned "+temp.getProperties().get("calories_burned"));
	                        	  System.out.println("GET PROPERTIES : miles "+temp.getProperties().get("miles"));
	                        	  System.out.println("GET PROPERTIES : _createdAt "+temp.getCreatedAt());
	                        	}

	                        
	                    }

	                    @Override
	                    public void onError(IOException exception) {
	                        //mAnnounceTxt.setText(R.string.announce_fail);
	                        //animateArrival();
	                        handleEndpointException(exception);
	                    }
	                };

	        // execute the query with the handler
	        mProcessingFragment.getCloudBackend().listByKind(
	                "Fitness_data_test3", CloudEntity.PROP_CREATED_AT, Order.DESC, 50,
	                Scope.FUTURE_AND_PAST, handler);
	        
	        //System.out.println("MPOSTS value : "+mPosts.toString());

	    }
	   
	    */
	    
	    	    
	    public void btn_steps_dist_cal_onClick(View view)
	    {
	    	Spannable span;
	    	pop_sound.start();
	    	int absolutesizespan=70;
	        //btn.setText(span);
	    	
	    	Button btn_steps_dist_cal=(Button) findViewById(R.id.btn_steps_dist_cal);
	    	System.out.println("current value of steps_dist_cal_flag "+Transfer.getbtn_steps_dist_cal_flag());
	    	if(Transfer.getbtn_steps_dist_cal_flag()==0||Transfer.getbtn_steps_dist_cal_flag()==3)
	    		{
	    		Transfer.setbtn_steps_dist_cal_flag();
	    		btn_steps_dist_cal.startAnimation(animFadein);
	    		span = new SpannableString(mStepValue + "\n" +  "Steps");
		        span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (mStepValue+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_steps_dist_cal.setText(span);
		        
	    		}
	    	else if(Transfer.getbtn_steps_dist_cal_flag()==1)
	    		{
	    		Transfer.setbtn_steps_dist_cal_flag();
	    		btn_steps_dist_cal.startAnimation(animFadein);
	    		//span = new SpannableString(mDistanceValue + "\n" +  "Miles");
	    		
	    		if(mDistanceValue<=0)
	    			{
	    			span = new SpannableString(("" + mDistanceValue) + "\n" +  "Miles");
	    			span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) ("" + mDistanceValue).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			}
	    		else
	    			{
	    			span = new SpannableString(("" + (mDistanceValue + 0.000001f)).substring(0, 5) + "\n" +  "Miles");
	    			span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) ("" + (mDistanceValue + 0.000001f)).substring(0, 5).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			}
	    		
		        //span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) (mDistanceValue+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    		//span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) ("" + (mDistanceValue + 0.000001f)).substring(0, 5).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_steps_dist_cal.setText(span);
		        
	    		}
	    	else if(Transfer.getbtn_steps_dist_cal_flag()==2)
    			{
	    		Transfer.setbtn_steps_dist_cal_flag();
	    		btn_steps_dist_cal.startAnimation(animFadein);
	    		span = new SpannableString(mCaloriesValue + "\n" +  "Calorie");
		        span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0,  (mCaloriesValue+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_steps_dist_cal.setText(span);
		        
    			}
	    	
	    	//steps_dist_cal_btn.setText(span);
	    }
	    
	    public void btn_steps_dist_cal_refresh()
	    {
	    	Spannable span;
	    	Button btn_steps_dist_cal=(Button) findViewById(R.id.btn_steps_dist_cal);
	    	//TextView steps_dist_cal_txtvw=(TextView) findViewById(R.id.step_value);
	    	int absolutesizespan=70;
	    	
	    	if(Transfer.getbtn_steps_dist_cal_flag()==1)
	    		{
	    		span = new SpannableString(mStepValue + "\n" +  "Steps");
		        span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (mStepValue+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_steps_dist_cal.setText(span);
	    		}
	    	else if(Transfer.getbtn_steps_dist_cal_flag()==2)
	    		{
	    		//span = new SpannableString(mDistanceValue + "\n" +  "Miles");
	    		if(mDistanceValue<=0)
	    			{
	    			span = new SpannableString(("" + mDistanceValue) + "\n" +  "Miles");
	    			span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) ("" + mDistanceValue).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			}
	    		else
	    			{
	    			span = new SpannableString(("" + (mDistanceValue + 0.000001f)).substring(0, 5) + "\n" +  "Miles");
	    			span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) ("" + (mDistanceValue + 0.000001f)).substring(0, 5).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    			}
	    		
		        //span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) ("" + (mDistanceValue + 0.000001f)).substring(0, 5).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_steps_dist_cal.setText(span);
	    		}
	    	else if(Transfer.getbtn_steps_dist_cal_flag()==3)
    			{
	    		span = new SpannableString(mCaloriesValue + "\n" +  "Calorie");
		        span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (mCaloriesValue+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_steps_dist_cal.setText(span);
    			}
	    }
		     

	    public void btn_stepsmin_mileshr_onClick(View view)
	    {
	    	Spannable span;
	    	pop_sound.start();
	    	System.out.println("btn_stepsmin_mileshr_onClick");
	        //btn.setText(span);
	    	
	    	Button btn_stepsmin_mileshr=(Button) findViewById(R.id.btn_stepsmin_mileshr);
	    	
	    	if(Transfer.getbtn_stepsmin_mileshr_flag()==0||Transfer.getbtn_stepsmin_mileshr_flag()==2)
	    		{
	    		Transfer.setbtn_stepsmin_mileshr_flag();
	    		btn_stepsmin_mileshr.startAnimation(animFadein);
	    		span = new SpannableString(mPaceValue + "\n" +  "steps/min");
		        span.setSpan(new AbsoluteSizeSpan(50), 0, (mPaceValue+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_stepsmin_mileshr.setText(span);
		        
	    		}
	    	else if(Transfer.getbtn_stepsmin_mileshr_flag()==1)
	    		{
	    		Transfer.setbtn_stepsmin_mileshr_flag();
	    		btn_stepsmin_mileshr.startAnimation(animFadein);
	    		span = new SpannableString(mSpeedValue + "\n" +  "miles/hr");
		        span.setSpan(new AbsoluteSizeSpan(50), 0, (int) (mSpeedValue+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_stepsmin_mileshr.setText(span);
	    		}
	    	

	    }
	    
	    public void btn_stepsmin_mileshr_refresh()
	    {
	    	Spannable span;
	    	Button btn_stepsmin_mileshr=(Button) findViewById(R.id.btn_stepsmin_mileshr);
	    		    	
	    	if(Transfer.getbtn_stepsmin_mileshr_flag()==1)
	    		{
	    		span = new SpannableString(mPaceValue + "\n" +  "steps/min");
		        span.setSpan(new AbsoluteSizeSpan(50), 0, (mPaceValue+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_stepsmin_mileshr.setText(span);
	    		}
	    	else if(Transfer.getbtn_stepsmin_mileshr_flag()==2)
	    		{
	    		span = new SpannableString(mSpeedValue + "\n" +  "miles/hr");
		        span.setSpan(new AbsoluteSizeSpan(50), 0, (int) (mSpeedValue+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_stepsmin_mileshr.setText(span);
	    		}
	    	
	    }
	    
	    public void btn_times_onClick(View view)
	    {
	    	Spannable span;
	    	pop_sound.start();
	    	System.out.println("btn_times_onClick");
	        //btn.setText(span);
	    	int absolutesizespan=30;
	    	
	    	Button btn_times=(Button) findViewById(R.id.btn_times);
	    	int btn_times_flag=Transfer.get_btn_times_flag();
	    	
	    	if(btn_times_flag==0||btn_times_flag==4)
	    		{
	    		Transfer.set_btn_times_flag();
	    		btn_times.startAnimation(animFadein);
	    		span = new SpannableString(Transfer.getstilltime_string() + "\n" +  "still time");
		        span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (Transfer.getstilltime_string()+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_times.setText(span);
		        
	    		}
	    	else if(btn_times_flag==1)
	    		{
	    		Transfer.set_btn_times_flag();
	    		btn_times.startAnimation(animFadein);
	    		span = new SpannableString(Transfer.getonfoottime_string() + "\n" +  "onfoot time");
		        span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) (Transfer.getonfoottime_string()+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_times.setText(span);
	    		}
	    	else if(btn_times_flag==2)
	    		{
	    		Transfer.set_btn_times_flag();
	    		btn_times.startAnimation(animFadein);
	    		span = new SpannableString(Transfer.getbicycletime_string() + "\n" +  "bicyle time");
		        span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) (Transfer.getbicycletime_string()+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_times.setText(span);
	    		}
	    	else if(btn_times_flag==3)
	    		{
	    		Transfer.set_btn_times_flag();
	    		btn_times.startAnimation(animFadein);
	    		span = new SpannableString(Transfer.getvehicle_time_string() + "\n" +  "vehicle time");
		        span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) (Transfer.getvehicle_time_string()+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_times.setText(span);
	    		}	    	

	    }
	    
	    public void btn_times_refresh()
	    {
	    	Spannable span;
	    	Button btn_times=(Button) findViewById(R.id.btn_times);
	    	int btn_times_flag=Transfer.get_btn_times_flag();
	    	int absolutesizespan=30;
	    		    	
	    	if(btn_times_flag==1)
	    		{
	    		span = new SpannableString(Transfer.getstilltime_string() + "\n" +  "still time");
	    		span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (Transfer.getstilltime_string()+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_times.setText(span);
	    		}
	    	else if(btn_times_flag==2)
	    		{
	    		span = new SpannableString(Transfer.getonfoottime_string() + "\n" +  "onfoot time");
		        span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) (Transfer.getonfoottime_string()+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_times.setText(span);
	    		}
	    	else if(btn_times_flag==3)
	    		{
	    		span = new SpannableString(Transfer.getbicycletime_string() + "\n" +  "bicyle time");
		        span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) (Transfer.getbicycletime_string()+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_times.setText(span);
	    		}
	    	else if(btn_times_flag==4)
	    		{
	    		span = new SpannableString(Transfer.getvehicle_time_string() + "\n" +  "vehicle time");
		        span.setSpan(new AbsoluteSizeSpan(absolutesizespan), 0, (int) (Transfer.getvehicle_time_string()+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        btn_times.setText(span);
	    		}
	    }
		     
}
