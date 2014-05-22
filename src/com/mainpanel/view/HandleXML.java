package com.mainpanel.view;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class HandleXML {

   private String event_title = "";
   private String Lat_Long = "";
   private String logo_url ="";
   private String event_details = "";
   private String urlString = null;
   private String start_date= null;
   private String desc = "";
   private String venue = "";
   
   private XmlPullParserFactory xmlFactoryObject;
   public volatile boolean parsingComplete = true;
   public HandleXML(String url){
      this.urlString = url;
   }
   public String getevent_title(){
      return event_title;
   }

   public String getevent_details(){
	      return event_details;
	   }

   public void parseXMLAndStoreIt(XmlPullParser myParser) {
      int event;
      String text=null;
      
      try {
         event = myParser.getEventType();
         while (event != XmlPullParser.END_DOCUMENT) {
            String name=myParser.getName();
            
            switch (event){
               case XmlPullParser.START_TAG:
               break;
               case XmlPullParser.TEXT:
               text = myParser.getText();
               break;

               case XmlPullParser.END_TAG:
                  if(name.equals("title")){
                	  event_title = text;
                	  
                  }
                  else if(name.equals("logo")){
                	  logo_url = text;
    	        	   
                   }
                  else if(name.equals("start_date")){
                	  start_date = text;
    	        	   
                   }
                  else if(name.equals("description")){
                	  if (desc.equals("")){
                		  desc = text;
                	  }
                	  
                   }
                  else if(name.equals("name")){
                	  	venue =text +", ";
    	        	   
                   }
                  else if(name.equals("address")){
                	  venue += text + ", ";
    	        	   
                   }
                  else if(name.equals("city")){
                	  venue += text + ", ";
                	  
                   }
                  
                  else if(name.equals("Lat-Long")){
                	  Lat_Long = text;
                	  event_details += event_title + "lifeapp#" + Lat_Long + "lifeapp#" + 
       			  		   logo_url + "lifeapp#" + start_date + "lifeapp#" + desc + "lifeapp#"+ venue +"eventend#";
                	  desc ="";

                   }
                  else{
                  }
                  break;
                  }		 
            	

                  event = myParser.next(); 
                  

              }
                 parsingComplete = false;
      } catch (Exception e) {
         e.printStackTrace();
      }

   }
   public void fetchXML(){
      Thread thread = new Thread(new Runnable(){
         @Override
         public void run() {
            try {
               URL url = new URL(urlString);
               HttpURLConnection conn = (HttpURLConnection) 
               url.openConnection();
                  conn.setReadTimeout(10000 /* milliseconds */);
                  conn.setConnectTimeout(15000 /* milliseconds */);
                  conn.setRequestMethod("GET");
                  conn.setDoInput(true);
                  conn.connect();
            InputStream stream = conn.getInputStream();

            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
            , false);
            myparser.setInput(stream, null);
            parseXMLAndStoreIt(myparser);
            stream.close();
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
    });

    thread.start(); 


   }

}