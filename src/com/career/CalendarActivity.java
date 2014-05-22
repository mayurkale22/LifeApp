package com.career;

import java.util.List;

import com.career.db.DbReadObserver;
import com.career.db.ReadDbAsyncTask;
import com.career.db.Record;
import com.career.db.RecordHolder;

import com.szugyi.circlemenu.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * Activity class that displays the job hunt calendar
 * 
 * 
 */
public class CalendarActivity extends ListActivity implements DbReadObserver {
	
	/**
	 * Action Bar item
	 */
	public final static String ACTION_BAR_CALENDAR = "Calendar";
	private final static String MESSAGE_NO_ITEMS = "No items were found";
	private final static boolean READ_MODE_CALENDAR = true;
	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private final static String LOGTAG = CalendarActivity.class.getName();
    
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    private List<Record> mRecords;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d("Navin-CalActivity", "onCreate");
        
        setContentView(R.layout.job_hunt_calendar);
        
        // Gesture detection
        gestureDetector = new GestureDetector(getApplicationContext(), new CalendarGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        
        getListView().setOnTouchListener(gestureListener);
        getListView().setOnItemClickListener(new OnItemClickListener() {
        	  
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
        		int position, long id) {
        	
        		Log.d("Navin-CalActivity", "onItemClick");
        	    
        	    Record record = mRecords.get(position);
        	    RecordHolder recordHolder = new RecordHolder(record);
        	    Intent intent = null;
        	    
        	    // Initialise Intent with relevant destination
        	    if(record.getRecordType().equals(RecordHolder.RECORD_TYPE_GOAL)) {
        	    	intent = new Intent(getApplicationContext(), GoalEdit.class);
        	    }
        	    else if(record.getRecordType().equals(RecordHolder.RECORD_TYPE_INT)) {
        	    	intent = new Intent(getApplicationContext(), JobInterviewActivity.class);
        	    	//intent.putExtra(RecordHolder.KEY_JOB_ADDRESS, record.getJobAddress());
            	    //intent.putExtra(RecordHolder.KEY_JOB_POSTCODE, record.getJobPostcode());
        	    } else {
        	    	intent = new Intent(getApplicationContext(), JobHuntContactActivity.class);
        	    	//intent.putExtra(RecordHolder.KEY_RECORD_TYPE, record.getRecordType());
        	    }
        	    intent.putExtra(RecordHolder.KEY_RECORD, recordHolder.recordToJson());
        	    // Load Bundle with record data that is to be printed to destination screen
        	    /*intent.putExtra(RecordHolder.KEY_DATE, recordHolder.getDateScreen());
        	    intent.putExtra(RecordHolder.KEY_TIME, recordHolder.getTimeScreen());
        	    intent.putExtra(RecordHolder.KEY_COMMENTS, record.getComments());
        	    intent.putExtra(RecordHolder.KEY_JOB_CO, record.getJobCompany());
        	    intent.putExtra(RecordHolder.KEY_JOB_TITLE, record.getJobTitle());
        	    intent.putExtra(RecordHolder.KEY_RECR_CO, record.getRecruiterCompany());
        	    intent.putExtra(RecordHolder.KEY_RECR_NAME, record.getRecruiterName());
        	    intent.putExtra(RecordHolder.KEY_ID, record.getId());*/
        	    startActivity(intent);
        	    finish();
        	  }
        	});
        
        // Initialise DB task
        ReadDbAsyncTask dbTask = new ReadDbAsyncTask(getApplicationContext(), this, READ_MODE_CALENDAR);
        // Run DB task to fetch entries from DB
        dbTask.execute();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	Log.d("Navin-CalActivity", "onCreateOptionsMenu");
    	
        getMenuInflater().inflate(R.menu.activity_job_hunt_main, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	
    	Log.d("Navin-CalActivity", "onBackPressed");
    	
        // Move to Menu screen if back button pressed
    	startActivity(new Intent(getApplicationContext(), MenuActivity.class));
    	finish();
        return;
    }    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	Log.d("Navin-CalActivity", "onOptionsItemSelected");
    	
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
        case R.id.menu_conversation:
        	startActivity(new Intent(getApplicationContext(), JobHuntContactActivity.class));
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
	public void receiveRecordList(List<Record> records) {
		// Assign list to instance var to be used by ListView.onClickListener
		mRecords = records;
		
		
		Log.d("Navin-CalActivity", "receiveRecordList");
		if(records.isEmpty()) {
			// Let user know the cupboard is bare
			Toast.makeText(getApplicationContext(), MESSAGE_NO_ITEMS, Toast.LENGTH_LONG).show();
		}
		// Initialise adapter with received record list
		CalendarAdapter adapter = new CalendarAdapter(this, records);
		// Attach adapter to ListView
		getListView().setAdapter(adapter);
	}
	
    /**
     * Detect gestures/swipes made on screen
     *
     */
    class CalendarGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(CalendarActivity.this, "Right to Left Swipe", Toast.LENGTH_SHORT).show();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(CalendarActivity.this, "Left to Right Swipe", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(LOGTAG, e.getLocalizedMessage(), e);
            }
            return false;
        }

    }

}
