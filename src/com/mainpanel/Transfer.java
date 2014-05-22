package com.mainpanel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.location.LocationManager;

public class Transfer {

	static String current_activity="";
	static int GPS_status=0;
	
	public static void setCurrentactivity(String str)
	{
		current_activity=str;
	}
	
	public static String getCurrentactivity()
	{
		return current_activity;
	}
	
	static int current_activity_confidence=0;
    public static void set_current_activity_confidence(int i)
    {
    	current_activity_confidence=i;
    }
    
    public static int get_current_activity_confidence()
    {
    	return current_activity_confidence;
    }
    
    static String current_activity_timestamp="";
    public static void set_current_activity_timestamp(String str)
    {
    	current_activity_timestamp=str;
    }    
    public static String get_current_activity_timestamp()
    {
    	return current_activity_timestamp;
    }
	
	
	public static void setGPSstatus(int i)
	{
		GPS_status=i;
	}
	
	public static int getGPSstatus()
	{
		return GPS_status;
	}
	
	static boolean IsRunning_activity_recognition=false;
	public static void setIsActivityRecognition()
	{
		IsRunning_activity_recognition=true;
	}
	
	public static boolean getIsActivityRecognition()
	{
		return IsRunning_activity_recognition;
	}
	
	static int btn_steps_dist_cal_flag=0;
	public static void setbtn_steps_dist_cal_flag()
	{
		if(btn_steps_dist_cal_flag==3)
			btn_steps_dist_cal_flag=1;
		else
			btn_steps_dist_cal_flag++;
	}
	
	public static int getbtn_steps_dist_cal_flag()
	{
		return btn_steps_dist_cal_flag;
	}
	
	static int btn_stepsmin_mileshr_flag=0;
	public static void setbtn_stepsmin_mileshr_flag()
	{
		if(btn_stepsmin_mileshr_flag==2)
			btn_stepsmin_mileshr_flag=1;
		else
			btn_stepsmin_mileshr_flag++;
	}
	
	public static int getbtn_stepsmin_mileshr_flag()
	{
		return btn_stepsmin_mileshr_flag;
	}
	
	static int btn_times_flag=0;
	public static void set_btn_times_flag()
	{
		if(btn_times_flag==4)
			btn_times_flag=1;
		else
			btn_times_flag++;
	}
	
	public static int get_btn_times_flag()
	{
		return btn_times_flag;
	}
	
	static String onfoot_time_string="00:00:00";
	static String still_time_string="00:00:00";
	static String bicycle_time_string="00:00:00";
	static String vehicle_time_string="00:00:00";
	
	static long onfoot_time=0;
	static long still_time=0;
	static long bicycle_time=0;
	static long vehicle_time=0;
	
	public static void setonfoottime(long l)
	{
		onfoot_time=l;
		onfoot_time_string=getDate(onfoot_time);
	}
	
	public static long getonfoottime()
	{
		return onfoot_time;
	}
	
	public static String getonfoottime_string()
	{
		return onfoot_time_string;
	}
	
	public static void setstilltime(long l)
	{
		still_time=l;
		still_time_string=getDate(still_time);
	}
	
	public static long getstilltime()
	{
		return still_time;
	}
	
	public static String getstilltime_string()
	{
		return still_time_string;
	}
	
	public static void setbicycletime(long l)
	{
		bicycle_time=l;
		bicycle_time_string=getDate(bicycle_time);
	}
	
	public static long getbicycletime()
	{
		return bicycle_time;
	}
	
	public static String getbicycletime_string()
	{
		return bicycle_time_string;
	}
	
	public static void setvehicle_time(long l)
	{
		vehicle_time=l;
		vehicle_time_string=getDate(vehicle_time);
	}
	
	public static long getvehicle_time()
	{
		return vehicle_time;
	}
	
	public static String getvehicle_time_string()
	{
		return vehicle_time_string;
	}
	
	public static String getDate(long milliseconds)
	{	     
	     int seconds = (int) (milliseconds / 1000) % 60 ;
	     int minutes = (int) ((milliseconds / (1000*60)) % 60);
	     int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
	     
	     String str_hours=hours+"";
	     String str_minutes=minutes+"";
	     String str_seconds=seconds+"";
	     
	     if((hours+"").length()<2)
	    	 str_hours="0"+hours;
	     
	     if((minutes+"").length()<2)
	    	 str_minutes="0"+minutes;
	     
	     if((seconds+"").length()<2)
	    	 str_seconds="0"+seconds;
	     
	     System.out.println("Time is now : "+str_hours+":"+str_minutes+":"+str_seconds);
	     return str_hours+":"+str_minutes+":"+str_seconds;
	}
	
	public static boolean app_running_flag=false;
	
	public static void set_app_running_flag(boolean b)
	{
		app_running_flag=b;
	}
	
	public static boolean get_app_running_flag()
	{
		return app_running_flag;
	}
	
	public static int mStepValue_from_datastore=0;
	
	public static void set_mStepValue_from_datastore(int i)
	{
		mStepValue_from_datastore=i;
	}
	
	public static int get_mStepValue_from_datastore()
	{
		return mStepValue_from_datastore;
	}
	
	public static float mDistanceValue_from_datastore=0;
	
	public static void set_mDistanceValue_from_datastore(float f)
	{
		mDistanceValue_from_datastore=f;
	}
	
	public static float get_mDistanceValue_from_datastore()
	{
		return mDistanceValue_from_datastore;
	}
	
	public static int mCaloriesValue_from_datastore=0;
	
	public static void set_mCaloriesValue_from_datastore(int i)
	{
		mCaloriesValue_from_datastore=i;
	}
	
	public static int get_mCaloriesValue_from_datastore()
	{
		return mCaloriesValue_from_datastore;
	}
	
	static int btn_graph_heading=1;
	public static void setbtn_graph_heading()
	{
		if(btn_graph_heading==3)
			btn_graph_heading=1;
		else
			btn_graph_heading++;
	}
	
	public static int getbtn_graph_heading()
	{
		return btn_graph_heading;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub		

	}

	

}
