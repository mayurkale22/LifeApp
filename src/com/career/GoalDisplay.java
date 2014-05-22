package com.career;

import java.text.SimpleDateFormat;

import com.career.db.RecordHolder;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;
import com.szugyi.circlemenu.R;

public class GoalDisplay extends ActionBarActivity {
	/**
	 * Activity name for logging purposes
	 */
	private final static String LOGTAG = JobHuntContactActivity.class.getName();
	/**
	 * Format date to screen
	 */
	private final SimpleDateFormat mScreenDateFormat = new SimpleDateFormat(RecordHolder.DATEFORMAT_SCREEN_DATE);
	/**
	 * Format time to screen
	 */
	private final SimpleDateFormat mScreenTimeFormat = new SimpleDateFormat(RecordHolder.DATEFORMAT_SCREEN_TIME);
	// Edit fields
	private EditText  mGoalLocationEdit, mGoalTitleEdit, mGoalDescEdit;	
	private TextView mDateEdit, mTimeEdit, mErrorText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goal_display);

		// Initialise EditTexts
        setupTextviews();
        
        // Fetch bundle from Intent
        Bundle extras = getIntent().getExtras();
        
        // Populate text fields  
        if(extras != null) {
        	populateTextviews(extras);
        }
	}

	 private void setupTextviews() {
	    	
	    }
	    
	    private void populateTextviews(Bundle extras) {
	    	
	    }


}
