package com.career.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.google.gson.Gson;

import android.util.Log;

public class RecordHolder {
	
	private final static String LOGTAG = RecordHolder.class.getName();
	
	public final static String RECORD_TYPE_GOAL = "Goal";
	public final static String RECORD_TYPE_CONV = "Conversation";
	public final static String RECORD_TYPE_INT = "Interview";
	public final static String KEY_ID = "id";
	public final static String KEY_RECORD_TYPE = "RecordType";
	public final static String KEY_TIME = "Time";
	/**
	 * Key corresponding to Record serialised to JSON
	 */
	public final static String KEY_RECORD = "Record";
	public final static String KEY_DATE = "Date";
	public final static String KEY_JOB_TITLE = "JobTitle";
	public final static String KEY_JOB_CO = "JobCompany";
	public final static String KEY_RECR_NAME = "RecrName";
	public final static String KEY_RECR_CO = "RecrCompany";
	public final static String KEY_COMMENTS = "Comments";
	public final static String KEY_JOB_ADDRESS = "JobAddress";
	public final static String KEY_JOB_POSTCODE = "JobPostcode";
	public final static String KEY_GOAL_TITLE = "GoalTitle";
	public final static String KEY_GOAL_DESC = "GoalDesc";
	public final static String KEY_GOAL_LOC = "GoalLoc";
	
	public final static String SEARCH_GOAL_TITLE = "Goal Title";
	public final static String SEARCH_RECR_CO = "Recruiter Company";
	public final static String SEARCH_RECR_NAME = "Recruiter Name";
	public final static String SEARCH_JOB_CO = "Job Company";
	public final static String SEARCH_JOB_TITLE = "Job Title";
	public final static String SEARCH_COLUMN_NAME = "col_name";
	public final static String SEARCH_ITEM = "search_item";
	
	public final static String DATEFORMAT_SCREEN_DATE = "dd/MM/yyyy";
	public final static String DATEFORMAT_SCREEN_TIME = "HH:mm";
	private final static String DATEFORMAT_DB = "yyyy-MM-dd HH:mm:ss";
	private final static String DATEFORMAT_SCREEN = "dd/MM/yyyy HH:mm";
	private final SimpleDateFormat dbFormat = new SimpleDateFormat(DATEFORMAT_DB);
	private final SimpleDateFormat screenDateTimeFormat = new SimpleDateFormat(DATEFORMAT_SCREEN);
	private final SimpleDateFormat screenDateFormat = new SimpleDateFormat(DATEFORMAT_SCREEN_DATE);
	private final SimpleDateFormat screenTimeFormat = new SimpleDateFormat(DATEFORMAT_SCREEN_TIME);
	
	public RecordHolder(Record record) {
		mRecord = record;
	}
	
	public RecordHolder(String json) {
		Gson gson = new Gson();
		mRecord = gson.fromJson(json, Record.class);
	}
	
	private Record mRecord;
	
	public Record getRecord() {
		return mRecord;
	}
	
	public String recordToJson() {
		Gson gson = new Gson();
		
		//Log.d(DataUtils.class.getSimpleName(), "Gson formatted JSON: "+gson.toJson(sHolder, SuspectHolder.class));
		return gson.toJson(mRecord, Record.class);
	}
	
	public static Record jsonToList(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Record.class);
	}
	public void setmRecord(Record record) {
		mRecord = record;
	}
	public String getDateTimeDb() {
		
		return dbFormat.format(mRecord.getDateTime());
	}
	public void setDateTimeDb(String dateTime) {
		try {
			mRecord.setDateTime(dbFormat.parse(dateTime));
		} catch (ParseException e) {
			Log.e(LOGTAG, e.getMessage(),e);
		}
	}
	public String getDateTimeScreen() {
		return screenDateTimeFormat.format(mRecord.getDateTime());
	}
	public void setDateTimeScreen(String dateTime) {
		try {
			mRecord.setDateTime(screenDateTimeFormat.parse(dateTime));
		} catch (ParseException e) {
			Log.e(LOGTAG, e.getMessage(),e);
		}
	}
	
	public String getDateScreen() {
		return screenDateFormat.format(mRecord.getDateTime());
	}
	
	public String getTimeScreen() {
		return screenTimeFormat.format(mRecord.getDateTime());
	}
}
