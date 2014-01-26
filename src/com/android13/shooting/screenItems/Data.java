package com.android13.shooting.screenItems;

public class Data {
	private int id;
	private String name;
	private String score;
	private int year ;
	private int month ;
	private int day ;

	public Data(int i, String n, String s, int year, int month, int day) {
		id = i;
		name = n;
		score = s;
		this.year = year ;
		this.month = month ;
		this.day = day ;
	}
	public int getId() {
		return id;
	}
	public void setId(int i) {
		id = i;
	}
	public String getName() {
		return name;
	}
	public void setName(String n) {
		name = n;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String s) {
		score = s;
	}
	public int getYear(){
		return year ;
	}
	public void setYear( int y ){
		year = y ;
	}
	public int getMonth(){
		return month ;
	}
	public int getDay(){
		return day ;
	}
}
