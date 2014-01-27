package com.android13.shooting.sql;

public class Data {
	private long id;
	private String name;
	private int score;
	private long time;

	public Data(long id, String name, int score, long time) {
		this.id = id;
		this.name = name;
		this.score = score;
		this.time = time;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
