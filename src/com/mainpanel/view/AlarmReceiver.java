package com.mainpanel.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

     @Override
     public void onReceive(Context context, Intent intent) {
    	 Log.d("ping","in AlarmReceiver");
     // When our Alaram time is triggered , this method will be excuted (onReceive)
     // We're invoking a service in this method which shows Notification to the User
      Intent myIntent = new Intent(context, PingService.class);
      myIntent.setAction(CommonConstants.ACTION_PING);
      context.startService(myIntent);
    }

} 