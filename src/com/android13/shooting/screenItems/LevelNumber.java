package com.android13.shooting.screenItems;

import com.android13.shooting.Game;
import com.android13.shooting.R;
import com.android13.shooting.R.drawable;
import com.android13.shooting.res.BitmapPool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class LevelNumber extends ScreenItem {

	private int levelNum;
	private Bitmap bmp;
	private Bitmap levelBmp;

	public LevelNumber(int level) {
		this.levelNum = level;
		bmp = BitmapPool.getBitmap(R.drawable.level1 + level - 1);
		levelBmp = BitmapPool.getBitmap(R.drawable.level);
		bmpWidth = bmp.getWidth();
		bmpHeight = bmp.getHeight();

		this.x = Game.Constant.SCREEN_WIDTH / 2;
		this.y = Game.Constant.SCREEN_WIDTH / 50f ;
		this.z = Game.Constant.NEAREST;
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bmp, x - bmpWidth / 2, y - bmpHeight / 2, paint);
		canvas.drawBitmap(levelBmp, x - bmpWidth / 2 - levelBmp.getWidth(), y
				- bmpHeight / 2, paint);
	}
}
