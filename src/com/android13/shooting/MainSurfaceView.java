package com.android13.shooting;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.android13.shooting.screenItems.Ball;
import com.android13.shooting.screenItems.Score;
import com.android13.shooting.screenItems.Timer;

/**
 * 显示游戏主体画面的SurfaceView
 * 
 * @author 11331197 林家访 <98905067@qq.com>
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * @author 11331185 连凌淦 <839021322@qq.com>
 * 
 */
public class MainSurfaceView extends SurfaceView implements Callback, Runnable, OnTouchListener {

	private boolean flag;
	private boolean isBegin;
	private SurfaceHolder surfaceHolder;
	private Canvas canvas;
	private Paint paint;
	private MyGestureListener myGestureListener;
	private GestureDetector gestureDetector;
	private Thread thread;
	private List<Ball> balls = Game.balls;
	private Handler handler;

	public MainSurfaceView(Context context, Handler handler) {
		super(context);
		this.handler = handler;
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		myGestureListener = new MyGestureListener(this);
		gestureDetector = new GestureDetector(context, myGestureListener);
		setLongClickable(true);
		setClickable(true);
		setOnTouchListener(this);
		setKeepScreenOn(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		flag = true;
		thread = new Thread(this);
		thread.start();
		if (!isBegin) {
			isBegin = true;
			Message msg = new Message();
			msg.what = MainActivity.MESSAGE_NEXTLEVEL;
			handler.sendMessage(msg);
		} else {
			Message msg = new Message();
			msg.what = MainActivity.MESSAGE_RESTART;
			handler.sendMessage(msg);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}

	@Override
	public void run() {
		while (flag) {
			if (!Game.Constant.GAME_PAUSE) {
				long start = System.currentTimeMillis();

				Game.refreshScreen(surfaceHolder, canvas, paint);

				long end = System.currentTimeMillis();
				try {
					// 控制FPS 为标准的 30
					if (end - start < 30)
						Thread.sleep(30 - (end - start));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (!Game.Constant.IS_TRAIN) {
					boolean allUnshoot = true;
					for (Ball ball : balls) {
						if (!ball.unShoot) {
							allUnshoot = false;
							break;
						}
					}
					if (Timer.getInstance().isOverTime() && allUnshoot) {
						startNextLevel();
					}
				}
			}
		}
	}

	private void startNextLevel() {
		if ((Game.getLevel() == 1 && Score.getInstance().getScore() >= 40)
				|| (Game.getLevel() == 2 && Score.getInstance().getScore() >= 90)
				|| ((Game.getLevel() == 3 && Score.getInstance().getScore() >= 150))) {
			Game.nextLevel();
			Message msg = new Message();
			msg.what = MainActivity.MESSAGE_NEXTLEVEL;
			handler.sendMessage(msg);
		} else {
			Game.Constant.GAME_PAUSE = true;
			Message msg = new Message();
			msg.what = MainActivity.MESSAGE_FINISH;
			handler.sendMessage(msg);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (((!Game.Constant.IS_TRAIN && !Timer.getInstance().isOverTime()) || Game.Constant.IS_TRAIN)
				&& !Game.Constant.GAME_PAUSE) {
			gestureDetector.onTouchEvent(event);
		}
		return true;
	}

	public void forceRefreshScreen() {
		Game.refreshScreen(surfaceHolder, canvas, paint);
	}
}
