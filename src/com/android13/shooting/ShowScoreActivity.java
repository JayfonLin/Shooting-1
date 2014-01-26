package com.android13.shooting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android13.shooting.R.layout;
import com.android13.shooting.screenItems.CurrentTime;
import com.android13.shooting.screenItems.SqlChange;
import com.android13.shooting.screenItems.SqlOpenHelper;

import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ShowScoreActivity extends Activity{
	private List<Map<String, Object>> dataList ;
	private ListView listView ;
	private String sqlName = "Datas" ;
	private SQLiteDatabase sqLiteDatabase ;
	private SqlOpenHelper sqlOpenHelper ;
	private List<Map<String, Object>> contacts ;
	private CurrentTime currentTime ;
	private int year ;
	private int month ;
	private int day ;
	private String signal ;
	
	//private SqlChange sqlChange ;
	private Comparator<Map<String, Object>> comparator = new Comparator<Map<String,Object>>() {
		@Override
		public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
			// TODO Auto-generated method stub
			if(Integer.valueOf(lhs.get("score").toString())<Integer.valueOf(rhs.get("score").toString())){
				String temp = rhs.get("order").toString() ;
				rhs.put("order", lhs.get("order").toString()) ;
				lhs.put("order", temp) ;
				return 1 ;
			}
			else {
				return -1;
			}
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(layout.show_score) ;
		currentTime = new CurrentTime() ;
		year = currentTime.getYear() ;
		month = currentTime.getMonth() ;
		day = currentTime.getDay() ;
		signal = new String("history") ;
		//sqlChange = new SqlChange(this) ;
		sqlOpenHelper = new SqlOpenHelper(this, "Datas") ;
		sqLiteDatabase = sqlOpenHelper.getReadableDatabase() ;
		listView = (ListView) findViewById(R.id.listView1) ;
		int j = 1 ;
		
		Intent intent = this.getIntent() ;
		Bundle bundle = intent.getExtras() ;
		signal = bundle.getString("kind") ;
		
		contacts = new ArrayList<Map<String, Object>>();
		try {
			sqLiteDatabase = sqlOpenHelper.getReadableDatabase();
			
			Cursor cursor = sqLiteDatabase.query(sqlName, null, null, null, null, null, null);
			int count = cursor.getColumnCount();
			Boolean temp = true;
			while (cursor.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i=0; i<count; i++) {
					String columnName = cursor.getColumnName(i);
					String value = cursor.getString(i);
					Log.v(columnName, value) ;
					if (value == null) {
						value = "";
					}
					map.put(columnName, value);
				}
			
				if( signal.equals("history")){
					map.put("order", String.valueOf(j) ) ;
					j++ ;
					contacts.add(map);
				}
				else{
					temp = dateCompare(Integer.valueOf(map.get("year").toString()), Integer.valueOf(map.get("month").toString()), Integer.valueOf(map.get("day").toString())) ;
					if(temp){
						map.put("order", String.valueOf(j) ) ;
						j++ ;
						contacts.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}
		Collections.sort(contacts,comparator) ;
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,
				contacts,R.layout.score_item,new String[]{"name","score","order"},
				new int[]{R.id.textView1,R.id.textView2,R.id.textView3});
		listView.setAdapter(listItemAdapter) ;
	}
	public Boolean dateCompare(int dYear, int dMonth, int dDay){
		if(year == dYear){
			if(month == dMonth){
				if( (dDay-day)<7){
					return true ;
				}
				else {
					return false ;
				}
			}
			else if( (dMonth-month) == 1 && day<7){
				switch (dMonth) {
					case 0:
					case 2:
					case 4:
					case 6:
					case 7:
					case 9:
					case 11:
						if( dDay > (24+day) ){ //31-(7-day)
							return true ;
						}
						else {
							return false ;
						}
					case 1:
					case 3:
					case 5:
					case 8:
					case 10:
						if( dDay > (23+day) ){
							return true ;
						}
						else {
							return false ;
						}
				default:
					return false ;
				}
			}
			else{
				return false ;
			}
		}
		else {
			return false;
		}
	}
}
