package com.career;

import com.career.db.DbHelper;
import com.career.db.Record;
import com.career.db.RecordHolder;

import com.szugyi.circlemenu.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchActivity extends Activity {
	
	private Spinner mSpinner;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_hunt_search);
        
        mSpinner  = (Spinner) findViewById(R.id.search_cat_spinner);
        
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_job_hunt_main, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
        // Move to Menu screen if back button pressed
    	startActivity(new Intent(getApplicationContext(), MenuActivity.class));
    	finish();
        return;
    } 
    
    /**
     * Callback method executed when Search button is clicked
     * @param v View representing the search button
     */
    public void buttonClicked(View v) {
    	String item = (String) mSpinner.getSelectedItem();
    	String tableColumn;
    	if(item.equals(RecordHolder.SEARCH_GOAL_TITLE)) {
    		tableColumn = DbHelper.COLUMN_GOAL_TITLE;
    	}
    	else if(item.equals(RecordHolder.SEARCH_JOB_CO)) {
    		tableColumn = DbHelper.COLUMN_JOB_COMPANY;
    	}
    	else if(item.equals(RecordHolder.SEARCH_JOB_TITLE)) {
    		tableColumn = DbHelper.COLUMN_JOB_TITLE;
    	}
    	else if(item.equals(RecordHolder.SEARCH_RECR_CO)) {
    		tableColumn = DbHelper.COLUMN_RECRUITER_COMPANY;
    	} else {
    		tableColumn = DbHelper.COLUMN_RECRUITER_NAME;
    	}
    	
    	EditText searchItemEdit = (EditText) findViewById(R.id.search_item_edit);
    	
    	Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
    	intent.putExtra(RecordHolder.SEARCH_COLUMN_NAME, tableColumn);
    	intent.putExtra(RecordHolder.SEARCH_ITEM, searchItemEdit.getText().toString());
    	
    	startActivity(intent);
    	finish();
        
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
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
}
