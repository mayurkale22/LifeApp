
package com.social.customui;

import com.szugyi.circlemenu.R;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.SocialAuthAdapter;

import com.social.customadapter.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ProfileActivity extends Activity {

	// SocialAuth Components
	SocialAuthAdapter adapter;
	Profile profileMap;

	// Android Components
	TextView name;
	TextView displayName;
	TextView email;
	TextView location;
	TextView gender;
	TextView language;
	TextView country;
	ImageView image;

	// Variables
	String provider_name;
	ImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

		profileMap = (Profile) getIntent().getSerializableExtra("profile");
		Log.d("Custom-UI", "Validate ID = " + profileMap.getValidatedId());
		Log.d("Custom-UI", "First Name  = " + profileMap.getFirstName());
		Log.d("Custom-UI", "Last Name   = " + profileMap.getLastName());
		Log.d("Custom-UI", "Email       = " + profileMap.getEmail());
		Log.d("Custom-UI", "Gender  	 = " + profileMap.getGender());
		Log.d("Custom-UI", "Country  	 = " + profileMap.getCountry());
		Log.d("Custom-UI", "Language  	 = " + profileMap.getLanguage());
		Log.d("Custom-UI", "Location 	 = " + profileMap.getLocation());
		Log.d("Custom-UI", "Profile Image URL  = " + profileMap.getProfileImageURL());

		provider_name = getIntent().getStringExtra("provider");

		// Set title
		name = (TextView) findViewById(R.id.name);
		displayName = (TextView) findViewById(R.id.displayName);
		email = (TextView) findViewById(R.id.email);
		location = (TextView) findViewById(R.id.location);
		gender = (TextView) findViewById(R.id.gender);
		language = (TextView) findViewById(R.id.language);
		country = (TextView) findViewById(R.id.country);
		image = (ImageView) findViewById(R.id.imgView);

		imageLoader = new ImageLoader(ProfileActivity.this);

		imageLoader.DisplayImage(profileMap.getProfileImageURL(), image);

		// Name:
		// Facebook, Instagram returns : FullName,FirstName,Last Name
		// MySpace, SalesForce, Yahoo, Google, FourSquare,
		// Linkedin returns : First , Last Name
		// RunKeeper : First Name , Full Name
		// Yammer, Twitter, Flickr : FullName

		if (profileMap.getFullName() == null)
			name.setText("Name                  :  " + profileMap.getFirstName() + profileMap.getLastName());
		else
			name.setText("Name                  :  " + profileMap.getFullName());

		// Display Name
		// Return by Twitter, MySpace, Yahoo , SalesForce, Flickr, Instagram
		if (provider_name.equalsIgnoreCase("twitter") || provider_name.equalsIgnoreCase("myspace")
				|| provider_name.equalsIgnoreCase("yahoo") || provider_name.equalsIgnoreCase("salesforce")
				|| provider_name.equalsIgnoreCase("flickr") || provider_name.equalsIgnoreCase("instagram"))
			displayName.setText("Display Name  :  " + profileMap.getDisplayName());
		else
			displayName.setVisibility(View.GONE);

		// Email
		// Return by Facebook, Twitter, Linkedin, Google, GooglePlus, Yammer,
		// Yahoo, FourSquare, Salesforce
		if (provider_name.equalsIgnoreCase("facebook") || provider_name.equalsIgnoreCase("linkedin")
				|| provider_name.equalsIgnoreCase("google") || provider_name.equalsIgnoreCase("googleplus")
				|| provider_name.equalsIgnoreCase("foursquare") || provider_name.equalsIgnoreCase("salesforce")
				|| provider_name.equalsIgnoreCase("yahoo") || provider_name.equalsIgnoreCase("yammer"))
			email.setText("Email                  :  " + profileMap.getEmail());
		else
			email.setVisibility(View.GONE);

		// Location
		// Return by Facebook, MySpace, Linkedin, Yammer,Runkeeper
		// Yahoo, FourSquare, Salesforce
		if (provider_name.equalsIgnoreCase("facebook") || provider_name.equalsIgnoreCase("linkedin")
				|| provider_name.equalsIgnoreCase("myspace") || provider_name.equalsIgnoreCase("runkeeper")
				|| provider_name.equalsIgnoreCase("foursquare") || provider_name.equalsIgnoreCase("yahoo")
				|| provider_name.equalsIgnoreCase("yammer"))
			location.setText("Location            :  " + profileMap.getLocation());
		else
			location.setVisibility(View.GONE);

		// Gender
		// Return by FB, Yahoo, Runkeeper , FourSquare
		if (provider_name.equalsIgnoreCase("facebook") || provider_name.equalsIgnoreCase("runkeeper")
				|| provider_name.equalsIgnoreCase("yahoo") || provider_name.equalsIgnoreCase("foursquare"))
			gender.setText("Gender               :  " + profileMap.getGender());
		else
			gender.setVisibility(View.GONE);

		// Language
		// Return by Fcaebook, Twitter, Google, GooglePlus, Salesforce, Myspace,
		// Yahoo
		if (provider_name.equalsIgnoreCase("facebook") || provider_name.equalsIgnoreCase("twitter")
				|| provider_name.equalsIgnoreCase("google") || provider_name.equalsIgnoreCase("googleplus")
				|| provider_name.equalsIgnoreCase("salesforce") || provider_name.equalsIgnoreCase("myspace")
				|| provider_name.equalsIgnoreCase("yahoo"))

			language.setText("Language          :  " + profileMap.getLanguage());
		else
			language.setVisibility(View.GONE);

		// Country
		// Return by FB , Google , SalesForce, Flickr
		if (provider_name.equalsIgnoreCase("facebook") || provider_name.equalsIgnoreCase("google")
				|| provider_name.equalsIgnoreCase("salesforce") || provider_name.equalsIgnoreCase("flickr"))
			country.setText("Country            :  " + profileMap.getCountry());
		else
			country.setVisibility(View.GONE);
	}
}