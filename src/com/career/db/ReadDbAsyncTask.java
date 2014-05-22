package com.career.db;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Task performs database fetch
 * 
 *
 */
public class ReadDbAsyncTask extends AsyncTask<String, Void, List<Record>>{
	
	private final static int TABLE_COLUMN = 0;
	private final static int SEARCH_ITEM = 1;
	private Context mContext;
	private DbReadObserver mObserver;
	private boolean mCalendarRead;
	
	public ReadDbAsyncTask(Context context, DbReadObserver observer, boolean calendarRead) {
		mContext = context;
		mObserver = observer;
		mCalendarRead = calendarRead;
	}

	@Override
	protected List<Record> doInBackground(String... args) {
		List<Record> records = null;
		JobHuntDAO jobHuntDAO = new JobHuntDAO(mContext);
		jobHuntDAO.open();
		
		//jobHuntDAO.drop_table();
				
		if(mCalendarRead) {
			Log.d("Navin-ReadDb", "1");
			records = jobHuntDAO.getRecords();
		} else {
			records = jobHuntDAO.searchRecords(args[TABLE_COLUMN], args[SEARCH_ITEM]);
		}
		
		jobHuntDAO.close();
		return records;
	}
	
	@Override
	protected void onPostExecute(List<Record> records) {
		mObserver.receiveRecordList(records);
    }	

}
