package com.android13.shooting;

import com.android13.shooting.screenItems.CurrentTime;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StoreScoreActivity extends Activity{
	private TextView textView ;
	private EditText editText ;
	private Button button ;
	private String name ;
	private String score ;
	private CurrentTime time ;
	private int year ;
	private int month ;
	private int day ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort_score) ;
		
		name = new String();
		score = new String() ;
		time = new CurrentTime() ;
		
		textView = (TextView) findViewById(R.id.textView3) ;
		editText = (EditText) findViewById(R.id.editText1) ;
		button = (Button) findViewById(R.id.button1) ;
		Intent intent = this.getIntent() ;
		Bundle bundle = new Bundle() ;
		bundle = intent.getExtras() ;
		score = bundle.getString("score") ;
		textView.setText(score) ;
		year = time.getYear() ;
		month = time.getMonth() ;
		day = time.getDay() ;
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				name = editText.getText().toString() ;
				if(name.equals("")){
					name = "Admin" ;
				}
					Intent intent = new Intent(StoreScoreActivity.this,MainActivity.class) ;
					Bundle bundle = new Bundle() ;
					Log.v("sqlaaaaa", name+" "+score) ;
					bundle.putString("name", name) ;
					bundle.putString("score", score) ;
					bundle.putInt("year", year) ;
					bundle.putInt("month", month) ;
					bundle.putInt("day", day) ;
					intent.putExtras(bundle) ;
					StoreScoreActivity.this.setResult(RESULT_FIRST_USER, intent) ;
					StoreScoreActivity.this.finish() ;
				
			}
		});
	}
}
