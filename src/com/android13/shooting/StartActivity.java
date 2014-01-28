package com.android13.shooting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 开始应用程序前延迟1s的启动界面
 * 
 * @author 11331197 林家访 <98905067@qq.com>
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * @author 11331185 连凌淦 <839021322@qq.com>
 * 
 */
public class StartActivity extends Activity {
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_layout);
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(StartActivity.this, LevelSelector.class);
				startActivity(intent);
				finish();
			}
		}, 1000);
	}

}
