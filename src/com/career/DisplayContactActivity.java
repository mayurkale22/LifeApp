
package com.career;

import com.szugyi.circlemenu.R;
import android.app.Activity;
import android.os.Bundle;

public class DisplayContactActivity extends Activity{
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
