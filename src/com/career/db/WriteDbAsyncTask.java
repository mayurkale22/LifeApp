package com.career.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Task performs database write
 * 
 *
 */
public class WriteDbAsyncTask extends AsyncTask<Record,Void,Boolean> {
	
	private Context mContext;
	private DbWriteObserver mObserver;
	private int mUpdate;
	
	public WriteDbAsyncTask(Context context, DbWriteObserver observer, int update) {
		
		Log.d("Navin-WriteDbAsync", "constructor");
		
		mContext = context;
		mObserver = observer;
		mUpdate = update;
	}

	@Override
	protected Boolean doInBackground(Record... records) {
		
		Log.d("Navin-WriteDbAsync", "doInBackground");
		Log.d("Navin-WriteDbAsync-mUpdate", String.valueOf(mUpdate));
		
		boolean result = false;
		Record record = records[0];
		JobHuntDAO jobHuntDAO = new JobHuntDAO(mContext);
		
		jobHuntDAO.open();
		
		if(mUpdate==1) {
			result = jobHuntDAO.addRecord(record);
		}
		if(mUpdate==2) {
			result = jobHuntDAO.updateRecord(record);
		} else {
			result = jobHuntDAO.deleteRecord(record);
		}
		
		jobHuntDAO.close();
		return result;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		
		Log.d("Navin-WriteDbAsync", "onPostExec");
		
		mObserver.printDbWriteResult(result);
    }

}
