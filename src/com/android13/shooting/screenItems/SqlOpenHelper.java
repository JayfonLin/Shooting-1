package com.android13.shooting.screenItems;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlOpenHelper extends SQLiteOpenHelper{
	private static String sqlName = "Datas";
	private static int version = 1 ;
	public SqlOpenHelper(Context context, String name) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table " + sqlName
				+ " (id integer primary key autoincrement,"
				+ " name varchar(30)," + " score varchar(8),"
				+ " year int," + " month int," + " day int)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
