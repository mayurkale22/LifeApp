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
import android.app.ProgressDialog;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.mainpanel.view.Yelp;

public class LifeApp_Map_Places extends Activity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.place_info);
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
		String ptitle = bundle.getString("title");
	
		
                
//        String etitle = "Five Guys Burgers and Fries";
//        String edesc = "4";
//        String eaddress = "new addr";
//        String emain_img_url = "";
        
		

		new AsyncTask<String, Void, String>(){
			//Before running code in separate thread  
			private ProgressDialog progressDialog;
	        @Override  
	        protected void onPreExecute()  
	        {  
	            //Create a new progress dialog  
	        	progressDialog = ProgressDialog.show(LifeApp_Map_Places.this,"Loading...",  
	        		    "Loading application View, please wait...", false, false);   
	        }  
			
			
			  @Override
			  protected String doInBackground(String... urlStr){
			    // do stuff on non-UI thread
				  String htmlCode = "";
			    try{
			      URL url = new URL(urlStr[0]);
			      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			      String inputLine;
			      int flag = 0;
			      while ((inputLine = in.readLine()) != null) {
			        if (inputLine.toLowerCase().contains("see also") || inputLine.toLowerCase().contains("references") || inputLine.toLowerCase().contains("external links")){
			        	flag += 1; 
			        }else{
			        	 htmlCode += inputLine;
			        }
			        
			        if (flag == 4){ break;}
			    	 
			      }

			      in.close();
			    } catch (Exception e) {
			        e.printStackTrace();
			        Log.d("ping", "Error: " + e.getMessage());
			        Log.d("ping", "HTML CODE: " + htmlCode);
			    }
			    return htmlCode.toString();
			  }         
			 

			  @Override
			  protected void onPostExecute(String htmlCode){
				  progressDialog.dismiss(); 
				  WebView description = (WebView) findViewById(R.id.description);
				  htmlCode = htmlCode.replace("src=\"//", "src=\"http://");
				  htmlCode = htmlCode.replace("<a", "<lifeapp");
				  description.loadData(htmlCode, "text/html", "UTF-8");
			  }

			}.execute("http://en.wikipedia.org/w/index.php?title="+ptitle.replace(" ", "%20")+"&action=render");

       
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}

}
