package com.android13.shooting.screenItems;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.android13.shooting.Game;
import com.android13.shooting.R;
import com.android13.shooting.res.BitmapPool;

/**
 * 计时器，Singleton
 * 
 * @author 11331197 林家访 <98905067@qq.com>
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * @author 11331185 连凌淦 <839021322@qq.com>
 * 
 */
public class Timer extends ScreenItem {

	private static Timer instance;

	private int remainingTime;
	private Bitmap timeBmp;
	private long postTime;
	private long timer;

	public static Timer getInstance() {
		if (instance == null) {
			synchronized (Hint.class) {
				if (instance == null)
					instance = new Timer();
			}
		}
		return instance;
	}

	private Timer() {
		bmps = new Bitmap[10];
		for (int i = 0; i < 10; ++i) {
			bmps[i] = BitmapPool.getBitmap(R.drawable.time0 + i);
		}

		timeBmp = BitmapPool.getBitmap(R.drawable.time);

		this.x = Game.Constant.SCREEN_WIDTH / 2 + bmps[0].getWidth() / 2;
		this.y = Game.Constant.SCREEN_HEIGHT * 0.1f;
		this.z = Game.Constant.NEAREST;
		setRemainingTime(12);
	}

	/**
	 * @return 当前剩余时间
	 */
	public int getTime() {
		return remainingTime;
	}

	/**
	 * 设置当前剩余时间，并初始化定时器
	 * 
	 * @param remainingTime
	 *            剩余时间
	 */
	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
		if (this.remainingTime > 99) {
			this.remainingTime = 99;
		}
		timer = 0;
	}

	@Override
	protected void logic() {

		long curTime = System.currentTimeMillis();
		timer += curTime - postTime;
		if (remainingTime > 0 && timer >= 1000) {
			timer = 0;
			remainingTime -= 1;
		}
		postTime = curTime;
	}

	@Override
	public void draw(final Canvas canvas, final Paint paint) {
		logic();

		int tenFlame;
		int bitFlame;

		tenFlame = this.remainingTime / 10;
		bitFlame = this.remainingTime % 10;

		Matrix matrix = new Matrix();
		matrix.postScale(0.5f, 0.5f);
		canvas.drawBitmap(
				Bitmap.createBitmap(bmps[bitFlame], 0, 0, bmps[bitFlame].getWidth(),
						bmps[bitFlame].getHeight(), matrix, true), x, y, paint);
		canvas.drawBitmap(
				Bitmap.createBitmap(bmps[tenFlame], 0, 0, bmps[tenFlame].getWidth(),
						bmps[tenFlame].getHeight(), matrix, true), x - bmps[0].getWidth() / 2, y,
				paint);

		canvas.drawBitmap(Bitmap.createBitmap(timeBmp, 0, 0, timeBmp.getWidth(),
				timeBmp.getHeight(), matrix, true), x - bmps[0].getWidth() / 2 - timeBmp.getWidth()
				/ 2, y, paint);
	}

	/** 判断计时器是否结束 */
	public boolean isOverTime() {
		return !Game.Constant.IS_TRAIN && this.remainingTime <= 0;
	}

	@Override
	public void release() {
		super.release();
	}
}
