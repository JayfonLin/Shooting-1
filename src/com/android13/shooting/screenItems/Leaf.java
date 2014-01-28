package com.android13.shooting.screenItems;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.android13.shooting.Game;
import com.android13.shooting.R;
import com.android13.shooting.res.BitmapPool;

/**
 * 叶子
 * 
 * @author 11331197 林家访 <98905067@qq.com>
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * @author 11331185 连凌淦 <839021322@qq.com>
 * 
 */
public class Leaf extends ScreenItem {

	public Bitmap leafBitmap;
	// 叶子的旋转角度
	private float rotation;
	// 叶子的水平速度
	private float speedX;

	// 叶子的垂直速度
	private float speedY;

	// 叶子的旋转速度
	private float rotateSpeed;
	private Random random;

	public Leaf() {
		random = new Random();
		this.x = random.nextInt((int) Game.Constant.SCREEN_WIDTH);
		this.y = random.nextInt((int) Game.Constant.SCREEN_HEIGHT);
		this.z = Game.Constant.NEAREST;
		leafBitmap = BitmapPool.getBitmap(R.drawable.leaf);
		this.rotateSpeed = random.nextFloat() * 20 + 10;
		this.speedX = 0;
		speedY = random.nextFloat() * 12f - 2f;
	}

	@Override
	protected void logic() {
		this.x += this.speedX * 5f;
		this.y += this.speedY;
		if (x > Game.Constant.SCREEN_WIDTH) {
			x = 0;
		} else if (x < 0) {
			x = Game.Constant.SCREEN_WIDTH;
		}
		if (y > Game.Constant.SCREEN_HEIGHT) {
			y = 0;
		} else if (y < 0) {
			y = Game.Constant.SCREEN_HEIGHT;
		}
		this.rotation += this.rotateSpeed;
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		logic();
		canvas.save();
		canvas.rotate(this.rotation, this.x, this.y);
		canvas.drawBitmap(leafBitmap, this.x, this.y, paint);
		canvas.restore();
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}
}
