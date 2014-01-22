package com.android13.shooting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


// 2014年1月21日14:25:51  游戏的选关界面，先留着，简陋点
public class LevelSelector extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_selector);

		Button level_1 = (Button) this.findViewById(R.id.button1);
		Button level_2 = (Button) this.findViewById(R.id.button2);
		Button level_3 = (Button) this.findViewById(R.id.button3);
		Button level_4 = (Button) this.findViewById(R.id.button4);

		level_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelSelector.this, MainActivity.class);
				intent.putExtra("level", 1);
				startActivity(intent);
			}
		});
		level_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelSelector.this, MainActivity.class);
				intent.putExtra("level", 2);
				startActivity(intent);
			}
		});
		level_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelSelector.this, MainActivity.class);
				intent.putExtra("level", 3);
				startActivity(intent);
			}
		});
		level_4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelSelector.this, MainActivity.class);
				intent.putExtra("level", 4);
				startActivity(intent);
			}
		});
	}
}
