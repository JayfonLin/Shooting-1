package com.android13.shooting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.android13.shooting.sql.Data;
import com.android13.shooting.sql.SqlOpenHelper;

/**
 * 游戏的选项界面，包括选择游戏模式，显示排行榜以及游戏音乐音效设置
 * 
 * @author 11331197 林家访 <98905067@qq.com>
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * @author 11331185 连凌淦 <839021322@qq.com>
 * 
 */

public class LevelSelector extends Activity {
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private PopupWindow audioPopupWin, exitPopupWin;
	private LinearLayout ll;
	private Button settingsIV;
	private Button exitButton, highScoreButton;
	private ImageView game_music_IV, sound_effect_IV, sureIV, cancelIV;
	private Boolean game_music_temp, game_sound_temp;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll = (LinearLayout) this.getLayoutInflater().inflate(R.layout.main_activity, null);
		setContentView(ll);
		sharedPreferences = getSharedPreferences("audiosettings", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		Game.Constant.GAME_MUSIC_ON = sharedPreferences.getBoolean("game_music_on", true);
		Game.Constant.SOUND_EFFECT_ON = sharedPreferences.getBoolean("sound_effect_on", true);

		Button singleButton = (Button) this.findViewById(R.id.start_single);
		Button trainButton = (Button) this.findViewById(R.id.start_train);
		settingsIV = (Button) this.findViewById(R.id.setting_btn);
		exitButton = (Button) this.findViewById(R.id.exit_btn);
		highScoreButton = (Button) this.findViewById(R.id.high_score_btn);

		highScoreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.post(new ShowHighScore());
			}
		});

		singleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LevelSelector.this, MainActivity.class);
				Game.Constant.IS_TRAIN = false;
				intent.putExtra("level", 1);
				startActivity(intent);
			}
		});
		trainButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(LevelSelector.this);
				builder.setTitle("请选择要关卡");
				builder.setItems(new String[] { "Level 1", "Level 2", "Level 3", "Level 4" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								int level = 1;
								if (which <= 3) {
									level = which + 1;
								}
								dialog.dismiss();
								Intent intent = new Intent(LevelSelector.this, MainActivity.class);
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
		/** 弹出设置音量音效对话框 */
		settingsIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				game_music_temp = Game.Constant.GAME_MUSIC_ON;
				game_sound_temp = Game.Constant.SOUND_EFFECT_ON;
				LinearLayout popWin_layout = (LinearLayout) LevelSelector.this.getLayoutInflater()
						.inflate(R.layout.audio_settings_layout, null);
				audioPopupWin = new PopupWindow(popWin_layout, LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				audioPopupWin.setOutsideTouchable(true);
				audioPopupWin.setFocusable(true);
				audioPopupWin.showAtLocation(ll, Gravity.CENTER, 0, 0);
				audioPopupWin.update();

				sureIV = (ImageView) popWin_layout.findViewById(R.id.image_sure);
				cancelIV = (ImageView) popWin_layout.findViewById(R.id.image_cancel);
				game_music_IV = (ImageView) popWin_layout.findViewById(R.id.image_game_music);
				sound_effect_IV = (ImageView) popWin_layout.findViewById(R.id.image_sound_effect);
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

		/** 弹出是否退出确认对话框 */
		exitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout popWin_layout = (LinearLayout) LevelSelector.this.getLayoutInflater()
						.inflate(R.layout.exit_layout, null);
				exitPopupWin = new PopupWindow(popWin_layout, LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				exitPopupWin.setOutsideTouchable(true);
				exitPopupWin.setFocusable(true);
				exitPopupWin.showAtLocation(ll, Gravity.CENTER, 0, 0);
				exitPopupWin.update();

				Button okButton = (Button) popWin_layout.findViewById(R.id.ok_btn);
				Button cancelButton = (Button) popWin_layout.findViewById(R.id.cancel_btn);

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

	/** 关闭音量音效设置对话框 */
	private void closeAudioPopWin() {
		if (audioPopupWin != null && audioPopupWin.isShowing()) {
			audioPopupWin.dismiss();
		}
	}

	/** 关闭退出确认对话框 */
	private void closeExitPopWin() {
		if (exitPopupWin != null && exitPopupWin.isShowing()) {
			exitPopupWin.dismiss();
		}
	}

	/** 设置音量音效的时候，点击按钮根据效果更换图标 */
	private void changeDrawable() {
		if (game_music_temp) {
			game_music_IV.setImageDrawable(getResources().getDrawable(R.drawable.volume_on));
		} else {
			game_music_IV.setImageDrawable(getResources().getDrawable(R.drawable.volume_off));
		}
		if (game_sound_temp) {
			sound_effect_IV.setImageDrawable(getResources().getDrawable(R.drawable.volume_on));
		} else {
			sound_effect_IV.setImageDrawable(getResources().getDrawable(R.drawable.volume_off));
		}
	}

	/** 弹出显示高分榜的对话框 */
	class ShowHighScore implements Runnable {
		private PopupWindow highScorePopupWin;
		private ListView scoreListView;
		private List<HashMap<String, String>> showHighScoreData;
		private SimpleAdapter scoreAdapter;
		private Button weekButton;
		private Button historyButton;
		private Button backButton;
		private SqlOpenHelper sqlOpenHelper;

		@Override
		public void run() {
			sqlOpenHelper = new SqlOpenHelper(LevelSelector.this);
			LinearLayout popWin_layout = (LinearLayout) getLayoutInflater().inflate(
					R.layout.show_score, null);
			highScorePopupWin = new PopupWindow(popWin_layout, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			showHighScoreData = new ArrayList<HashMap<String, String>>();
			getData(1);

			highScorePopupWin.setOutsideTouchable(true);
			highScorePopupWin.setFocusable(true);
			highScorePopupWin.showAtLocation(ll, Gravity.CENTER, 0, 0);
			highScorePopupWin.update();

			weekButton = (Button) popWin_layout.findViewById(R.id.week_btn);
			historyButton = (Button) popWin_layout.findViewById(R.id.history_btn);
			backButton = (Button) popWin_layout.findViewById(R.id.back);
			scoreListView = (ListView) popWin_layout.findViewById(R.id.score_list);

			historyButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getData(1);
					scoreAdapter.notifyDataSetChanged();
				}
			});
			weekButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getData(0);
					scoreAdapter.notifyDataSetChanged();
				}
			});
			backButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					highScorePopupWin.dismiss();
				}
			});

			scoreListView.setCacheColorHint(0);
			scoreAdapter = new SimpleAdapter(LevelSelector.this, showHighScoreData,
					R.layout.score_item, new String[] { "rank", "name", "score" }, new int[] {
							R.id.rank, R.id.name, R.id.score });
			scoreListView.setAdapter(scoreAdapter);
		}

		private void getData(int field) {
			showHighScoreData.clear();

			List<Data> datas = new ArrayList<Data>();
			if (field == 0) {
				datas = sqlOpenHelper.getWeekData();
			} else {
				datas = sqlOpenHelper.getAllData();
			}

			for (int i = 0; i < datas.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("rank", Integer.toString(i + 1));
				map.put("name", datas.get(i).getName());
				map.put("score", Integer.toString(datas.get(i).getScore()));
				showHighScoreData.add(map);
			}
		}
	}
}
