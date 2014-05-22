package com.career.db;

/**
 * This interface uses the Observer design pattern to
 * enable the decoupling of DB access from the Calendar
 * Activity.
 * 
 *
 */
public interface DbWriteObserver {
	/**
	 * Allows result of Db write to be accessed within Activity
	 * 
	 * @param result boolean, true if DB write successful
	 */
	public void printDbWriteResult(boolean result);
}
