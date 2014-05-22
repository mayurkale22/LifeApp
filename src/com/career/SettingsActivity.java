package com.career;

import com.szugyi.circlemenu.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class SettingsActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_hunt_search);
        
        
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_job_hunt_main, menu);
        return true;
    }
}
