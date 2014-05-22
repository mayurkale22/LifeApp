package com.career;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.career.db.DbWriteObserver;
import com.career.db.Record;
import com.career.db.RecordHolder;
import com.career.db.WriteDbAsyncTask;
import com.szugyi.circlemenu.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JobInterviewActivity extends FragmentActivity implements DbWriteObserver, TimeDateObserver {

	/**
	 * Bundle Key to indicate data will arrive from conversation screen
	 * to populate some edit fields
	 */
	public final static String NEW_RECORD_PREPOP = "prepopdata";
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
	/**
	 * indicates whether to insert to db or update or delete.
	 * 1 = insert
	 * 2 = update
	 * 3 = delete
	 */
	private int mUpdate = 1;
	/**
	 * long holds the record ID
	 */
	private long mId = 0;
	
	/**
	 * The latest event date set by user
	 */
	private Calendar cal = Calendar.getInstance();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d("Navin-IntAct", "onCreate");
        
        setContentView(R.layout.job_hunt_intview_edit);
        
        // Initialise EditTexts
        setupEditTexts();
        
        // Fetch bundle from Intent
        Bundle extras = getIntent().getExtras();
        
        // Populate edit fields if user wishes to update values 
        if(extras != null) {
        	populateEditTexts(extras);
        	mUpdate = 2;
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	Log.d("Navin-IntAct", "onCreateOptionsMenu");
    	
        getMenuInflater().inflate(R.menu.activity_job_hunt_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	Log.d("Navin-IntAct", "onOptionsItemSelected");
    	
        // Handle item selection
        switch (item.getItemId()) {
        	case R.id.menu_menu:
        		startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        		finish();
        		return true;
        	case R.id.menu_goal_set:
            	startActivity(new Intent(getApplicationContext(), GoalEdit.class));
            	finish();
                return true;
            case R.id.menu_conversation:
            	startActivity(new Intent(getApplicationContext(), JobHuntContactActivity.class));
            	finish();
                return true;
            case R.id.menu_calendar:
            	startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
            	finish();
                return true;     
            case R.id.menu_search:
            	startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            	finish();
                return true;    
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onBackPressed() {
    	
    	Log.d("Navin-IntAct", "onBackPressed");
    	
        // Move to Menu screen if back button pressed
    	startActivity(new Intent(getApplicationContext(), MenuActivity.class));
    	finish();
        return;
    }
    
    /**
     * Callback method executed when Save/Update button is clicked
     * @param v View representing the save button
     */
    public void buttonClicked(View v) {
    	
    	Log.d("Navin-IntAct", "buttonClicked");
    	
        WriteDbAsyncTask writeTask = new WriteDbAsyncTask(getApplicationContext(), this, mUpdate);
        
        // Check for empty fields
        if(emptyFields()) {
        	
        	mErrorText.setText("Please fill in all fields");
        }
        else
        {
        	// Run DB task to write user entered details to DB
            writeTask.execute(buildRecord());
        }
    }

	@Override
	public void printDbWriteResult(boolean writeSuccess) {
		
		Log.d("Navin-IntAct", "printDbWriteResult");
		
		String message;
		
		if(writeSuccess) {
			
			message = mUpdate==2 ? "Record updated" : "Record saved.";
			new AlertDialog.Builder(this)
            .setTitle(message)
            .setMessage("Do you want to save this event to Google Calendar?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                	Intent intent = new Intent(Intent.ACTION_INSERT)
                	        .setData(Uri.parse("content://com.android.calendar/events"))
                	        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis())
                	        .putExtra(Events.TITLE, "Job Interview")
                	        .putExtra(Events.DESCRIPTION, mJobTitle.getText().toString()+" at "+mJobCompany.getText().toString())
                	        .putExtra(Events.EVENT_LOCATION, mJobAddress.getText().toString())
                	        .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
                	startActivity(intent);
                	clearEditTexts();
                }
            }).create().show();
			
		} else {
			message = "Record save failed";
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		}
		
	}
	
	/**
	 * Check for empty editText fields
	 * @return boolean, true if at least one field (except comment) is empty
	 */
	private boolean emptyFields() {
		
		Log.d("Navin-IntAct", "emptyFields");
		
		boolean emptyField = false;
		if (mDateEdit.getText().toString().equals("")) emptyField = true;
		if (mTimeEdit.getText().toString().equals("")) emptyField = true;
		if (mRecrNameEdit.getText().toString().equals("")) emptyField = true;
		if (mRecrCompanyEdit.getText().toString().equals("")) emptyField = true;
		if (mJobTitle.getText().toString().equals("")) emptyField = true;
		if (mJobCompany.getText().toString().equals("")) emptyField = true;
		return emptyField;
	}	
	
	/**
	 * Builds a Record instance using user entered data
	 * @return Record containing user values relating to contact with Recruiter
	 */
	private Record buildRecord() {
		
		Log.d("Navin-IntAct", "buildRecord");
		
        Record record = new Record();
        
        record.setRecruiterName(mRecrNameEdit.getText().toString());
        record.setRecruiterCompany(mRecrCompanyEdit.getText().toString());
        record.setJobTitle(mJobTitle.getText().toString());
        record.setJobCompany(mJobCompany.getText().toString());
        record.setComments(mComments.getText().toString());
        record.setRecordType(RecordHolder.RECORD_TYPE_INT);
        record.setJobAddress(mJobAddress.getText().toString());
        record.setJobPostcode(mJobPostcode.getText().toString());
        record.setId(mId);
        RecordHolder rHolder = new RecordHolder(record);
        rHolder.setDateTimeScreen(mDateEdit.getText().toString()+" "+mTimeEdit.getText().toString());
        return record;
	}
	
	/**
	 * Initialise user entry fields
	 */
	private void setupEditTexts() {
		
		Log.d("Navin-IntAct", "setupEditeTexts");
		
		mDateEdit = (TextView)findViewById(R.id.inv_date_edit);
        mTimeEdit = (TextView)findViewById(R.id.inv_time_edit);
        mRecrNameEdit = (EditText)findViewById(R.id.inv_recr_name_edit);
        mRecrCompanyEdit = (EditText)findViewById(R.id.inv_recr_company_edit);
        mJobTitle = (EditText)findViewById(R.id.inv_job_title_edit);
        mJobCompany = (EditText)findViewById(R.id.inv_job_company_edit);
        mComments = (EditText)findViewById(R.id.inv_comments_edit);
        mErrorText = (TextView) findViewById(R.id.inv_error_text);
        mJobAddress = (EditText) findViewById(R.id.inv_job_address_edit);
        mJobPostcode = (EditText) findViewById(R.id.inv_job_postcode_edit);
        mTimeEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	Log.d(LOGTAG, "time clicked");
            	DialogFragment newFragment = new TimePickerFragment(JobInterviewActivity.this);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
        mDateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	Log.d(LOGTAG, "date clicked");
            	DialogFragment newFragment = new DatePickerFragment(JobInterviewActivity.this);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
	}
	
	/**
	 * Clear user entry fields
	 */
	private void clearEditTexts() {
		
		Log.d("Navin-IntAct", "clearEditTexts");
		
		mDateEdit.setText("");
		mTimeEdit.setText("");
		mRecrNameEdit.setText("");
		mRecrCompanyEdit.setText("");
		mJobTitle.setText("");
		mJobCompany.setText("");
		mComments.setText("");
		mErrorText.setText("");
		mJobAddress.setText("");
		mJobPostcode.setText("");
	}	
	
	/**
	 * Populate user entry fields
	 */
	private void populateEditTexts(Bundle extras) {
		
		Log.d("Navin-IntAct", "populateEditTexts");
		
		// Retrieve values and check for null values
				mId = extras.getLong(RecordHolder.KEY_ID);
				RecordHolder rHolder = new RecordHolder(extras.getString(RecordHolder.KEY_RECORD));
				Record record = rHolder.getRecord();
				String date = rHolder.getDateScreen() != null ? rHolder.getDateScreen() : "";
				String time = rHolder.getTimeScreen() != null ? rHolder.getTimeScreen() : "";
				String recrName = record.getRecruiterName() != null ? record.getRecruiterName() : "";
				String recrCompany = record.getRecruiterCompany() != null ? record.getRecruiterCompany() : "";
				String jobTitle = record.getJobTitle() != null ? record.getJobTitle() : "";
				String jobCo = record.getJobCompany() != null ? record.getJobCompany() : "";
				String comments = record.getComments() != null ? record.getComments() : "";
				String recordType = record.getRecordType();
				String jobAddress = record.getJobAddress() != null ? record.getJobAddress() : "";
				String jobPostcode = record.getJobPostcode() != null ? record.getJobPostcode() : "";
				
				mId = record.getId();
				System.out.println(mId);
				extras.putLong(RecordHolder.KEY_ID, mId);
				Log.d("navin-IntActivity", String.valueOf(mId));
				
				// Set fields with values
				mDateEdit.setText(date);
				mTimeEdit.setText(time);
				mRecrNameEdit.setText(recrName);
				mRecrCompanyEdit.setText(recrCompany);
				mJobTitle.setText(jobTitle);
				mJobCompany.setText(jobCo);
				mComments.setText(comments);// Retrieve values and check for null values
				mJobAddress.setText(jobAddress);
				mJobPostcode.setText(jobPostcode);
		/*String date = extras.getString(Record.KEY_DATE) != null ? extras.getString(Record.KEY_DATE) : "";
		String time = extras.getString(Record.KEY_TIME) != null ? extras.getString(Record.KEY_TIME) : "";
		String recrName = extras.getString(Record.KEY_RECR_NAME) != null ? extras.getString(Record.KEY_RECR_NAME) : "";
		String recrCompany = extras.getString(Record.KEY_RECR_CO) != null ? extras.getString(Record.KEY_RECR_CO) : "";
		String jobTitle = extras.getString(Record.KEY_JOB_TITLE) != null ? extras.getString(Record.KEY_JOB_TITLE) : "";
		String jobCo = extras.getString(Record.KEY_JOB_CO) != null ? extras.getString(Record.KEY_JOB_CO) : "";
		String comments = extras.getString(Record.KEY_COMMENTS) != null ? extras.getString(Record.KEY_COMMENTS) : "";
		String jobAddress = extras.getString(Record.KEY_JOB_ADDRESS) != null ? extras.getString(Record.KEY_JOB_ADDRESS) : "";
		String jobPostcode = extras.getString(Record.KEY_JOB_POSTCODE) != null ? extras.getString(Record.KEY_JOB_POSTCODE) : "";
		
		// Set fields with values
		mDateEdit.setText(date);
		mTimeEdit.setText(time);
		mRecrNameEdit.setText(recrName);
		mRecrCompanyEdit.setText(recrCompany);
		mJobTitle.setText(jobTitle);
		mJobCompany.setText(jobCo);
		mComments.setText(comments);
		mJobAddress.setText(jobAddress);
		mJobPostcode.setText(jobPostcode);*/
		
		// Change button text to "Update"
		Button button = (Button)findViewById(R.id.conversation_button);
		String updateText = getApplicationContext().getResources().getString(R.string.conversation_button_update);
		button.setText(updateText);
	}
	
	@Override
	public void processTime(int hour, int minute) {
		
		Log.d("Navin-IntAct", "processTime");
		
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		
		mTimeEdit.setText(mScreenTimeFormat.format(cal.getTime()));
	}

	@Override
	public void processDate(int year, int month, int day) {
		
		Log.d("Navin-IntAct", "processDate");
		
		cal.set(year, month, day);
		mDateEdit.setText(mScreenDateFormat.format(cal.getTime()));
	}	
}
