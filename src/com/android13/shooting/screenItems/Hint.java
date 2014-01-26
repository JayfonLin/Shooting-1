package com.android13.shooting.screenItems;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.android13.shooting.Game;
import com.android13.shooting.R;
import com.android13.shooting.res.BitmapPool;

public class Hint extends ScreenItem {
	private float speedY;

	private static Hint instance;
	/**
	 * 投篮得分
	 */
	public boolean isGoal = false;
	public int score;

	public static Hint getInstance() {
		if (instance == null) {
			synchronized (Hint.class) {
				if (instance == null)
					instance = new Hint();
			}
		}
		return instance;
	}

	private Hint() {
		this.x = Game.Constant.HOOP_X;
		this.y = Game.Constant.HOOP_Y + Game.Constant.HOOP_HEIGHT
				- Game.Constant.HINT_HEIGHT;
		this.z = Game.Constant.NEAREST;
		bmps = new Bitmap[2];
		for (int i = 0; i < 2; ++i)
			bmps[i] = BitmapPool.getBitmap(R.drawable.hint0 + i);

		bmpWidth = bmps[0].getWidth();
		bmpHeight = bmps[0].getHeight();
		score = 0;
		speedY = 0;
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		logic();
		if (score == 2) {
			canvas.drawBitmap(bmps[0], x - bmpWidth / 2, y - bmpHeight / 2,
					paint);
		} else if (score == 3) {
			canvas.drawBitmap(bmps[1], x - bmpWidth / 2, y - bmpHeight / 2,
					paint);
		}
	}

	@Override
	protected void logic() {

		// 得分
		if (isGoal) {
			speedY = 3f;
			this.y = Game.Constant.HOOP_Y + Game.Constant.HOOP_HEIGHT
					- Game.Constant.HINT_HEIGHT;
			isGoal = false;
		}

		if (this.y >= Game.Constant.HOOP_Y + Game.Constant.HOOP_HEIGHT) {
			speedY = 0f;
			this.y = Game.Constant.HOOP_Y + Game.Constant.HOOP_HEIGHT
					- Game.Constant.HINT_HEIGHT;
			score = 0;
		}

		y += speedY;
	}

	@Override
	public void release() {
		instance = null;
		super.release();
	}
}
