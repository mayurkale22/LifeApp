package com.career;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.szugyi.circlemenu.R;
import com.career.db.DbWriteObserver;
import com.career.db.Record;
import com.career.db.RecordHolder;
import com.career.db.WriteDbAsyncTask;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GoalEdit extends FragmentActivity implements DbWriteObserver, TimeDateObserver{

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
	private EditText  mLocation, mGoalTitle, mDescription;	
	// Text fields
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.goal_edit);

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
	
	
	private void setupEditTexts() {
		
		mDateEdit = (TextView)findViewById(R.id.goal_date_edit);
        mTimeEdit = (TextView)findViewById(R.id.goal_time_edit);
        mLocation = (EditText)findViewById(R.id.goal_location);
        mGoalTitle = (EditText)findViewById(R.id.title_text);
        mDescription = (EditText)findViewById(R.id.desc_text);
        mErrorText = (TextView) findViewById(R.id.goal_error_text);
		
        mTimeEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	Log.d(LOGTAG, "time clicked");
            	DialogFragment newFragment = new TimePickerFragment(GoalEdit.this);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
        mDateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	Log.d(LOGTAG, "date clicked");
            	DialogFragment newFragment = new DatePickerFragment(GoalEdit.this);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
	}
	
	
	/**
	 * Clear user entry fields
	 */
	private void clearEditTexts() {
		mDateEdit.setText("");
		mTimeEdit.setText("");
		mLocation.setText("");
		mGoalTitle.setText("");
		mDescription.setText("");		
		mErrorText.setText("");
	}
	
	
	private void populateEditTexts(Bundle extras) {
		// Retrieve values and check for null values
				mId = extras.getLong(RecordHolder.KEY_ID);
				RecordHolder rHolder = new RecordHolder(extras.getString(RecordHolder.KEY_RECORD));
				Record record = rHolder.getRecord();
				String date = rHolder.getDateScreen() != null ? rHolder.getDateScreen() : "";
				String time = rHolder.getTimeScreen() != null ? rHolder.getTimeScreen() : "";
				String goalLocation = record.getGoalLocation() != null ? record.getGoalLocation() : "";
				String goalTitle = record.getGoalTitle() != null ? record.getGoalTitle() : "";
				String goalDesc = record.getGoalDesc() != null ? record.getGoalDesc() : "";				
				String recordType = record.getRecordType();
				
				mId = record.getId();
				System.out.println(mId);
				extras.putLong(RecordHolder.KEY_ID, mId);
				Log.d("navin-GoalActivity", String.valueOf(mId));
				
				// Set fields with values
				mDateEdit.setText(date);
				mTimeEdit.setText(time);
				mLocation.setText(goalLocation);
				mGoalTitle.setText(goalTitle);
				mDescription.setText(goalDesc);
				
		/*String date = extras.getString(Record.KEY_DATE) != null ? extras.getString(Record.KEY_DATE) : "";
		String time = extras.getString(Record.KEY_TIME) != null ? extras.getString(Record.KEY_TIME) : "";
		String recrName = extras.getString(Record.KEY_RECR_NAME) != null ? extras.getString(Record.KEY_RECR_NAME) : "";
		String recrCompany = extras.getString(Record.KEY_RECR_CO) != null ? extras.getString(Record.KEY_RECR_CO) : "";
		String jobTitle = extras.getString(Record.KEY_JOB_TITLE) != null ? extras.getString(Record.KEY_JOB_TITLE) : "";
		String jobCo = extras.getString(Record.KEY_JOB_CO) != null ? extras.getString(Record.KEY_JOB_CO) : "";
		String comments = extras.getString(Record.KEY_COMMENTS) != null ? extras.getString(Record.KEY_COMMENTS) : "";
		String jobAddress = extras.getString(Record.KEY_JOB_ADDRESS) != null ? extras.getString(Record.KEY_JOB_ADDRESS) : "";
		String jobPostcode = extras.getString(Record.KEY_JOB_POSTCODE) != null ? extras.getString(Record.KEY_JOB_POSTCODE) : "";*/
		
		// Set fields with values
		mDateEdit.setText(date);
		mTimeEdit.setText(time);
		mLocation.setText(goalLocation);
		mGoalTitle.setText(goalTitle);
		mDescription.setText(goalDesc);		
		
		// Change button text to "Update"
		Button button = (Button)findViewById(R.id.goal_save_button);
		String updateText = getApplicationContext().getResources().getString(R.string.conversation_button_update);
		button.setText(updateText);
	}
	
	
	@Override
	public void processTime(int hour, int minute) {
		// TODO Auto-generated method stub
		
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		
		mTimeEdit.setText(mScreenTimeFormat.format(cal.getTime()));	
	}

	@Override
	public void processDate(int year, int month, int day) {
		// TODO Auto-generated method stub
		
		cal.set(year, month, day);
		mDateEdit.setText(mScreenDateFormat.format(cal.getTime()));
	}
	
	@Override
    public void onBackPressed() {
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
    	// Initialise DB task
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
    
    /**
	 * Check for empty editText fields
	 * @return boolean, true if at least one field (except comment) is empty
	 */
	private boolean emptyFields() {
		boolean emptyField = false;
		if (mDateEdit.getText().toString().equals("")) emptyField = true;
		if (mTimeEdit.getText().toString().equals("")) emptyField = true;
		if (mLocation.getText().toString().equals("")) emptyField = true;
		if (mGoalTitle.getText().toString().equals("")) emptyField = true;
		if (mDescription.getText().toString().equals("")) emptyField = true;
		
		return emptyField;
	}
	
	/**
	 * Builds a Record instance using user entered data
	 * @return Record containing user values relating to contact with Recruiter
	 */
	private Record buildRecord() {
		
        Record record = new Record();
        
        record.setGoalLocation(mLocation.getText().toString());       
        record.setGoalTitle(mGoalTitle.getText().toString());
        record.setGoalDesc(mDescription.getText().toString());
        
        record.setRecordType(RecordHolder.RECORD_TYPE_GOAL);
       
        record.setId(mId);
        RecordHolder rHolder = new RecordHolder(record);
        rHolder.setDateTimeScreen(mDateEdit.getText().toString()+" "+mTimeEdit.getText().toString());
        return record;
	}
	
	@Override
	public void printDbWriteResult(boolean writeSuccess) {
		String message;
		if(writeSuccess) {
			message = mUpdate==2 ? "Record successfully updated" : "Record sucessfully saved.";
			// Only clear fields if DB write was successful
			clearEditTexts();
		} else {
			message = "Record save failed";
		}
		
		// Display to user success of DB write
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		
		Log.d("Navin-Goal", "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.activity_job_hunt_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
}
	
	