package com.android13.shooting.screenItems;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.android13.shooting.Game;
import com.android13.shooting.R;
import com.android13.shooting.res.BitmapPool;

public class Score extends ScreenItem {
	private int score;
	private int hundredFlame;
	private int tenFlame;
	private int bitFlame;
	public float scale;
	private static Score instance;

	public static Score getInstance() {
		if (instance == null) {
			synchronized (Score.class) {
				if (instance == null)
					instance = new Score();
			}
		}
		return instance;
	}

	private Score() {
		score = 0;
		hundredFlame = 0;
		tenFlame = 0;
		bitFlame = 0;
		scale = 0.3f;
		bmps = new Bitmap[10];
		for (int i = 0; i < 10; ++i) {
			bmps[i] = BitmapPool.getBitmap(R.drawable.score0 + i);
		}
		bmpWidth = bmps[0].getWidth();
		bmpHeight = bmps[0].getHeight();

		this.x = Game.Constant.SCREEN_WIDTH - Game.Constant.SCORE_NUM_WIDTH / 3;
		this.y = Game.Constant.SCREEN_HEIGHT / 45f;
		this.z = Game.Constant.NEAREST;
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		canvas.drawBitmap(Bitmap.createBitmap(bmps[hundredFlame], 0, 0,
				bmps[hundredFlame].getWidth(), bmps[hundredFlame].getHeight(),
				matrix, true), x - bmps[0].getWidth() * scale * 2, y, paint);
		canvas.drawBitmap(Bitmap.createBitmap(bmps[tenFlame], 0, 0,
				bmps[tenFlame].getWidth(), bmps[tenFlame].getHeight(), matrix,
				true), x - bmps[0].getWidth() * scale, y, paint);
		canvas.drawBitmap(Bitmap.createBitmap(bmps[bitFlame], 0, 0,
				bmps[bitFlame].getWidth(), bmps[bitFlame].getHeight(), matrix,
				true), x, y, paint);
	}

	public void addScore(int addGrade) {
		score += addGrade;
		if (score > 999) {
			score = 999;
		}
		hundredFlame = score / 100;
		tenFlame = score / 10 % 10;
		bitFlame = score % 100 % 10;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int parament) {
		score = parament;
		addScore(0);
	}

	@Override
	public void release() {
		super.release();
	}
}
