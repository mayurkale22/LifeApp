package com.career;

public interface TimeDateObserver {
	void processTime(int hour, int minute);
	void processDate(int year, int month, int day);
}
