package com.android13.shooting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

// TODO 添加排行榜的Activity，按钮ID 为high_score_btn

public class LevelSelector extends Activity {
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	PopupWindow audioPopupWin, exitPopupWin;
	LinearLayout ll;
	Button settingsIV;
	Button exitButton, highScoreButton;
	ImageView game_music_IV, sound_effect_IV, sureIV, cancelIV;
	Boolean game_music_temp, game_sound_temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll = (LinearLayout) this.getLayoutInflater().inflate(
				R.layout.main_activity, null);
		setContentView(ll);
		sharedPreferences = getSharedPreferences("audiosettings", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		Game.Constant.GAME_MUSIC_ON = sharedPreferences.getBoolean(
				"game_music_on", true);
		Game.Constant.SOUND_EFFECT_ON = sharedPreferences.getBoolean(
				"sound_effect_on", true);

		Button singleButton = (Button) this.findViewById(R.id.start_single);
		Button trainButton = (Button) this.findViewById(R.id.start_train);
		settingsIV = (Button) this.findViewById(R.id.setting_btn);
		exitButton = (Button) this.findViewById(R.id.exit_btn);
		highScoreButton = (Button) this.findViewById(R.id.high_score_btn);
		
		highScoreButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//  跳转。。。
			}
		});
		
		singleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelSelector.this,
						MainActivity.class);
				Game.Constant.IS_TRAIN = false;
				intent.putExtra("level", 1);
				startActivity(intent);
			}
		});
		trainButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						LevelSelector.this);

				builder.setTitle("请选择要关卡");
				builder.setItems(new String[] { "Level 1", "Level 2",
						"Level 3", "Level 4" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								int level = 1;
								if (which <= 3) {
									level = which + 1;
								}
								dialog.dismiss();

								Intent intent = new Intent(LevelSelector.this,
										MainActivity.class);
								Game.Constant.IS_TRAIN = true;
								intent.putExtra("level", level);
								startActivity(intent);
							}
						});
				builder.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();

			}
		});

		settingsIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				game_music_temp = Game.Constant.GAME_MUSIC_ON;
				game_sound_temp = Game.Constant.SOUND_EFFECT_ON;
				LinearLayout popWin_layout = (LinearLayout) LevelSelector.this
						.getLayoutInflater().inflate(
								R.layout.audio_settings_layout, null);
				audioPopupWin = new PopupWindow(popWin_layout,
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

				// mPopWin.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.background));
				audioPopupWin.setOutsideTouchable(true);

				audioPopupWin.setFocusable(true);
				audioPopupWin.showAtLocation(ll, Gravity.CENTER, 0, 0);

				audioPopupWin.update();

				sureIV = (ImageView) popWin_layout
						.findViewById(R.id.image_sure);
				cancelIV = (ImageView) popWin_layout
						.findViewById(R.id.image_cancel);
				game_music_IV = (ImageView) popWin_layout
						.findViewById(R.id.image_game_music);
				sound_effect_IV = (ImageView) popWin_layout
						.findViewById(R.id.image_sound_effect);
				changeDrawable();
				sureIV.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						editor.putBoolean("game_music_on", game_music_temp);
						editor.putBoolean("sound_effect_on", game_sound_temp);
						editor.commit();
						Game.Constant.GAME_MUSIC_ON = game_music_temp;
						Game.Constant.SOUND_EFFECT_ON = game_sound_temp;
						closeAudioPopWin();
					}
				});
				cancelIV.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						closeAudioPopWin();
					}
				});
				game_music_IV.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (game_music_temp) {
							game_music_temp = false;
						} else {
							game_music_temp = true;
						}
						changeDrawable();
					}
				});
				sound_effect_IV.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (game_sound_temp) {
							game_sound_temp = false;
						} else {
							game_sound_temp = true;
						}
						changeDrawable();
					}
				});
			}
		});

		exitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LinearLayout popWin_layout = (LinearLayout) LevelSelector.this
						.getLayoutInflater()
						.inflate(R.layout.exit_layout, null);
				exitPopupWin = new PopupWindow(popWin_layout,
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				exitPopupWin.setOutsideTouchable(true);

				exitPopupWin.setFocusable(true);
				exitPopupWin.showAtLocation(ll, Gravity.CENTER, 0, 0);

				exitPopupWin.update();

				Button okButton = (Button) popWin_layout
						.findViewById(R.id.ok_btn);
				Button cancelButton = (Button) popWin_layout
						.findViewById(R.id.cancel_btn);

				okButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
				cancelButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						closeExitPopWin();
					}
				});
			}
		});
	}

	private void closeAudioPopWin() {
		if (audioPopupWin != null && audioPopupWin.isShowing()) {
			audioPopupWin.dismiss();
		}
	}

	private void closeExitPopWin() {
		if (exitPopupWin != null && exitPopupWin.isShowing()) {
			exitPopupWin.dismiss();
		}
	}

	private void changeDrawable() {
		if (game_music_temp)
			game_music_IV.setImageDrawable(getResources().getDrawable(
					R.drawable.volume_on));
		else
			game_music_IV.setImageDrawable(getResources().getDrawable(
					R.drawable.volume_off));
		if (game_sound_temp)
			sound_effect_IV.setImageDrawable(getResources().getDrawable(
					R.drawable.volume_on));
		else
			sound_effect_IV.setImageDrawable(getResources().getDrawable(
					R.drawable.volume_off));
	}
}
