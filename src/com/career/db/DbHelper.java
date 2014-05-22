package com.career.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Helper class to manage data creation
 * 
 *
 */
public class DbHelper extends SQLiteOpenHelper {
	
	// Table and Column names
	public static final String TABLE_JOB = "job";
	public static final String TABLE_RECORD = "record";
	public static final String TABLE_RECRUITER = "recruiter";
	public static final String TABLE_JOB_RECORD = "job_record";
	public static final String TABLE_GOAL = "goal";
	
	//column ids
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_RECORD_GOAL_ID = "goal_id";
	public static final String COLUMN_RECORD_JOB_ID = "job_id";
	public static final String COLUMN_RECORD_RECRUITER_ID = "recruiter_id";
	
	//goal
	public static final String COLUMN_GOAL_TITLE = "goal_title";
	public static final String COLUMN_GOAL_DESC = "goal_desc";
	public static final String COLUMN_GOAL_LOC = "goal_loc";
	
	//contact
	public static final String COLUMN_RECORD_TYPE = "type";
	
	//interview
	public static final String COLUMN_RECORD_JOB_ADDRESS = "job_address";
	public static final String COLUMN_RECORD_JOB_POSTCODE = "job_postcode";
	
	//Common
	public static final String COLUMN_RECORD_DATETIME = "datetime";
	public static final String COLUMN_RECRUITER_NAME = "name";
	public static final String COLUMN_RECRUITER_COMPANY = "recruiter_company";
	public static final String COLUMN_JOB_TITLE = "title";
	public static final String COLUMN_JOB_COMPANY = "job_company";
	public static final String COLUMN_RECORD_COMMENTS = "comments";
	
	
	
	
	private static final String DATABASE_NAME = "jobhunt.db";
	private static final int DATABASE_VERSION = 6;
	private static final String LOGTAG = DbHelper.class.getName();

	/*// TODO Use when splitting table into Record, Goal, Job, Recruiter
		private static final String GOAL_DATABASE_CREATE = "create table " + TABLE_GOAL + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_GOAL_TITLE + " text not null collate nocase, "
			+ COLUMN_GOAL_DESC+" text not null, "
			+ COLUMN_GOAL_LOC + " text not null);";
	
	// TODO Use when splitting table into Record, Goal, Job, Recruiter
	private static final String JOB_DATABASE_CREATE = "create table " + TABLE_JOB + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_JOB_TITLE + " text not null collate nocase, "
			+ COLUMN_JOB_COMPANY+" text not null);";
	
	// TODO Use when splitting table into Record, Goal, Job, Recruiter
	private static final String RECRUITER_DATABASE_CREATE = "create table " + TABLE_RECRUITER + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_RECRUITER_NAME + " text not null, "
			+ COLUMN_RECRUITER_COMPANY+" text not null);";
	
	// TODO Use when splitting table into Record, Goal, Job, Recruiter
	private static final String RECORD_DATABASE_CREATE = "create table " + TABLE_RECORD + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_RECORD_TYPE + " text not null, "			
			+ COLUMN_RECORD_JOB_ID+" integer, " + " foreign key ("+COLUMN_RECORD_JOB_ID+") references " + TABLE_JOB 
			+ "("+ COLUMN_ID + "), "
			+ COLUMN_RECORD_RECRUITER_ID+ "integer, " + " foreign key ("+COLUMN_RECORD_RECRUITER_ID+") references "+TABLE_RECRUITER
			+ "("+ COLUMN_ID + "), "
			+ COLUMN_RECORD_COMMENTS+" text"
			+ COLUMN_RECORD_GOAL_ID+" integer, " + " foreign key ("+COLUMN_RECORD_GOAL_ID+") references " + TABLE_GOAL 
			+ "("+ COLUMN_ID + "), "
			+");";*/
	
	// Column numbers
		public static final int COLUMN_NUM_ID = 0;
		public static final int COLUMN_NUM_RECORD_TYPE = 1;
		public static final int COLUMN_NUM_RECORD_DATETIME = 2;
		public static final int COLUMN_NUM_JOB_TITLE = 3;
		public static final int COLUMN_NUM_JOB_COMPANY = 4;
		public static final int COLUMN_NUM_RECRUITER_NAME = 5;
		public static final int COLUMN_NUM_RECRUITER_COMPANY = 6;	
		public static final int COLUMN_NUM_RECORD_COMMENTS = 7;
		public static final int COLUMN_NUM_JOB_ADDRESS = 8;
		public static final int COLUMN_NUM_JOB_POSTCODE = 9;
		public static final int COLUMN_NUM_GOAL_LOCATION = 10;
		public static final int COLUMN_NUM_GOAL_TITLE = 11;
		public static final int COLUMN_NUM_GOAL_DESC = 12;
	
	/**
	 * SQL to create job_record table
	 */
	private static final String JOB_RECORD_DATABASE_CREATE = "create table " + TABLE_JOB_RECORD + "(" 
			+ COLUMN_ID + " integer primary key, " 
			+ COLUMN_RECORD_TYPE + " text, "
			+ COLUMN_RECORD_DATETIME+" datetime not null, "
			+ COLUMN_JOB_TITLE + " text collate nocase, "
			+ COLUMN_JOB_COMPANY+" text collate nocase, "
			+ COLUMN_RECRUITER_NAME + " text collate nocase, "
			+ COLUMN_RECRUITER_COMPANY + " text collate nocase, " 
			+ COLUMN_RECORD_COMMENTS+" text, "
			+ COLUMN_RECORD_JOB_ADDRESS+" text, "
			+ COLUMN_RECORD_JOB_POSTCODE+" text, "
			+ COLUMN_GOAL_LOC + " text, "
			+ COLUMN_GOAL_TITLE + " text, "
			+ COLUMN_GOAL_DESC + " text "			
			+ ");";
	
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		Log.d("Navin-DbHelper", "constructor");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		Log.d("Navin-DbHelper", "onCreate");
		
		// Create DB table
		db.execSQL(JOB_RECORD_DATABASE_CREATE);
		Log.d("Navin-DbHelper", "onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		Log.d("Navin-DbHelper", "onUpgrade");
		
		Log.w(LOGTAG,
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		// Drop table
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB_RECORD);
		// Create Table again
		onCreate(db);
	}
	
}
