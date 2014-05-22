package com.mainpanel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.szugyi.circlemenu.R;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.mainpanel.view.Yelp;

public class LifeApp_Map_Event extends Activity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.event_info);
		super.onCreate(savedInstanceState);
		TextView title = (TextView) findViewById(R.id.title);
		WebView description = (WebView) findViewById(R.id.description);
		TextView resto_addr = (TextView) findViewById(R.id.address);
		TextView start_date = (TextView) findViewById(R.id.start_date);
		SmartImageView event_img = (SmartImageView) findViewById(R.id.event_image);
		
		Bundle bundle = getIntent().getExtras();
		String etitle = bundle.getString("title");
		String edesc = bundle.getString("desc");
		String emain_img_url = bundle.getString("main_img");
		String eaddress = bundle.getString("address");
		String estart_date = bundle.getString("start_date");
		
		
                
//        String etitle = "Five Guys Burgers and Fries";
//        String edesc = "4";
//        String eaddress = "new addr";
//        String emain_img_url = "";
        
        title.setText(etitle);
        description.loadData(edesc, "text/html", "UTF-8");
        resto_addr.setText("Venue : " + eaddress);
        start_date.setText("Start Date : " + estart_date);
        event_img.setImageUrl(emain_img_url);
        
        Log.d("event",emain_img_url);
       
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}

}
