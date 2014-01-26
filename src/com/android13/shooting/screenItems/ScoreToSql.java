package com.android13.shooting.screenItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ScoreToSql {
	private static String sqlName = "Datas";
	private SqlOpenHelper sqlOpenHelper ;
	private SQLiteDatabase sqLiteDatabase ;
	public ScoreToSql(Context context) {
		// TODO Auto-generated constructor stub
		sqlOpenHelper = new SqlOpenHelper(context, sqlName) ;
	}
	
	public boolean addScore(Data data,String table) {
		sqLiteDatabase = null;
		ContentValues values = new ContentValues();
		values.put("name", data.getName());
		values.put("score", data.getScore());
		values.put("year", data.getYear()) ;
		values.put("month", data.getMonth()) ;
		values.put("day", data.getDay()) ;
		try {
			sqLiteDatabase = sqlOpenHelper.getWritableDatabase();
			long rid = sqLiteDatabase.insert(table, null, values);
			return (rid != 0 );
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}
		return false;
	}
	
	public void deleteDataByScore(String score, String tableString) {
		sqLiteDatabase = null;
		String sql = "delete * from " + tableString
				+ " where score = " + score;
		try {
			sqLiteDatabase = sqlOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}
	}
	
	public void deleteTableData(String tableString){
		sqLiteDatabase = sqlOpenHelper.getWritableDatabase() ;
		sqLiteDatabase.delete(tableString, null, null) ;
		if(sqLiteDatabase!=null){
			sqLiteDatabase.close() ;
		}
	}
	public List<Map<String, Object>> getAllData() {
		sqLiteDatabase = null;
		List<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();
		try {
			sqLiteDatabase = sqlOpenHelper.getReadableDatabase();
			Cursor cursor = sqLiteDatabase.query(sqlName, null, null, null, null, null, null);
			int count = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i=0; i<count; i++) {
					String columnName = cursor.getColumnName(i);
					String value = cursor.getString(i);
					if (value == null) {
						value = "";
					}
					map.put(columnName, value);
				}
				contacts.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}
		return contacts;
	}
	
}
