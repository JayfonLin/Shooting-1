package com.android13.shooting;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 游戏的入口Activity
 * 
 * @author Tiga <liangkangabc@gmail.com>
 * 
 */
public class MainActivity extends Activity {

	private MediaPlayer mediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PlaySound.init(this, 5, AudioManager.STREAM_MUSIC, 0);
		mediaPlayer = MediaPlayer.create(getApplicationContext(),
				R.raw.music_game);
		mediaPlayer.setLooping(true);
		startMusic();

		Intent intent = getIntent();
		int level = intent.getIntExtra("level", 1);
		// 2014年1月21日14:26:46 以第几关初始化游戏
		Game.init(this, level);

		setContentView(new MainSurfaceView(this));

	}

	private void startMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}

		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.start();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(this, "pause", Toast.LENGTH_SHORT).show();
			Game.release();
			mediaPlayer.stop();
			finish();
		}
		return true;
	}
}
