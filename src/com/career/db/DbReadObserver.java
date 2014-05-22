package com.career.db;

import java.util.List;

/**
 * This interface uses the Observer design pattern to
 * enable the decoupling of DB access from the Calendar
 * Activity.
 * 
 *
 */
public interface DbReadObserver {
	/**
	 * Allows records obtained from Db to be accessed within Activity
	 * 
	 * @param records List<Record> of records obtained from database
	 */
	public void receiveRecordList(List<Record> records);
}
