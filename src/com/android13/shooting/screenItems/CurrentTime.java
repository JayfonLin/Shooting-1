package com.android13.shooting.screenItems;

import java.util.Calendar;

import android.util.Log;

public class CurrentTime {
	private Calendar calendar ;
	private int year ;
	private int month ;
	private int day ;
	public CurrentTime(){
		calendar = Calendar.getInstance() ;
		year = calendar.get(Calendar.YEAR) ;
		month = calendar.get(Calendar.MONTH) ;
		day = calendar.get(Calendar.DAY_OF_MONTH) ;
		Log.v("year", String.valueOf(year)) ;
		Log.v("month", String.valueOf(month)) ;
		Log.v("day", String.valueOf(day)) ;
	}
	public int getYear(){
		return year ;
	}
	public int getMonth(){
		return month ;
	}
	public int getDay(){
		return day ;
	}
}
