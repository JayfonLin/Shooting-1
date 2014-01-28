package com.android13.shooting;

import java.util.ArrayList;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.android13.shooting.Game.Constant;
import com.android13.shooting.screenItems.Ball;

/**
 * 手指滑动的监听器，判断手指滑动并且给球初速度
 * @author 11331197 林家访 <98905067@qq.com>
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * @author 11331185 连凌淦 <839021322@qq.com>
 *
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

	private ArrayList<Ball> balls;

	public MyGestureListener(MainSurfaceView pMainSurfaceView) {
		super();
		balls = Game.balls;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return super.onDown(e);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		for (Ball ball : balls) {
			// 判断按下的点是不是在篮球内部
			if (Math.abs((double) e1.getX() - ball.getX()) < Constant.BALL_RADIUS
					&& Math.abs((double) e1.getY() - ball.getY()) < Constant.BALL_RADIUS) {
				// 是，则判断篮球是否已经投出
				if (ball.unShoot) {
					// 根据滑动的距离给篮球设置初始速度
					ball.setSpeedX((e2.getX() - e1.getX()) * 0.3f);
					// Y,Z轴方向的合速度
					float speedYZ = ((e2.getY() - e1.getY())) * 2f;
					/**
					 * 投篮速度不能超过阀值
					 */
					if (speedYZ < 0 && speedYZ < Game.Constant.BOUND_VELOCITY) {
						speedYZ = Game.Constant.BOUND_VELOCITY;
					}
					ball.setSpeedY((float) (speedYZ * Math
							.cos(Game.Constant.ALPHA)));
					ball.setSpeedZ(-(float) (speedYZ * Math
							.sin(Game.Constant.ALPHA)) * 1.15f);

					ball.unShoot = false;
				}
				return true;
			}
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		super.onLongPress(e);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return super.onSingleTapUp(e);
	}

}
