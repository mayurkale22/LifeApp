package com.mainpanel.view;


import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.os.AsyncTask;
import android.util.Log;



/**
 * Example for accessing the Yelp API.
 */
public class Yelp extends AsyncTask<String,Void,Response>{

  OAuthService service;
  Token accessToken;
  double longitude;
  double latitude;
  int sort_value = 1;
  String term ;
  /**
   * Setup the Yelp API OAuth credentials.
   *
   * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
   *
   * @param consumerKey Consumer key
   * @param consumerSecret Consumer secret
   * @param token Token
   * @param tokenSecret Token secret
   */
  public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret,String term,double latitude,double longitude) {
    this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
    this.accessToken = new Token(token, tokenSecret);
    this.term = term;
    this.latitude = latitude;
    this.longitude = longitude;
    
  }

  /**
   * Search with term and location.
   *
   * @param term Search term
   * @param latitude Latitude
   * @param longitude Longitude
   * @return JSON string response
   */
  public String search(String term, double latitude, double longitude) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("ll", latitude + "," + longitude);
   
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

@Override
protected Response doInBackground(String... arg0) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("ll", latitude + "," + longitude);
    request.addQuerystringParameter("limit", "3");

    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    Log.d("datastore",response.getBody());
    return response;
}

  // CLI
/*  public static void main(String[] args) {
    // Update tokens here from Yelp developers site, Manage API access.
    String consumerKey = "1YEeYECd46tQZRCKYNwP4w";
    String consumerSecret = "JepkDXR_f-JsGx3WJbMJkh7mxu8";
    String token = "v2egbk17U67qhrgjzWl3CwT89MXla9jH";
    String tokenSecret = "L5Oy3aHjUMFw5ZNhJc7HqEN34ck";

    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
    String response = yelp.search("burritos", 30.361471, -87.164326);

    System.out.println(response);
    
   
  }*/
  
  
}


