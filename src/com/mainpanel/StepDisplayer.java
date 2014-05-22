package com.mainpanel;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.example.android.activityrecognition.LogFile;

/**
 * Counts steps provided by StepDetector and passes the current
 * step count to the activity.
 */
public class StepDisplayer implements StepListener, SpeakingTimer.Listener {

    private int mCount = 0;
    PedometerSettings mSettings;
    Utils mUtils;
    
    public StepDisplayer(PedometerSettings settings, Utils utils) {
        mUtils = utils;
        mSettings = settings;
        notifyListener();
    }
    public void setUtils(Utils utils) {
        mUtils = utils;
    }

    public void setSteps(int steps) {
        mCount = steps;
        notifyListener();
    }
    public void onStep() {
    	System.out.println("inside onstep function of stepdisplayer");
    	
    	String current_activity=Transfer.getCurrentactivity();  
    	
    	System.out.println("inside onstep function current activity "+current_activity);
    	//if(current_activity=="unknown" ||current_activity=="on_foot" ||current_activity==""	||Transfer.getGPSstatus()==0)
    	if(current_activity=="on_foot" && Transfer.getGPSstatus()==1)
    		{
    		mCount ++;
    		notifyListener();
    		}
    	
    	/*
    	//temporary for testing. needs to be removed later
    	mCount ++;
		notifyListener();
		*/
    }
    public void reloadSettings() {
        notifyListener();
    }
    public void passValue() {
    }
    
    

    //-----------------------------------------------------
    // Listener
    
    public interface Listener {
        public void stepsChanged(int value);
        public void passValue();
    }
    private ArrayList<Listener> mListeners = new ArrayList<Listener>();

    public void addListener(Listener l) {
        mListeners.add(l);
    }
    public void notifyListener() {
        for (Listener listener : mListeners) {
            listener.stepsChanged((int)mCount);
        }
    }
    
    //-----------------------------------------------------
    // Speaking
    
    public void speak() {
        if (mSettings.shouldTellSteps()) { 
            if (mCount > 0) {
                mUtils.say("" + mCount + " steps");
            }
        }
    }
    
    
}
