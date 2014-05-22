package com.mainpanel.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
 
public class PlaceJSONParser {
 
    /** Receives a JSONObject and returns a list */
    public List<HashMap<String,String>> parse(JSONObject jObject){
 
        JSONArray jPlaces = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
        * where each json object represent a place
        */
        return getPlaces(jPlaces);
    }
 
    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> place = null;
 
        /** Taking each place, parses and adds to list object */
        for(int i=0; i<placesCount;i++){
            try {
                /** Call getPlace with place JSON object to parse the place */
                place = getPlace((JSONObject)jPlaces.get(i));
                placesList.add(place);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }
 
    /** Parsing the Place JSON object */
    private HashMap<String, String> getPlace(JSONObject jPlace){
 
        HashMap<String, String> place = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity="-NA-";
        String latitude="";
        String longitude="";
        String reference="";
        String rating="0";
        String photo_reference="";
        boolean now_open = false;
        String types = "Types : ";
        
        JSONArray json_result;
        try {
            // Extracting Place name, if available
            if(!jPlace.isNull("name")){
                placeName = jPlace.getString("name");
            }
 
            // Extracting Place Vicinity, if available
            if(!jPlace.isNull("vicinity")){
                vicinity = jPlace.getString("vicinity");
            }
 
            latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = jPlace.getString("reference");
            
            if(!jPlace.isNull("rating")){
            	rating = jPlace.getString("rating");
            }

            if(!jPlace.isNull("photos")){
            	
            	json_result = jPlace.getJSONArray("photos");
            	for (int i = 0; i < json_result.length(); i++) {
            	    JSONObject c = json_result.getJSONObject(i);
            	    photo_reference = c.getString("photo_reference");
            	}
            }
            
            if(!jPlace.isNull("types")){
            	
            	JSONArray json_result_type = jPlace.getJSONArray("types");
            	for (int i = 0; i < json_result_type.length(); i++) {
            		if (!json_result_type.getString(i).equals("establishment")){
            			types += json_result_type.getString(i).substring(0, 1).toUpperCase() + json_result_type.getString(i).substring(1) + ", ";
            		}
            	}
            	types += "Table Service";
            }
            now_open = jPlace.getJSONObject("opening_hours").getBoolean("open_now");
            
            place.put("place_name", placeName);
            place.put("vicinity", vicinity);
            place.put("lat", latitude);
            place.put("lng", longitude);
            place.put("reference", reference);
            place.put("rating", rating);
            place.put("photo_reference",photo_reference);
            place.put("now_open",Boolean.toString(now_open));
            place.put("types",types);
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
