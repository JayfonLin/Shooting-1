package com.android13.shooting;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

/**
 * 游戏的入口Activity
 * 
 * @author Tiga <liangkangabc@gmail.com>
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int level = intent.getIntExtra("level", 1);
		// 2014年1月21日14:26:46 以第几关初始化游戏
		Game.init(this, level);

		setContentView(new MainSurfaceView(this));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(this, "pause", Toast.LENGTH_SHORT).show();
			Game.release();
			finish();
		}
		return true;
	};
}
