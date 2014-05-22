package com.career;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.career.db.DbWriteObserver;
import com.career.db.Record;
import com.career.db.RecordHolder;
import com.career.db.WriteDbAsyncTask;

import com.szugyi.circlemenu.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity class that displays the form for entering
 * details of conversation/email with a recruiter
 * 
 *
 */
public class JobHuntContactActivity extends FragmentActivity implements DbWriteObserver, TimeDateObserver {
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
	private EditText  mRecrNameEdit, mRecrCompanyEdit, mJobTitle, mJobCompany, mComments;	
	// Text fields
	private TextView mDateEdit, mTimeEdit, mErrorText;
	
	/**
	 * Spinner displaying the contact types: i.e email, phone call
	 */
	private Spinner mTypeSpinner;
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
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	Log.d("navin-ContactActivity", "onCreate");
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_hunt_contact_edit);
        
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
    	
    	Log.d("navin-ContactActivity", "onCreateOptionsMenu");
    	
        getMenuInflater().inflate(R.menu.activity_job_hunt_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	Log.d("navin-ContactActivity", "onOptionsItemSelected");
    	
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
        case R.id.menu_interview:
    	    startActivity(new Intent(getApplicationContext(), JobInterviewActivity.class));
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
    
    /**
     * Callback method executed when Save/Update button is clicked
     * @param v View representing the save button
     */
    public void buttonClicked(View v) {
    	
    	Log.d("navin-ContactActivity", "buttonClicked");
    	
    	// Initialise DB task
        WriteDbAsyncTask writeTask = new WriteDbAsyncTask(getApplicationContext(), this, mUpdate);        
        Log.d("navin-ContactActivity", String.valueOf(mUpdate));
        
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
		
		Log.d("navin-ContactActivity", "printDbWriteResult");
		
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
	
	/**
	 * Builds a Record instance using user entered data
	 * @return Record containing user values relating to contact with Recruiter
	 */
	private Record buildRecord() {
		
		Log.d("navin-ContactActivity", "buildRecord");
		
        Record record = new Record();
        
        record.setRecruiterName(mRecrNameEdit.getText().toString());
        record.setRecruiterCompany(mRecrCompanyEdit.getText().toString());
        record.setJobTitle(mJobTitle.getText().toString());
        record.setJobCompany(mJobCompany.getText().toString());
        record.setComments(mComments.getText().toString());
        record.setRecordType((String) mTypeSpinner.getSelectedItem());
        record.setId(mId);
        RecordHolder rHolder = new RecordHolder(record);
        rHolder.setDateTimeScreen(mDateEdit.getText().toString()+" "+mTimeEdit.getText().toString());
        return record;
	}
	
	@Override
    public void onBackPressed() {
        // Move to Menu screen if back button pressed
    	startActivity(new Intent(getApplicationContext(), MenuActivity.class));
    	finish();
        return;
    }    
	
	/**
	 * Check for empty editText fields
	 * @return boolean, true if at least one field (except comment) is empty
	 */
	private boolean emptyFields() {
		
		Log.d("navin-ContactActivity", "emptyFields");
		
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
	 * Initialise user entry fields
	 */
	private void setupEditTexts() {
		
		Log.d("navin-ContactActivity", "setupEditTexts");
		
		mDateEdit = (TextView)findViewById(R.id.con_date_edit);
        mTimeEdit = (TextView)findViewById(R.id.con_time_edit);
        mRecrNameEdit = (EditText)findViewById(R.id.con_recr_name_edit);
        mRecrCompanyEdit = (EditText)findViewById(R.id.con_recr_company_edit);
        mJobTitle = (EditText)findViewById(R.id.con_job_title_edit);
        mJobCompany = (EditText)findViewById(R.id.conv_job_company_edit);
        mComments = (EditText)findViewById(R.id.con_comments_edit);
        mErrorText = (TextView) findViewById(R.id.con_error_text);
        mTypeSpinner = (Spinner) findViewById(R.id.con_type_spinner);
        mTimeEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	Log.d(LOGTAG, "time clicked");
            	DialogFragment newFragment = new TimePickerFragment(JobHuntContactActivity.this);
                newFragment.show(getSupportFragmentManager(), "timePicker");
                
            }
        });
        mDateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	Log.d(LOGTAG, "date clicked");
            	DialogFragment newFragment = new DatePickerFragment(JobHuntContactActivity.this);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        
	}
	
	/**
	 * Clear user entry fields
	 */
	private void clearEditTexts() {
		
		Log.d("navin-ContactActivity", "clearEditTexts");
		
		mDateEdit.setText("");
		mTimeEdit.setText("");
		mRecrNameEdit.setText("");
		mRecrCompanyEdit.setText("");
		mJobTitle.setText("");
		mJobCompany.setText("");
		mComments.setText("");
		mErrorText.setText("");
	}
	
	/**
	 * Populate user entry fields
	 */
	private void populateEditTexts(Bundle extras) {
		
		Log.d("navin-ContactActivity", "populateEditTexts");
		
		// Retrieve values and check for null values
		
		RecordHolder rHolder = new RecordHolder(extras.getString(RecordHolder.KEY_RECORD));
		Record record = rHolder.getRecord();
		/*String date = extras.getString(RecordHolder.KEY_DATE) != null ? extras.getString(Record.KEY_DATE) : "";
		String time = extras.getString(Record.KEY_TIME) != null ? extras.getString(Record.KEY_TIME) : "";
		String recrName = extras.getString(Record.KEY_RECR_NAME) != null ? extras.getString(Record.KEY_RECR_NAME) : "";
		String recrCompany = extras.getString(Record.KEY_RECR_CO) != null ? extras.getString(Record.KEY_RECR_CO) : "";
		String jobTitle = extras.getString(Record.KEY_JOB_TITLE) != null ? extras.getString(Record.KEY_JOB_TITLE) : "";
		String jobCo = extras.getString(Record.KEY_JOB_CO) != null ? extras.getString(Record.KEY_JOB_CO) : "";
		String comments = extras.getString(Record.KEY_COMMENTS) != null ? extras.getString(Record.KEY_COMMENTS) : "";
		String recordType = extras.getString(Record.KEY_RECORD_TYPE);*/
		String date = rHolder.getDateScreen() != null ? rHolder.getDateScreen() : "";
		String time = rHolder.getTimeScreen() != null ? rHolder.getTimeScreen() : "";
		String recrName = record.getRecruiterName() != null ? record.getRecruiterName() : "";
		String recrCompany = record.getRecruiterCompany() != null ? record.getRecruiterCompany() : "";
		String jobTitle = record.getJobTitle() != null ? record.getJobTitle() : "";
		String jobCo = record.getJobCompany() != null ? record.getJobCompany() : "";
		String comments = record.getComments() != null ? record.getComments() : "";
		String recordType = record.getRecordType();
		mId = record.getId();
		System.out.println(mId);
		extras.putLong(RecordHolder.KEY_ID, mId);
		Log.d("navin-ContactActivity", String.valueOf(mId));
		// Set fields with values
		mDateEdit.setText(date);
		mTimeEdit.setText(time);
		mRecrNameEdit.setText(recrName);
		mRecrCompanyEdit.setText(recrCompany);
		mJobTitle.setText(jobTitle);
		mJobCompany.setText(jobCo);
		mComments.setText(comments);
		
		// Set spinner to value received in recordType
		ArrayAdapter adapter = (ArrayAdapter) mTypeSpinner.getAdapter();
		if (recordType != null) {
			int position = adapter.getPosition(recordType);
			mTypeSpinner.setSelection(position);
		}
		
		
		// Change button text to "Update"
		Button button = (Button)findViewById(R.id.contact_button);
		String updateText = getApplicationContext().getResources().getString(R.string.conversation_button_update);
		button.setText(updateText);
		
	}

	@Override
	public void processTime(int hour, int minute) {
		
		Log.d("navin-ContactActivity", "processTime");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		mTimeEdit.setText(mScreenTimeFormat.format(cal.getTime()));
	}

	@Override
	public void processDate(int year, int month, int day) {
		
		Log.d("navin-ContactActivity", "processDate");
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		mDateEdit.setText(mScreenDateFormat.format(cal.getTime()));
	}
}
