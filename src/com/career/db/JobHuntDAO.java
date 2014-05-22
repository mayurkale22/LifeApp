package com.career.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Class using Data Access Object design pattern to
 * enable DB read and write access and map data to
 * model objects
 * 
 *
 */
public class JobHuntDAO {

	private SQLiteDatabase mDatabase;
	private DbHelper mDbHelper;
	
	private final static String LOGTAG = JobHuntDAO.class.getName(); 
	
	private final static String[] ALLCOLUMNS = {DbHelper.COLUMN_ID, 
			DbHelper.COLUMN_RECORD_TYPE,
			DbHelper.COLUMN_RECORD_DATETIME,
			DbHelper.COLUMN_JOB_TITLE,
			DbHelper.COLUMN_JOB_COMPANY,
			DbHelper.COLUMN_RECRUITER_NAME,
			DbHelper.COLUMN_RECRUITER_COMPANY,
			DbHelper.COLUMN_RECORD_JOB_ADDRESS,
			DbHelper.COLUMN_RECORD_JOB_POSTCODE,
			DbHelper.COLUMN_RECORD_COMMENTS,
			DbHelper.COLUMN_GOAL_LOC,
			DbHelper.COLUMN_GOAL_TITLE,
			DbHelper.COLUMN_GOAL_DESC};
	
	
	private final String[] mAllGoalColumns = {DbHelper.COLUMN_ID,
											DbHelper.COLUMN_GOAL_TITLE,
											DbHelper.COLUMN_GOAL_DESC,
											DbHelper.COLUMN_GOAL_LOC};
	
	// TODO Use when splitting table into Record, Job, Recruiter
	private final String[] mAllJobColumns = {DbHelper.COLUMN_ID, 
											DbHelper.COLUMN_JOB_TITLE,
											DbHelper.COLUMN_JOB_COMPANY};
	
	// TODO Use when splitting table into Record, Job, Recruiter
	private final String[] mAllRecruiterColumns = {DbHelper.COLUMN_ID,
												DbHelper.COLUMN_RECRUITER_NAME,
												DbHelper.COLUMN_RECRUITER_COMPANY};
	
	// TODO Use when splitting table into Record, Job, Recruiter
	private final String[] mAllRecordColumns = {DbHelper.COLUMN_ID,
												DbHelper.COLUMN_RECORD_DATETIME,
												DbHelper.COLUMN_RECORD_TYPE,
												DbHelper.COLUMN_RECORD_JOB_ID,
												DbHelper.COLUMN_RECORD_RECRUITER_ID,
												DbHelper.COLUMN_RECORD_COMMENTS};
	
	
	public JobHuntDAO(Context context) {
		
		Log.d("Navin-DAO", "constructor");
		
		mDbHelper = new DbHelper(context);
	}
	
	/**
	 * Open database
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		
		Log.d("Navin-DAO", "open");
		
		mDatabase = mDbHelper.getWritableDatabase();
	}
	
	/**
	 * Close database
	 */
	public void close() {
		
		Log.d("Navin-DAO", "close");
		
		mDbHelper.close();
	}
	
	/**
	 * Write user-entered data to database
	 * 
	 * @param record Record containing user-entered details
	 * @return boolean True if DB write was successful
	 */
	public boolean addRecord(Record record) {
		boolean success = false;		
				
		
		// Load user-entered values into ContentValues instance
		ContentValues values = recordToContentValues(record);
		Log.d("Navin-DAO-addRecord", String.valueOf(values));
		
		// Insert values into DB and store return value
		long insertId = mDatabase.insert(DbHelper.TABLE_JOB_RECORD, null, values);
					
		// DB write successful if value not -1
		if(insertId != -1) success = true;
		
		Log.d("Navin-DAO-addRecord-record.getId()", String.valueOf(record.getId()));
		Log.d("Navin-DAO-addRecord-String.valueOf(insertId)", String.valueOf(insertId));
		
		return success;
	}
	
	/**
	 * Update user-entered data to database
	 * 
	 * @param record Record containing user-entered details
	 * @return boolean True if DB write was successful
	 */
	public boolean updateRecord(Record record) {
		
		Log.d("Navin-DAO", "updateRecord");
		
		boolean success = false;
		// Load user-entered values into ContentValues instance
		long id= record.getId();
		
		Log.d("Navin-DAO", "updateRecord-id"+record.getId());
		
		record.setId(id);
		
		Log.d("Navin-DAO", "updateRecord-id"+record.getId());
		
		ContentValues values = updateRecordToContentValues(record);
		Log.d("Navin-DAO-updateRecord-String.valueOf(values)", String.valueOf(values));
		
		// Insert values into DB and store return value
		String where = DbHelper.COLUMN_ID + " = ?" ;
		
		Log.d("Navin-DAO-updateRecord", String.valueOf(id));
		
		String[] change = new String[1];
		change[0]= String.valueOf(record.getId());
		
		long insertId = mDatabase.update(DbHelper.TABLE_JOB_RECORD, values, where, change);
		
		// DB write successful if value not -1
		if(insertId != -1) success = true;
		Log.d("Navin-DAO-updateRecord", String.valueOf(insertId));
				
		return success;
	}
	
	/**
	 * Delete DB entry
	 * 
	 * @param record Record containing data to be deleted
	 */
	public boolean deleteRecord(Record record) {
		
		Log.d("Navin-DAO", "deleteRecord");
		
		boolean success = false;
		
		// Remove record based on id
		long insertId = mDatabase.delete(DbHelper.TABLE_JOB_RECORD, DbHelper.COLUMN_ID
				+" = "+ record.getId(), null);
		
		if(insertId!=-1) success = true;
		
		return success;
	}
	
	/**
	 * Fetch all records from database
	 * 
	 * @return List<Record> containing all records contained in database
	 */
	public List<Record> getRecords() {
		
		Log.d("Navin-DAO", "getRecords");
		
		List<Record> records = new ArrayList<Record>();
		
		String orderBy = DbHelper.COLUMN_RECORD_DATETIME + " ASC";
				
		Cursor cursor = mDatabase.query(DbHelper.TABLE_JOB_RECORD,
				ALLCOLUMNS, null, null, null, null, orderBy);
		
		cursor.moveToFirst();		
		
		//while (!cursor.isAfterLast()) {
		for(int i=0;i<cursor.getCount();i++){
			Record record = cursorToRecord(cursor);
			records.add(record);
			cursor.moveToNext();
		}		
		
		cursor.close();
		
		return records;
	}
	
	
	
	
	
	
	/**
	 * Fetch records from database between 2 dates
	 * 
	 * @param startDate String, start date of time period
	 * @param endDate String, end date of time period 
	 * @return List<Record> containing records contained in database between given dates
	 */
	public List<Record> getRecords(String startDate, String endDate) {
		
		Log.d("Navin-DAO", "getRecords2");
		
		List<Record> records = new ArrayList<Record>();
		
		String where = DbHelper.COLUMN_RECORD_DATETIME+ " BETWEEN ? AND ?";
		String[] whereArgs = {startDate, endDate};
		String orderBy = DbHelper.COLUMN_RECORD_DATETIME + " ASC";
		
		Cursor cursor = mDatabase.query(DbHelper.TABLE_JOB_RECORD,
				ALLCOLUMNS, where, whereArgs, null, null, orderBy);
		
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			Record record = cursorToRecord(cursor);
			records.add(record);
			//Log.d(LOGTAG, "Get Record ID: "+record.getId());
			//Log.d(LOGTAG, "Get Record date"+record.getDateTimeScreen());
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return records;
	}
	
	/**
	 * Fetch records from the DB that match the search criteria
	 * 
	 * @param category String, table column
	 * @param item String search item
	 * @return List<Record> Records matching search criteria 
	 */
	public List<Record> searchRecords(String category, String item) {
		
		Log.d("Navin-DAO", "searchRecords");
		
		List<Record> records = new ArrayList<Record>();
		
		//String where = category+" = '"+item+"'";
		String where = category+" LIKE '%"+item+"%'";
		String orderBy = DbHelper.COLUMN_RECORD_DATETIME + " ASC";
		
		Cursor cursor = mDatabase.query(DbHelper.TABLE_JOB_RECORD,
				ALLCOLUMNS, where, null, null, null, orderBy);
		
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			Record record = cursorToRecord(cursor);
			records.add(record);
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return records;
	}
	
	/**
	 * Delete job_record Table
	 */
	public void drop_table() {
		
		Log.d("Navin-DAO", "drop_table");
		
		mDatabase.execSQL("DROP TABLE IF EXISTS " + DbHelper.TABLE_JOB_RECORD);
		mDbHelper.onCreate(mDatabase);
	}
	
	/**
	 * Creates and populates a Record instance from a Cursor
	 * 
	 * @param cursor Cursor containing data fetched from database
	 * @return Record containing data fetched from database
	 */
	private Record cursorToRecord(Cursor cursor) {
		
		Log.d("Navin-DAO", "cursorToRecord Started");
		Record record = new Record();		
		
		record.setId(cursor.getLong(DbHelper.COLUMN_NUM_ID));							
		record.setComments(cursor.getString(DbHelper.COLUMN_NUM_RECORD_COMMENTS));				
		record.setJobCompany(cursor.getString(DbHelper.COLUMN_NUM_JOB_COMPANY));		
		record.setJobTitle(cursor.getString(DbHelper.COLUMN_NUM_JOB_TITLE));
		record.setRecordType(cursor.getString(DbHelper.COLUMN_NUM_RECORD_TYPE));
		record.setRecruiterCompany(cursor.getString(DbHelper.COLUMN_NUM_RECRUITER_COMPANY));
		record.setRecruiterName(cursor.getString(DbHelper.COLUMN_NUM_RECRUITER_NAME));
		record.setGoalTitle(cursor.getString(DbHelper.COLUMN_NUM_GOAL_TITLE));
		record.setGoalDesc(cursor.getString(DbHelper.COLUMN_NUM_GOAL_DESC));
		record.setGoalLocation(cursor.getString(DbHelper.COLUMN_NUM_GOAL_LOCATION));
		RecordHolder rh = new RecordHolder(record);
		rh.setDateTimeDb(cursor.getString(DbHelper.COLUMN_NUM_RECORD_DATETIME));
				
		return rh.getRecord();
	}
	
	/**
	 * Creates and populates a ContentValues instance from a Record
	 * 
	 * @param record Record containing user entered data
	 * @return ContentValues instance containing user entered data
	 */
	private ContentValues recordToContentValues(Record record) {
				
		Log.d("Navin-DAO", "recordToContentValues Started");
		
		ContentValues values = new ContentValues();
		// Load user-entered values into ContentValues instance
		RecordHolder rh = new RecordHolder(record);
		Random rand = new Random();
		int randId = rand.nextInt(100000);
		values.put(DbHelper.COLUMN_ID, randId);
		values.put(DbHelper.COLUMN_RECORD_TYPE, record.getRecordType());
		values.put(DbHelper.COLUMN_JOB_TITLE, record.getJobTitle());
		values.put(DbHelper.COLUMN_JOB_COMPANY, record.getJobCompany());
		values.put(DbHelper.COLUMN_RECRUITER_NAME, record.getRecruiterName());
		values.put(DbHelper.COLUMN_RECRUITER_COMPANY, record.getRecruiterCompany());
		values.put(DbHelper.COLUMN_RECORD_DATETIME, rh.getDateTimeDb());
		values.put(DbHelper.COLUMN_RECORD_COMMENTS, record.getComments());
		values.put(DbHelper.COLUMN_RECORD_JOB_ADDRESS, record.getJobAddress());
		values.put(DbHelper.COLUMN_RECORD_JOB_POSTCODE, record.getJobPostcode());
		values.put(DbHelper.COLUMN_GOAL_TITLE, record.getGoalTitle());
		values.put(DbHelper.COLUMN_GOAL_DESC, record.getGoalDesc());
		values.put(DbHelper.COLUMN_GOAL_LOC, record.getGoalLocation());			
		
		return values;
	}
	
	
	
	private ContentValues updateRecordToContentValues(Record record) {
		
		Log.d("Navin-DAO", "recordToContentValues Started");
		
		ContentValues values = new ContentValues();
		// Load user-entered values into ContentValues instance
		RecordHolder rh = new RecordHolder(record);
		values.put(DbHelper.COLUMN_ID, record.getId());
		values.put(DbHelper.COLUMN_RECORD_TYPE, record.getRecordType());
		values.put(DbHelper.COLUMN_JOB_TITLE, record.getJobTitle());
		values.put(DbHelper.COLUMN_JOB_COMPANY, record.getJobCompany());
		values.put(DbHelper.COLUMN_RECRUITER_NAME, record.getRecruiterName());
		values.put(DbHelper.COLUMN_RECRUITER_COMPANY, record.getRecruiterCompany());
		values.put(DbHelper.COLUMN_RECORD_DATETIME, rh.getDateTimeDb());
		values.put(DbHelper.COLUMN_RECORD_COMMENTS, record.getComments());
		values.put(DbHelper.COLUMN_RECORD_JOB_ADDRESS, record.getJobAddress());
		values.put(DbHelper.COLUMN_RECORD_JOB_POSTCODE, record.getJobPostcode());
		values.put(DbHelper.COLUMN_GOAL_TITLE, record.getGoalTitle());
		values.put(DbHelper.COLUMN_GOAL_DESC, record.getGoalDesc());
		values.put(DbHelper.COLUMN_GOAL_LOC, record.getGoalLocation());			
		
		return values;
	}
}
