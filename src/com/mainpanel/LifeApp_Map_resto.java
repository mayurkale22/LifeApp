package com.mainpanel;
import java.io.IOException;
import java.io.InputStream;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.mainpanel.view.Yelp;

public class LifeApp_Map_resto extends Activity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.resto_info);
		super.onCreate(savedInstanceState);
		TextView title = (TextView) findViewById(R.id.title);
		TextView description = (TextView) findViewById(R.id.description);
		TextView resto_addr = (TextView) findViewById(R.id.address);
		SmartImageView resto_img = (SmartImageView) findViewById(R.id.resto_image);
		SmartImageView resto_img1 = (SmartImageView) findViewById(R.id.resto_image1);

		RatingBar rating=(RatingBar) findViewById(R.id.place_rating);
		TextView resto_phoneno = (TextView) findViewById(R.id.phone);
		
		Bundle bundle = getIntent().getExtras();
		String search = bundle.getString("title");
	    double latitude = bundle.getDouble("latitude");
	    double longitude = bundle.getDouble("longitude");
		String rating1 = bundle.getString("rating");
		String main_img_url = bundle.getString("main_img");
		String address = bundle.getString("address");

        
        String Consumer_Key = "bqVTGamncnhATboNqTJitQ";
        String Consumer_Secret= "wnxRf8dqICfyb9XUyMtogKm4jx0";
        String Token = "wnaTN53KLW_Mjl6wGz78zqI7ofuwmRk0";
        String Token_Secret =	"DRWlDmfqNm5fQnxzLALxjrydy2M";
        
//        String search = "Five Guys Burgers and Fries";
//        double latitude = 40.693614;
//        double longitude = -73.98571;
//        String rating1 = "4";
//        String address = "new addr";
        
        String rawData = null;
        Yelp yelp_obj = new Yelp(Consumer_Key,Consumer_Secret,Token,Token_Secret,search,latitude,longitude);
        try {
        	rawData = yelp_obj.execute().get().getBody();
		} catch (Exception e) {

		}
        String value = "";
        String resto_img_url = null;
        String snippet_text= "";
        String resto_phone= "";
        String resto_review= "";
        int i = 0;
		try {
            JSONObject json = new JSONObject(rawData);
            JSONArray businesses;
            businesses = json.getJSONArray("businesses");
            for (i = 0; i < businesses.length(); i++) {
                    JSONObject business = businesses.getJSONObject(i);
                    snippet_text += business.getString("snippet_text") + "\n________________________________________________" + "\n"; 
                    resto_img_url = business.getString("image_url");
                    resto_phone = "Phone Number : " + businesses.getJSONObject(0).getString("display_phone");
                    
                    
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        title.setText(search);
        description.setText(snippet_text);
        resto_addr.setText(address);
        resto_phoneno.setText(resto_phone);
        resto_img.setImageUrl(main_img_url);
        resto_img1.setImageUrl(resto_img_url);
        rating.setRating(Float.parseFloat(rating1));
       
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}

}
