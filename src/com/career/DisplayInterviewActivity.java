package com.career;

import java.text.SimpleDateFormat;

import com.career.db.RecordHolder;

import com.szugyi.circlemenu.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayInterviewActivity extends Activity {
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
	private EditText  mRecrNameEdit, mRecrCompanyEdit, mJobTitle, mJobCompany, mComments, mJobAddress, mJobPostcode;	
	private TextView mDateEdit, mTimeEdit, mErrorText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_hunt_intview_display);
        
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
