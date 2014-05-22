package com.mainpanel;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

public class ActivityTimeCountService extends Service {

	static final public String COPA_RESULT ="com.controlj.copame.backend.COPAService.REQUEST_PROCESSED";
	LocalBroadcastManager broadcaster;
	
	@Override
    public void onCreate() {        
        super.onCreate();
        System.out.println("ActivityTimeCount Service is running");
        
       	//Transfer.setstilltime(System.currentTimeMillis());
       	//Transfer.setonfoottime(System.currentTimeMillis());  	
        
        broadcaster = LocalBroadcastManager.getInstance(this);
        
        myTimer = new Timer();
		myTimer.schedule(new TimerTask() {			
			@Override
			public void run() {
				System.out.println("total still time "+Transfer.getstilltime_string()+" seconds: "+Transfer.getstilltime());
				System.out.println("total onfoot time "+Transfer.getonfoottime_string()+" seconds: "+Transfer.getonfoottime());
				//String current_activity=Transfer.getCurrentactivity();
				
				TimerMethod();
			}
			
		}, 0, 1000);
    }
	
	private void TimerMethod()
	{
		
		System.out.println("ActivityTimeCount timer");
		
		if(Transfer.getGPSstatus()==1&&Transfer.getCurrentactivity()!="")
		{
	        if (Transfer.getCurrentactivity()=="still") {
	            //buildAlertMessageNoGps();
	        	System.out.println("still timer running");
	        	Transfer.setstilltime(Transfer.getstilltime()+1000);
	        	sendResult("1");
	        }
	        else if(Transfer.getCurrentactivity()=="on_foot")
	        {
	        	System.out.println("onfoot timer running");
	        	Transfer.setonfoottime(Transfer.getonfoottime()+1000);
	        	sendResult("2");
	        }
	        else if(Transfer.getCurrentactivity()=="on_vehicle")
	        {
	        	System.out.println("on_vehicle timer running");
	        	Transfer.setvehicle_time(Transfer.getvehicle_time()+1000);
	        	sendResult("3");
	        }
	        else if(Transfer.getCurrentactivity()=="on_bicycle")
	        {
	        	System.out.println("on_bicycle timer running");
	        	Transfer.setbicycletime(Transfer.getbicycletime()+1000);
	        	sendResult("4");
	        }
	        
		}
		
	}
	
	public void sendResult(String activity) {
	    Intent intent = new Intent(COPA_RESULT);
	    intent.putExtra("activity", activity);
	    broadcaster.sendBroadcast(intent);
	}
	
    @Override
    public void onDestroy() {
    	System.out.println("ActivityTimeCount service onDestroy");
    	
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
