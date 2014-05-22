package com.mainpanel;

import com.szugyi.circlemenu.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Activity for Pedometer settings.
 * Started when the user click Settings from the main menu.
 * @author Levente Bagi
 */
public class Settings extends PreferenceActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
        
        System.out.println("Settings onCreate call");
    }
}
