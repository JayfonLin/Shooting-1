package com.android13.shooting;

import java.io.IOException;
import java.security.PublicKey;

import com.android13.shooting.screenItems.Score;
import com.android13.shooting.screenItems.Timer;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 游戏的入口Activity
 * 
 * @author Tiga <liangkangabc@gmail.com>
 * 
 */
public class MainActivity extends Activity {

	public static final int MESSAGE_READYGO = 0;
	public static final int MESSAGE_NEXTLEVEL = 1;
	public static final int MESSAGE_FINISH = 2;

	private MediaPlayer mediaPlayer;
	SurfaceView mainSurfaceView;
	PopupWindow mPopWin;
	ImageView resumeIV, replayIV;
	Bundle initial_state;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initial_state = savedInstanceState;
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MESSAGE_NEXTLEVEL:
					handler.postDelayed(new ReadyGo(), 100);
					break;
				case MESSAGE_FINISH:
					Intent intent = new Intent(MainActivity.this,
							StoreScoreActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("score",
							Integer.toString(Score.getInstance().getScore()));
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

		PlaySound.init(this, 5, AudioManager.STREAM_MUSIC, 0);
		mediaPlayer = MediaPlayer.create(getApplicationContext(),
				R.raw.music_game);
		mediaPlayer.setLooping(true);
		startMusic();

		Intent intent = getIntent();
		int level = intent.getIntExtra("level", 1);
		// 2014年1月21日14:26:46 以第几关初始化游戏

		Game.init(this, level);
		Game.Constant.GAME_REMAIN_TIME = 10;
		mainSurfaceView = new MainSurfaceView(this, handler);
		setContentView(mainSurfaceView);
	}

	private void startMusic() {
		if (Game.Constant.GAME_MUSIC_ON) {
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
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onPause();

			LinearLayout popWin_layout = (LinearLayout) MainActivity.this
					.getLayoutInflater().inflate(R.layout.pause_popwin_layout,
							null);
			mPopWin = new PopupWindow(popWin_layout, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			// mPopWin.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.background));
			mPopWin.setOutsideTouchable(true);

			mPopWin.setFocusable(true);
			mPopWin.showAtLocation(mainSurfaceView, Gravity.CENTER, 0, 0);

			mPopWin.update();
			resumeIV = (ImageView) popWin_layout.findViewById(R.id.resumeIV);
			resumeIV.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					closePopWin();
					onResume();
				}
			});
			replayIV = (ImageView) popWin_layout.findViewById(R.id.replayIV);
			replayIV.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					closePopWin();
					finish();
				}
			});
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		Game.release(true);

		if (Game.Constant.GAME_MUSIC_ON) {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}

		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Game.Constant.GAME_PAUSE = true;
		if (Game.Constant.GAME_MUSIC_ON) {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		Game.Constant.GAME_PAUSE = false;
		if (Game.Constant.GAME_MUSIC_ON) {
			if (!mediaPlayer.isPlaying()) {
				startMusic();
			}
		}
		super.onResume();
	}

	private void closePopWin() {
		if (mPopWin != null && mPopWin.isShowing()) {
			mPopWin.dismiss();
		}
	}

	class ReadyGo implements Runnable {

		private PopupWindow popupWin;
		private int index;
		private ImageView cd;

		@Override
		public void run() {

			onPause();
			LinearLayout popWin_layout = (LinearLayout) getLayoutInflater()
					.inflate(R.layout.between_level, null);
			popupWin = new PopupWindow(popWin_layout,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			popupWin.setOutsideTouchable(true);

			popupWin.setFocusable(true);

			popupWin.showAtLocation(mainSurfaceView, Gravity.CENTER, 0, 0);
			popupWin.update();

			index = 1;
			AnimationListener animationListener = new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					RotateAnimation rotateAnimation = new RotateAnimation(0,
							360, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					rotateAnimation.setDuration(1000);
					rotateAnimation.setAnimationListener(this);
					switch (index) {
					case 1:
						index++;
						cd.setImageDrawable(getResources().getDrawable(
								R.drawable.cd_2));
						cd.startAnimation(rotateAnimation);
						break;
					case 2:
						index++;
						cd.setImageDrawable(getResources().getDrawable(
								R.drawable.cd_1));
						cd.startAnimation(rotateAnimation);
						break;
					case 3:
						index++;
						cd.setImageDrawable(getResources().getDrawable(
								R.drawable.cd_go));
						cd.startAnimation(rotateAnimation);
						break;
					case 4:
						popupWin.dismiss();
						Timer.getInstance().setRemainingTime(
								Game.Constant.GAME_REMAIN_TIME);
						onResume();
						break;
					}
				}
			};

			cd = (ImageView) popWin_layout.findViewById(R.id.imageView1);
			RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setDuration(1000);
			rotateAnimation.setAnimationListener(animationListener);
			cd.startAnimation(rotateAnimation);
		}

	}

}
