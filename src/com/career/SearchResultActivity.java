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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class SearchResultActivity extends ListActivity implements DbReadObserver{

	private final static boolean READ_MODE_SEARCH = false;
	private final static String MESSAGE_NO_ITEMS = "No items were found";
	
	private List<Record> mRecords;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_hunt_calendar);
        
        // Fetch bundle from Intent
        Bundle extras = getIntent().getExtras();
        String tableColumn = extras.getString(RecordHolder.SEARCH_COLUMN_NAME);
        String searchItem = extras.getString(RecordHolder.SEARCH_ITEM);
        
        // Initialise DB task
        ReadDbAsyncTask dbTask = new ReadDbAsyncTask(getApplicationContext(), this, READ_MODE_SEARCH);
        // Run DB task to fetch entries from DB
        dbTask.execute(tableColumn, searchItem);
        
        getListView().setOnItemClickListener(new OnItemClickListener() {
      	  @Override
      	  public void onItemClick(AdapterView<?> parent, View view,
      	    int position, long id) {
      	    
      	    Record record = mRecords.get(position);
      	    RecordHolder recordHolder = new RecordHolder(record);
      	    Intent intent = null;
      	    // Initialise Intent with relevant destination
      	    if(record.getRecordType().equals(RecordHolder.RECORD_TYPE_CONV)) {
      	    	intent = new Intent(getApplicationContext(), JobHuntContactActivity.class);
      	    } else {
      	    	intent = new Intent(getApplicationContext(), JobInterviewActivity.class);
      	    }
      	    intent.putExtra(RecordHolder.KEY_RECORD, recordHolder.recordToJson());
      	    // Load Bundle with record data that is to be printed to destination screen
      	    /*intent.putExtra(Record.KEY_DATE, record.getDateScreen());
      	    intent.putExtra(Record.KEY_TIME, record.getTimeScreen());
      	    intent.putExtra(Record.KEY_COMMENTS, record.getComments());
      	    intent.putExtra(Record.KEY_JOB_CO, record.getJobCompany());
      	    intent.putExtra(Record.KEY_JOB_TITLE, record.getJobTitle());
      	    intent.putExtra(Record.KEY_RECR_CO, record.getRecruiterCompany());
      	    intent.putExtra(Record.KEY_RECR_NAME, record.getRecruiterName());
      	    intent.putExtra(Record.KEY_ID, record.getId());*/
      	    startActivity(intent);
      	    finish();
      	  }
      	});
        
	}
	
	@Override
    public void onBackPressed() {
        // Move to Search screen if back button pressed
    	startActivity(new Intent(getApplicationContext(), SearchActivity.class));
    	finish();
        return;
    }	
	
	@Override
	public void receiveRecordList(List<Record> records) {
		// Assign list to instance var to be used by ListView.onClickListener
		mRecords = records;
				
		if(records.isEmpty()) {
		// Let user know the cupboard is bare
			Toast.makeText(getApplicationContext(), MESSAGE_NO_ITEMS, Toast.LENGTH_LONG).show();
		}
		// Initialise adapter with received record list
		CalendarAdapter adapter = new CalendarAdapter(this, records);
		// Attach adapter to ListView
		getListView().setAdapter(adapter);
		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_job_hunt_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
