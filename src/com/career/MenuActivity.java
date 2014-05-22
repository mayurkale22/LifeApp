package com.career;

import com.szugyi.circlemenu.R;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Launch Activity displaying application menu.
 * 
 * The JobHunt app allows the user to enter details of contact
 * with recruiters regarding job applications.
 * 
 * The user can also enter details of interviews that are arranged
 * 
 * A calendar displays all contact and interview events
 * 
 * The user can search on criteria such as job title, recruiter name etc
 *
 *
 */
public class MenuActivity extends ListActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_hunt_menu);
        
        MenuAdapter adapter = new MenuAdapter(this, makeMenuItems());
        setListAdapter(adapter);
        
        getListView().setOnItemClickListener(new OnItemClickListener() {
      	  /* (non-Javadoc)
      	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
      	 */
      	@Override
      	  public void onItemClick(AdapterView<?> parent, View view,
      	    int position, long id) {
      	    
      	    Intent intent = null;
      	    
      	    switch (position) {
      	    // Send user to Goals activity
	        case 0: intent = new Intent(getApplicationContext(), GoalEdit.class);
	    			startActivity(intent);
	    			finish();
	    			break;
      	    // Send user to Conversation activity
      	    case 1: intent = new Intent(getApplicationContext(), JobHuntContactActivity.class);
      	    		startActivity(intent);
      	    		finish();
      	    		break;
      	    // Send user to Interview activity
      	    case 2: intent = new Intent(getApplicationContext(), JobInterviewActivity.class); 
      	    		startActivity(intent);
      	    		finish();
      	    		break;
      	    // Send user to Calendar activity
      	    case 3:	intent = new Intent(getApplicationContext(), CalendarActivity.class); 
	    			startActivity(intent);
	    			finish();
	    			break;
	    	// Send user to Search activity
      	    case 4:	intent = new Intent(getApplicationContext(), SearchActivity.class); 
	    			startActivity(intent);
	    			finish();
	    			break;
      	    }
      	  }
      	});
        
	}
        
    /**
     * Create an array containing menu items from resource strings.xml
     * @return String[] containing menu items
     */
    private String[] makeMenuItems() {
    	
    	String goalItem = getApplicationContext().getResources().getString(R.string.menu_goal);
    	String conversationItem = getApplicationContext().getResources().getString(R.string.menu_contact);
    	String interviewItem = getApplicationContext().getResources().getString(R.string.menu_interview);    	
    	String calendarItem = getApplicationContext().getResources().getString(R.string.menu_calendar);
    	String searchItem = getApplicationContext().getResources().getString(R.string.menu_search);
    	
    	String[] menu_items = {goalItem, conversationItem, interviewItem, calendarItem, searchItem};
    	
        return menu_items;
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
        case R.id.menu_goal_set:
        	startActivity(new Intent(getApplicationContext(), GoalEdit.class));
        	finish();
            return true; 
        case R.id.menu_conversation:
        	startActivity(new Intent(getApplicationContext(), JobHuntContactActivity.class));
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

}
