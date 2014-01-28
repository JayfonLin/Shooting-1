package com.android13.shooting;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android13.shooting.screenItems.Score;
import com.android13.shooting.sql.Data;
import com.android13.shooting.sql.SqlOpenHelper;

/**
 * 游戏的入口Activity
 * 
 * @author Tiga <liangkangabc@gmail.com>
 * 
 */
public class MainActivity extends Activity {

	public static final int MESSAGE_NEXTLEVEL = 1;
	public static final int MESSAGE_FINISH = 2;
	public static final int MESSAGE_RESTART = 3;

	private boolean pause_temp;
	private MediaPlayer mediaPlayer;
	MainSurfaceView mainSurfaceView;
	PopupWindow mPopWin;
	ImageView resumeIV, replayIV;
	Bundle initial_state;
	ReadyGo readyGo;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			System.out.println(Game.Constant.GAME_PAUSE);
			switch (msg.what) {
			case MESSAGE_NEXTLEVEL:
			case MESSAGE_RESTART:
				mainSurfaceView.forceRefreshScreen();
				if (!Game.Constant.GAME_PAUSE) {
					if (readyGo == null) {
						readyGo = new ReadyGo(msg.what);
					}
					handler.post(readyGo);
				}
				break;
			case MESSAGE_FINISH:
				handler.post(new ShowScore());
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initial_state = savedInstanceState;

		PlaySound.init(this, 5, AudioManager.STREAM_MUSIC, 0);
		mediaPlayer = MediaPlayer.create(getApplicationContext(),
				R.raw.music_game);
		mediaPlayer.setLooping(true);
		startMusic();

		Intent intent = getIntent();
		int level = intent.getIntExtra("level", 1);
		// 2014年1月21日14:26:46 以第几关初始化游戏

		Game.Constant.GAME_REMAIN_TIME = 60;
		Game.init(this, level);

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
			Game.Constant.GAME_PAUSE = true;
			if (Game.Constant.GAME_MUSIC_ON) {
				if (mediaPlayer != null) {
					mediaPlayer.stop();
				}
			}

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
					Message msg = new Message();
					msg.what = MainActivity.MESSAGE_RESTART;
					handler.sendMessage(msg);

					Game.Constant.GAME_PAUSE = false;
					if (Game.Constant.GAME_MUSIC_ON) {
						if (!mediaPlayer.isPlaying()) {
							startMusic();
						}
					}
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
		handler.removeCallbacks(readyGo);
		readyGo = null;
		if (Game.Constant.GAME_MUSIC_ON) {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}

		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		handler.removeCallbacks(readyGo);
		readyGo = null;
		pause_temp = Game.Constant.GAME_PAUSE;
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
		if (!pause_temp) {
			Game.Constant.GAME_PAUSE = false;
		}
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

	class ShowScore implements Runnable {
		PopupWindow scorePopupWin;
		TextView scoreTextView;
		Button menuButton;
		Button saveButton;
		ImageView week_new, history_new;
		EditText nameEditText = new EditText(MainActivity.this);
		SqlOpenHelper sqlOpenHelper = new SqlOpenHelper(MainActivity.this);
		int score;
		DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String name = nameEditText.getText().toString();
				if (name.equals("")) {
					name = "noname";
				}
				Data data = new Data(-1, name, score,
						System.currentTimeMillis());
				sqlOpenHelper.insert(data);
				saveButton.setVisibility(View.INVISIBLE);
				dialog.dismiss();
			}
		};
		DialogInterface.OnClickListener cancelClickListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		AlertDialog saveDialog = new AlertDialog.Builder(MainActivity.this)
				.setTitle("请输入名字：").setView(nameEditText)
				.setPositiveButton("确定", okClickListener)
				.setNegativeButton("取消", cancelClickListener).create();

		@Override
		public void run() {
			score = Score.getInstance().getScore();

			LinearLayout popWin_layout = (LinearLayout) getLayoutInflater()
					.inflate(R.layout.sort_score, null);
			scorePopupWin = new PopupWindow(popWin_layout,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			scorePopupWin.setOutsideTouchable(true);
			scorePopupWin.setFocusable(true);
			scorePopupWin.showAtLocation(mainSurfaceView, Gravity.CENTER, 0, 0);
			scorePopupWin.update();

			scoreTextView = (TextView) popWin_layout
					.findViewById(R.id.tv_score);
			menuButton = (Button) popWin_layout.findViewById(R.id.menu_btn);
			saveButton = (Button) popWin_layout.findViewById(R.id.save_btn);
			week_new = (ImageView) popWin_layout.findViewById(R.id.week_record);
			history_new = (ImageView) popWin_layout
					.findViewById(R.id.history_record);

			scoreTextView.setText(Integer.toString(score));
			saveButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					saveDialog.show();
				}
			});
			menuButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					scorePopupWin.dismiss();
					finish();
				}
			});
			int historyHighScore = sqlOpenHelper.getHistoryHighestScore();
			int weekHighScore = sqlOpenHelper.getWeekHightestScore();
			if (score > historyHighScore) {
				history_new.setVisibility(View.VISIBLE);
			}
			if (score > weekHighScore) {
				week_new.setVisibility(View.VISIBLE);
			}
		}
	}

	class ReadyGo implements Runnable {

		private PopupWindow popupWin;
		private int index;
		private ImageView cd;
		private int msg;

		public ReadyGo(int msg) {
			this.msg = msg;
		}

		@Override
		public void run() {

			Game.Constant.GAME_PAUSE = true;
			if (Game.Constant.GAME_MUSIC_ON) {
				if (mediaPlayer != null) {
					mediaPlayer.stop();
				}
			}

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
					ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,
							2.0f, 1.0f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					scaleAnimation.setDuration(1000);
					scaleAnimation.setAnimationListener(this);
					switch (index) {
					case 1:
						index++;
						cd.setImageDrawable(getResources().getDrawable(
								R.drawable.cd_2));
						cd.startAnimation(scaleAnimation);
						break;
					case 2:
						index++;
						cd.setImageDrawable(getResources().getDrawable(
								R.drawable.cd_1));
						cd.startAnimation(scaleAnimation);
						break;
					case 3:
						index++;
						cd.setImageDrawable(getResources().getDrawable(
								R.drawable.cd_go));
						cd.startAnimation(scaleAnimation);
						break;
					case 4:
						popupWin.dismiss();

						Game.Constant.GAME_PAUSE = false;
						if (Game.Constant.GAME_MUSIC_ON) {
							if (!mediaPlayer.isPlaying()) {
								startMusic();
							}
						}

						break;
					}
				}
			};

			cd = (ImageView) popWin_layout.findViewById(R.id.imageView1);
			ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 2.0f,
					1.0f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			scaleAnimation.setDuration(1000);
			scaleAnimation.setAnimationListener(animationListener);
			cd.startAnimation(scaleAnimation);
		}

	}

}
