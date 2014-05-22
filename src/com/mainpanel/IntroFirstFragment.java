package com.mainpanel;

import com.szugyi.circlemenu.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mainpanel.view.GlowingText;

/**
 * This Fragment shows the first screen of the Guestbook introduction.
 */
public class IntroFirstFragment extends Activity implements OnClickListener {

    public static final String TAG = "FIRST";
    CountDownTimer countdowntimer;
    private ImageView mNextBtn;
    private TextView countdown;
    boolean flag = true;

	float 	startGlowRadius = 25f,
			minGlowRadius   = 3f,
			maxGlowRadius   = 15f;

	// Create an Instance of GlowingText class;
	GlowingText glowText,glowButton;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_intro_first);
		countdown = (TextView) findViewById(R.id.countdown);
        mNextBtn = (ImageView) findViewById(R.id.intro_1_next_btn);
        mNextBtn.setOnClickListener(this);
        
//        Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.coutdown_anim);
//        countdown.startAnimation(animation);
        
        // Start Glowing :D
        glowText = new GlowingText(
        		IntroFirstFragment.this,           // Pass activity Object
        		getBaseContext(),   // Context
        		countdown,           // TextView
        		minGlowRadius,      // Minimum Glow Radius
        		maxGlowRadius,      // Maximum Glow Radius
        		startGlowRadius,    // Start Glow Radius - Increases to MaxGlowRadius then decreases to MinGlowRadius.
        		Color.BLUE,         // Glow Color (int)
        		1); 
        
        // You can also use to set data dynamically
        glowText.setGlowColor(Color.WHITE);  //(int : 0xFFffffff)
        glowText.setStartGlowRadius(5f);
        glowText.setMaxGlowRadius(15f); 
        glowText.setMinGlowRadius(3f);
        
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
            	if (flag){
	                // TODO: Your application init goes here.
	                Intent mInHome = new Intent(IntroFirstFragment.this, LifeApp.class);
	                IntroFirstFragment.this.startActivity(mInHome);
	                IntroFirstFragment.this.finish();
	                overridePendingTransition (R.anim.open_next, R.anim.close_main);
            	}
            }
        }, 6000);
        
        countdowntimer = new CountDownTimer(6000, 1000) {

            public void onTick(long millisUntilFinished) {
            	countdown.setText("HOME IN " + millisUntilFinished / 1000 + " SECS");
            }

            public void onFinish() {
            	countdown.setText("HERE WE GO...");
            }
            
         }.start();        
         

    }
    


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.intro_1_next_btn:
            	flag = false;
	    		Intent intent = new Intent(IntroFirstFragment.this, LifeApp.class);
	            startActivity(intent);
	            overridePendingTransition (R.anim.open_next, R.anim.close_main);
            	
                break;
        }
    }
}
