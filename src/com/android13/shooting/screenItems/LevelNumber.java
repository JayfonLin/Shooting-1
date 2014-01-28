package com.android13.shooting.screenItems;

import com.android13.shooting.Game;
import com.android13.shooting.R;
import com.android13.shooting.res.BitmapPool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 关卡数字
 * 
 * @author 11331197 林家访 <98905067@qq.com>
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * @author 11331185 连凌淦 <839021322@qq.com>
 * 
 */
public class LevelNumber extends ScreenItem {

	private Bitmap bmp;
	private Bitmap levelBmp;

	public LevelNumber(int level) {
		bmp = BitmapPool.getBitmap(R.drawable.level1 + level - 1);
		levelBmp = BitmapPool.getBitmap(R.drawable.level);
		bmpWidth = bmp.getWidth();
		bmpHeight = bmp.getHeight();

		this.x = Game.Constant.SCREEN_WIDTH / 2 + bmpWidth;
		this.y = Game.Constant.SCREEN_WIDTH / 20f + bmpHeight / 2;
		this.z = Game.Constant.NEAREST;
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bmp, x - bmpWidth / 2, y - bmpHeight / 2, paint);
		canvas.drawBitmap(levelBmp, x - bmpWidth / 2 - levelBmp.getWidth(), y - bmpHeight / 2,
				paint);
	}
}
