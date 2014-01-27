package com.android13.shooting.sql;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlOpenHelper extends SQLiteOpenHelper {
	private static String DB_NAME = "Datas";
	private static String TABLE_NAME = "HighScore";
	private String CREATE_TABLE = "create table " + TABLE_NAME
			+ " (id integer primary key autoincrement, name varchar(30),"
			+ " score integer, time integer)";
	private static int VERSION = 1;

	public SqlOpenHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	public SqlOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public void insert(Data data) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", data.getName());
		values.put("score", data.getScore());
		values.put("time", data.getTime());
		long id = db.insert(TABLE_NAME, null, values);
		data.setId(id);
		db.close();
	}

	public void clearAll() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		this.onCreate(db);
		db.close();
	}

	public List<Data> getWeekData() {
		List<Data> weekData = new ArrayList<Data>();
		SQLiteDatabase db = getReadableDatabase();
		Calendar calendar = Calendar.getInstance();
		Calendar curCalendar = Calendar.getInstance();
		int curWeekOfYear = curCalendar.get(Calendar.WEEK_OF_YEAR);
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null,
				"score desc");
		while (c.moveToNext()) {

			long time = c.getLong(c.getColumnIndex("time"));
			calendar.setTimeInMillis(time);
			int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
			if ((calendar.get(Calendar.YEAR) == curCalendar.get(Calendar.YEAR))
					&& curWeekOfYear == weekOfYear) {
				Data data = new Data(c.getLong(0), c.getString(1), c.getInt(2),
						c.getLong(3));
				weekData.add(data);
			}
		}
		c.close();
		db.close();
		return weekData;
	}

	public List<Data> getAllData() {
		List<Data> weekData = new ArrayList<Data>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null,
				"score desc");
		while (c.moveToNext()) {
			Data data = new Data(c.getLong(0), c.getString(1), c.getInt(2),
					c.getLong(3));
			weekData.add(data);
		}
		c.close();
		db.close();
		return weekData;
	}

	public int getHistoryHighestScore() {
		List<Data> datas = getAllData();
		if (datas.size() == 0) {
			return 0;
		} else {
			return datas.get(0).getScore();
		}
	}

	public int getWeekHightestScore() {
		List<Data> datas = getWeekData();
		if (datas.size() == 0) {
			return 0;
		} else {
			return datas.get(0).getScore();
		}
	}
}
