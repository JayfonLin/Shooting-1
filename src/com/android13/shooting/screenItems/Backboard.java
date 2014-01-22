package com.android13.shooting.screenItems;

import com.android13.shooting.Game;
import com.android13.shooting.R;
import com.android13.shooting.res.BitmapPool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 篮板，Singleton
 * @author Tiga <liangkangabc@gmail.com>
 *
 */
public class Backboard extends ScreenItem {
	/**
	 * 移动方向：0 不动
	 *          1 向左
	 *          2 向右
	 */
	public int move_direction;
	public float speedX;
	public Hoop hoop = Hoop.getInstance();
	private static Backboard instance;
	public static Backboard getInstance() {
		if(instance == null) {
			synchronized (Backboard.class) {
				if(instance == null)
					instance = new Backboard();
			}
		}
		return instance;
	}
	
	private Backboard() {
		this.x = Game.Constant.BACKBOARD_X;
		this.y = Game.Constant.BACKBOARD_Y;
		this.z = Game.Constant.FARTHEST;
		move_direction = 0;
		speedX = 2f;
		bmps = new Bitmap[1];
		bmps[0] = BitmapPool.getBitmap(R.drawable.backboard);
		
		bmpWidth = bmps[0].getWidth();
		bmpHeight = bmps[0].getHeight();
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		logic();
		canvas.drawBitmap(bmps[0], x - bmpWidth / 2, y - bmpHeight / 2, paint);
	}
	
	@Override
	public void release() {
		instance = null;
		super.release();
	}
	@Override
	public void logic(){
		//如果是第3关及以上的关卡，篮板左右移动
		if (Game.getLevel() >= 3){
			if (move_direction == 0){
				move_direction = 1;
			}else if (move_direction == 1){
				if (x-Game.Constant.BACKBOARD_WIDHT/2 <= 2){
					move_direction = 2;
				}
			}else if (move_direction == 2){
				if (x+Game.Constant.BACKBOARD_WIDHT/2 >= Game.Constant.SCREEN_WIDTH-2){
					move_direction = 1;
				}
			}
			
			if (move_direction == 1){
				x -= speedX;
			}else if (move_direction == 2){
				x += speedX;
			}
			//篮框要跟随篮板移动
			hoop.x = x;
		}
	}
}
