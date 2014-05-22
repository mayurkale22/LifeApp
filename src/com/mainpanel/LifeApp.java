package com.mainpanel;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Scanner;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.mainpanel.view.AlarmReceiver;
import com.mainpanel.view.help;
import com.szugyi.circlemenu.R;
import com.mainpanel.view.CommonConstants;
import com.mainpanel.view.GPSManager;
import com.mainpanel.view.PingService;
import com.mainpanel.view.CircleImageView;
import com.mainpanel.view.CircleLayout;
import com.mainpanel.view.UserSettingActivity;
import com.mainpanel.view.CircleLayout.OnCenterClickListener;
import com.mainpanel.view.CircleLayout.OnItemClickListener;
import com.mainpanel.view.CircleLayout.OnItemSelectedListener;
import com.mainpanel.view.CircleLayout.OnRotationFinishedListener;
import com.career.*;
import com.career.db.*;
//import com.social.*;
import com.social.customui.CustomUI;


public class LifeApp extends Activity implements OnItemSelectedListener,
		OnItemClickListener, OnRotationFinishedListener, OnCenterClickListener {
	TextView selectedTextView;
	private Intent mServiceIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		CircleLayout circleMenu = (CircleLayout) findViewById(R.id.main_circle_layout);
		circleMenu.setOnItemSelectedListener(this);
		circleMenu.setOnItemClickListener(this);
		circleMenu.setOnRotationFinishedListener(this);
		circleMenu.setOnCenterClickListener(this);

		selectedTextView = (TextView) findViewById(R.id.main_selected_textView);
		selectedTextView.setText(((CircleImageView) circleMenu
				.getSelectedItem()).getName());
		
		// Check whether GPS is ON
		GPSManager gps = new GPSManager(LifeApp.this);
        gps.start();
		
        
        // Creates an explicit Intent to start the service that constructs and
        // issues the notification.
//        mServiceIntent = new Intent(getApplicationContext(), PingService.class);
//        mServiceIntent.setAction(CommonConstants.ACTION_PING);
//        startService(mServiceIntent);
			
        // location based restaurant alert service

//		Calendar Calendar_Object = Calendar.getInstance();
//		 Calendar calSet = (Calendar) Calendar_Object.clone();
//		
//		 calSet.setTimeInMillis(System.currentTimeMillis());
//		 calSet.set(Calendar.HOUR_OF_DAY, 20);
//		 calSet.set(Calendar.MINUTE,44);
//		 calSet.set(Calendar.SECOND, 0);
//		 calSet.set(Calendar.MILLISECOND, 0);
//		 
//		   if(calSet.compareTo(Calendar_Object) <= 0){
//			    //Today Set time passed, count to tomorrow
//			    calSet.add(Calendar.DATE, 1);
//			}
//		
//		Intent resto_AlarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
//		PendingIntent resto_pendingIntent = PendingIntent.getBroadcast(LifeApp.this,1, resto_AlarmIntent, 0);
//
//		AlarmManager resto_alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//		resto_alarmManager.setRepeating(AlarmManager.RTC, Calendar_Object.getTime().getTime(), AlarmManager.INTERVAL_DAY, resto_pendingIntent);
//		
		
		AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 8:30 a.m.
        calendar.set(Calendar.HOUR_OF_DAY, 02);
        calendar.set(Calendar.MINUTE, 48);
        
        // Set the alarm to fire at approximately 8:30 a.m., according to the device's
        // clock, and to repeat once a day.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

//		// Map data store service
//		Calendar Calendar_Object1= Calendar.getInstance();
//
//		Intent map_AlarmIntent = new Intent(getApplicationContext(), MapAlarmReceiver.class);
//		PendingIntent map_pendingIntent = PendingIntent.getBroadcast(LifeApp.this,0, map_AlarmIntent, 0);
//
//		AlarmManager map_alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//		//alarmManager.set(AlarmManager.RTC, Calendar_Object1.getTimeInMillis(),pendingIntent1);
//		
//		map_alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar_Object1.getTimeInMillis(),180* 1000, map_pendingIntent);
	}
	
    public void onBackPressed() {
    	finish();
       }
    
    
	@Override
	public void onItemSelected(View view, String name) {
		selectedTextView.setText(name);

		switch (view.getId()) {
			case R.id.lifeapp_map:
				// Request to Travel-Map Activity

				break;
			case R.id.lifeapp_fitness:
				// Handle google selection
				break;
			case R.id.lifeapp_social:
				// Handle linkedin selection
				break;
			case R.id.lifeapp_career:
				 //Handle myspace selection
				break;
			case R.id.lifeapp_settings:
				 //Handle twitter selection
				break;
			case R.id.lifeapp_help:
				// Handle wordpress selection
				break;
		}
	}

	@Override
	public void onItemClick(View view, String name) {
		Toast.makeText(getApplicationContext(),
				getResources().getString(R.string.start_app) + " " + name,
				Toast.LENGTH_SHORT).show();

		switch (view.getId()) {
			case R.id.lifeapp_map:
				// Request to Travel-Map Activity

			    		Intent intent = new Intent(LifeApp.this, LifeApp_Map.class);
			            startActivity(intent);
			            overridePendingTransition (R.anim.open_next, R.anim.close_main);

				break;
			case R.id.lifeapp_fitness:
				// Request to fitness Activity

	    		Intent fitness_intent = new Intent(LifeApp.this, Pedometer.class);
	            startActivity(fitness_intent);
	            overridePendingTransition (R.anim.open_next, R.anim.close_main);
				break;
			case R.id.lifeapp_social:
				// Handle linkedin click
				Intent social_intent = new Intent(LifeApp.this, CustomUI.class);
	            startActivity(social_intent);
	            overridePendingTransition (R.anim.open_next, R.anim.close_main);
				
				break;
				
			case R.id.lifeapp_career:
				// Handle myspace click
				Intent career_intent = new Intent(LifeApp.this, MenuActivity.class);
	            startActivity(career_intent);
	            overridePendingTransition (R.anim.open_next, R.anim.close_main);
				
				
				break;
			case R.id.lifeapp_settings:
				
				Intent i = new Intent(this, UserSettingActivity.class);
				startActivityForResult(i, 1);
				overridePendingTransition (R.anim.card_flip_left_in, R.anim.card_flip_left_out);
				
				break;
			case R.id.lifeapp_help:
				// Handle wordpress click
				
				help helpDialog = new help(LifeApp.this);
				helpDialog.setTitle("Help");
				helpDialog.show(); 
				break;
		}
	}

	@Override
	public void onCenterClick() {
		Toast.makeText(getApplicationContext(), R.string.center_click,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRotationFinished(View view, String name) {
		Animation animation = new RotateAnimation(0, 360, view.getWidth() / 2,
				view.getHeight() / 2);
		animation.setDuration(250);
		view.startAnimation(animation);
	}
	

}


