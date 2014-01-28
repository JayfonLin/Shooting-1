package com.android13.shooting.sql;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库的增删改查接口
 * 
 * @author 11331197 林家访 <98905067@qq.com>
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * @author 11331185 连凌淦 <839021322@qq.com>
 * 
 */
public class SqlOpenHelper extends SQLiteOpenHelper {
	/** 数据库名字 */
	private static String DB_NAME = "Datas";
	/** 数据表名字 */
	private static String TABLE_NAME = "HighScore";
	/** 建表脚本 */
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

	/** 插入数据项 */
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

	/** 清空数据库表 */
	public void clearAll() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		this.onCreate(db);
		db.close();
	}

	/** 获取本周的分数数据 */
	public List<Data> getWeekData() {
		List<Data> weekData = new ArrayList<Data>();
		SQLiteDatabase db = getReadableDatabase();
		Calendar calendar = Calendar.getInstance();
		Calendar curCalendar = Calendar.getInstance();
		int curWeekOfYear = curCalendar.get(Calendar.WEEK_OF_YEAR);
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, "score desc");
		while (c.moveToNext()) {

			long time = c.getLong(c.getColumnIndex("time"));
			calendar.setTimeInMillis(time);
			int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
			if ((calendar.get(Calendar.YEAR) == curCalendar.get(Calendar.YEAR))
					&& curWeekOfYear == weekOfYear) {
				Data data = new Data(c.getLong(0), c.getString(1), c.getInt(2), c.getLong(3));
				weekData.add(data);
			}
		}
		c.close();
		db.close();
		return weekData;
	}

	/** 获取所有的分数数据 */
	public List<Data> getAllData() {
		List<Data> weekData = new ArrayList<Data>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, "score desc");
		while (c.moveToNext()) {
			Data data = new Data(c.getLong(0), c.getString(1), c.getInt(2), c.getLong(3));
			weekData.add(data);
		}
		c.close();
		db.close();
		return weekData;
	}

	/* 获取历史的最高分 */
	public int getHistoryHighestScore() {
		List<Data> datas = getAllData();
		if (datas.size() == 0) {
			return 0;
		} else {
			return datas.get(0).getScore();
		}
	}

	/** 获取本周最高分 */
	public int getWeekHightestScore() {
		List<Data> datas = getWeekData();
		if (datas.size() == 0) {
			return 0;
		} else {
			return datas.get(0).getScore();
		}
	}
}
