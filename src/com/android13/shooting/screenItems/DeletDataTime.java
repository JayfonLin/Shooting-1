package com.android13.shooting.screenItems;

import java.util.Calendar;

import android.text.format.Time;
import android.util.Log;

public class DeletDataTime {
	private boolean ifDelete ;
	private int lastTime ;
	private int currentTime ;
	private int wDay ;
	private Calendar time ;
	private Time dayTime ;
	private int hour ;
	private int minute ;
	private int second ;
	private int pYear, lYear ;
	private int pMonth, lMonth ;
	private int pDay, lDay ;
	public DeletDataTime(){
		ifDelete = false ;
		wDay = 1 ;
		hour = 0 ;
		minute = 0 ;
		second = 0 ;
	}
	public boolean ifDeleteData(){
		time = Calendar.getInstance() ;
		wDay = time.get(Calendar.DAY_OF_WEEK) ;
		lYear = time.get(Calendar.YEAR) ;
		lMonth = time.get(Calendar.MONTH) ;
		lDay = time.get(Calendar.DAY_OF_MONTH) ;
		pDay = time.get(Calendar.DAY_OF_YEAR) ;
		Log.v("year,month,day,yday",lYear+","+lMonth+","+lDay+","+pDay);
		if( wDay==1 ){
			ifDelete = true ;
		}
		else{
			ifDelete = false ;
		}
		Log.v("weekDay",String.valueOf(wDay)) ;
		return ifDelete ;
	}
}
