package com.android13.shooting.screenItems;

import com.android13.shooting.R;
import com.android13.shooting.res.BitmapPool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 背景，Singleton
 * 
 * @author 11331197 林家访 <98905067@qq.com>
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * @author 11331185 连凌淦 <839021322@qq.com>
 * 
 */
public class Background extends ScreenItem {

	private static Background instance;

	public static Background getInstance() {
		if (instance == null) {
			synchronized (Background.class) {
				if (instance == null)
					instance = new Background();
			}
		}
		return instance;
	}

	private Background() {
		this.z = Integer.MAX_VALUE;

		bmps = new Bitmap[1];
		bmps[0] = BitmapPool.getBitmap(R.drawable.background);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bmps[0], 0, 0, paint);
	}

	@Override
	public void release() {
		instance = null;
		super.release();
	}
}
