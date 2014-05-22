package com.mainpanel.view;

import android.app.ActionBar;
import android.os.Bundle;

import com.szugyi.circlemenu.R;

import android.preference.PreferenceActivity;

public class UserSettingActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);
	    ActionBar actionBar = getActionBar();
	    actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
	    
	}
	
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition (R.anim.card_flip_right_in, R.anim.card_flip_right_out);
	}
}
