package com.mainpanel;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.TextView;

public class GPSCheckService extends Service {

	@Override
    public void onCreate() {        
        super.onCreate();
        System.out.println("GPS service is running");
        
        myTimer = new Timer();
		myTimer.schedule(new TimerTask() {			
			@Override
			public void run() {
				TimerMethod();
			}
			
		}, 0, 3000);
    }
	
	private void TimerMethod()
	{
		System.out.println("hello timer");
		//This method is called directly by the timer
		//and runs in the same thread as the timer.

		//We call the method that will work with the UI
		//through the runOnUiThread method.
		//this.runOnUiThread(Timer_Tick);
		
		 final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

	        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	            //buildAlertMessageNoGps();
	        	System.out.println("GPS is not on");
	        	Transfer.setGPSstatus(0);
	        	Transfer.setCurrentactivity("");
	        }
	        else
	        {
	        	System.out.println("GPS is on");
	        	Transfer.setGPSstatus(1);
	        }
	}
	
	/*
	private Runnable Timer_Tick = new Runnable() {
		public void run() {
		
		//This method runs in the same thread as the UI.    	       
		
		//Do something to the UI thread here
	
		}
	};
	*/

    @Override
    public void onDestroy() {
    	System.out.println("GPS service onDestroy");
    	
    	System.out.println("stopping timer");
    	myTimer.cancel();
    	myTimer.purge();
    	
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {        
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {        
        return null;
    }
    
    private Timer myTimer;
    
    
	
}
