package com.android13.shooting.screenItems;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SqlChange {
	private SqlOpenHelper sqlOpenHelper ;
	public SqlChange(Context context) {
		// TODO Auto-generated constructor stub
		SQLiteDatabase sqLiteDatabase = null;
		sqlOpenHelper = new SqlOpenHelper(context, "Datas") ;
		ContentValues values = new ContentValues();
		values.put("name", "abc");
		values.put("score", "26");
		values.put("year", 2012) ;
		values.put("month", 8) ;
		values.put("day", 6) ;
		try {
			sqLiteDatabase = sqlOpenHelper.getWritableDatabase();
			sqLiteDatabase.insert("Datas", null, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}
	}
}
