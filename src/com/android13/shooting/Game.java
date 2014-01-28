package com.android13.shooting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;

import com.android13.shooting.res.BitmapPool;
import com.android13.shooting.screenItems.Backboard;
import com.android13.shooting.screenItems.Background;
import com.android13.shooting.screenItems.Ball;
import com.android13.shooting.screenItems.Hint;
import com.android13.shooting.screenItems.Hoop;
import com.android13.shooting.screenItems.LevelNumber;
import com.android13.shooting.screenItems.Score;
import com.android13.shooting.screenItems.ScreenItem;
import com.android13.shooting.screenItems.Timer;
import com.android13.shooting.screenItems.Wind;

/**
 * 游戏控制类，负责游戏整体的逻辑调度
 * 
 * @author 11331197 林家访 <98905067@qq.com>
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * @author 11331185 连凌淦 <839021322@qq.com>
 * 
 */
public class Game {

	/** 根据 z 坐标排序 */
	private static List<ScreenItem> sortedItems;
	private static int level;
	public static int goal_count = 0;

	/** 所有的球的集合 */
	public static ArrayList<Ball> balls = new ArrayList<Ball>();

	/** 获取当前的关卡 */
	public static int getLevel() {
		return level;
	}

	/**
	 * 游戏初始化
	 * 
	 * @param activity
	 *            传入的外层Activity
	 * @param lv
	 *            要初始化的关卡
	 */
	public static void init(Activity activity, int lv) {

		level = lv;
		/** 根据屏幕实际参数，初始化所有距离相关的常量 */
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Constant.init(dm.widthPixels, dm.heightPixels);

		/** 一次加载所有图片资源 */
		BitmapPool.loadAll(activity);
		sortedItems = new ArrayList<ScreenItem>();

		/** 加载当前关卡显示图案 */
		sortedItems.add(new LevelNumber(level));

		/** 加载游戏背景 */
		sortedItems.add(Background.getInstance());
		sortedItems.add(Backboard.getInstance());
		sortedItems.add(Hoop.getInstance());

		/** 加载风的实例 */
		sortedItems.add(Wind.getInstance());
		if (level == 4) {
			Wind.getInstance().windBegin(Constant.WIND_SPEED);
		}

		/** 如果不是训练模式，加载计时器并设置计时器的初始值 */
		if (!Game.Constant.IS_TRAIN) {
			sortedItems.add(Timer.getInstance());
			Timer.getInstance().setRemainingTime(Game.Constant.GAME_REMAIN_TIME + 1);
		}
		/** 加载分数显示 */
		sortedItems.add(Score.getInstance());

		/** 加载进球提示数字 */
		sortedItems.add(Hint.getInstance());
		/** 加载三个篮球 */
		for (int i = 0; i < 3; i++) {
			Ball ball = new Ball();
			sortedItems.add(ball);
			balls.add(ball);
		}
	}

	/**
	 * 挑战模式中，当前关卡通过，加载下一关卡
	 */
	public static void nextLevel() {

		if (level < 4) {
			level++;
		}
		sortedItems.clear();
		balls.clear();

		/** 重新加载ScreenItem */
		sortedItems.add(new LevelNumber(level));
		sortedItems.add(Background.getInstance());
		sortedItems.add(Backboard.getInstance());
		sortedItems.add(Hoop.getInstance());
		sortedItems.add(Wind.getInstance());
		if (level == 4) {
			Wind.getInstance().windBegin(Constant.WIND_SPEED);
		}
		if (!Game.Constant.IS_TRAIN) {
			sortedItems.add(Timer.getInstance());
			Timer.getInstance().setRemainingTime(Game.Constant.GAME_REMAIN_TIME);
		}
		sortedItems.add(Score.getInstance());
		sortedItems.add(Hint.getInstance());
		for (int i = 0; i < 3; i++) {
			Ball ball = new Ball();
			sortedItems.add(ball);
			balls.add(ball);
		}
	}

	/**
	 * 获取当前屏幕中所有的元素
	 * 
	 * @return 一个包含了所有当前屏幕元素的List
	 */
	public static List<ScreenItem> getSortedItems() {
		return sortedItems;
	}

	/**
	 * 调用每一个ScreenItem的draw函数，刷新屏幕
	 * 
	 * @param surfaceHolder
	 * @param canvas
	 * @param paint
	 */
	public static void refreshScreen(SurfaceHolder surfaceHolder, Canvas canvas, Paint paint) {

		gameLogic();

		try {
			canvas = surfaceHolder.lockCanvas();
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
					| Paint.FILTER_BITMAP_FLAG));

			if (canvas != null) {
				for (ScreenItem item : sortedItems)
					item.draw(canvas, paint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null)
				surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	private static void gameLogic() {
		/** 根据 z 坐标对 ScreenItem 排序 */
		Collections.sort(sortedItems);
	}

	/**
	 * 游戏结束，释放当前游戏资源
	 * 
	 * @param destoryScore
	 *            是否释放分数，前期版本遗留，这里最好使用true
	 */
	public static void release(boolean destoryScore) {
		for (int i = 0; i < sortedItems.size(); i++) {
			sortedItems.get(i).release();
		}
		sortedItems.clear();
		balls.clear();
		if (destoryScore) {
			Score.getInstance().setScore(0);
		}
	}

	/**
	 * 定义了游戏的很多全局常量，如视野内三维空间的坐标范围
	 */
	public static class Constant {

		/**
		 * 初始化所有的游戏全局常量
		 * 
		 * @param sw
		 *            屏幕宽度
		 * @param sh
		 *            屏幕高度
		 */
		private static void init(int sw, int sh) {
			SCREEN_WIDTH = sw;
			SCREEN_HEIGHT = sh;
			BACKBOARD_WIDHT = SCREEN_WIDTH / 2;
			BACKBOARD_HEIGHT = SCREEN_WIDTH / 3;
			BACKBOARD_X = SCREEN_WIDTH / 2;
			BACKBOARD_Y = SCREEN_HEIGHT / 3.5f;
			HOOP_WIDTH = BACKBOARD_WIDHT / 2.5f;
			HOOP_HEIGHT = HOOP_WIDTH;
			HOOP_X = SCREEN_WIDTH / 2;
			HOOP_Y = BACKBOARD_Y + BACKBOARD_HEIGHT / 2;
			HOOP_RADIUS = HOOP_WIDTH / 2;
			BALL_RADIUS = HOOP_RADIUS * 1.2f;
			FARTHEST = SCREEN_HEIGHT;
			NEAREST = 0;

			MIDDLE_HOOP_PX = HOOP_X;
			LEFT_HOOP_PX = MIDDLE_HOOP_PX - HOOP_RADIUS;
			LEFT_HOOP_MID_PX = MIDDLE_HOOP_PX - HOOP_RADIUS / 2;
			RIGHT_HOOP_MID_PX = MIDDLE_HOOP_PX + HOOP_RADIUS / 2;
			RIGHT_HOOP_PX = MIDDLE_HOOP_PX + HOOP_RADIUS;
			TOP_HOOP_PY = HOOP_Y - HOOP_HEIGHT / 2;

			float vy = (float) Math.pow(2 * GRAVITY * SCREEN_HEIGHT, 0.5f);
			BOUND_VELOCITY = -(float) (vy / Math.cos(ALPHA)) * 3.45f;
			MOVE_TIME = 0.07f;
			COURT_UPPER_BOUND = 12.6f / 16.0f * SCREEN_HEIGHT;
			COURT_MIDDLE_BOUND = COURT_UPPER_BOUND + (SCREEN_HEIGHT - COURT_UPPER_BOUND) / 2f;

			LEAF_WIDTH = BALL_RADIUS / 2f;
			LEAF_HEIGHT = BALL_RADIUS / 2f;

			WIND_SPEED = 1.5f;
			GAME_PAUSE = false;

			TIME_NUM_WIDTH = SCREEN_WIDTH / 10f;
			TIME_NUM_HEIGHT = SCREEN_HEIGHT / 8f;

			SCORE_NUM_WIDTH = SCREEN_WIDTH / 4f;
			SCORE_NUM_HEIGHT = SCREEN_WIDTH / 2.4f;

			HINT_WIDTH = SCREEN_WIDTH / 7f;
			HINT_HEIGHT = SCREEN_WIDTH / 10f;

			LEVEL_WIDTH = SCREEN_HEIGHT / 40f;
			LEVEL_HEIGHT = SCREEN_WIDTH / 10f;
		}

		/**
		 * 风速
		 */
		public static float WIND_SPEED;

		public static float SCREEN_WIDTH, SCREEN_HEIGHT;
		public static float BACKBOARD_WIDHT, BACKBOARD_HEIGHT, BACKBOARD_X, BACKBOARD_Y;
		public static float HOOP_WIDTH, HOOP_HEIGHT, HOOP_X, HOOP_Y, HOOP_RADIUS;
		public static float LEFT_HOOP_PX, LEFT_HOOP_MID_PX, MIDDLE_HOOP_PX, RIGHT_HOOP_MID_PX,
				RIGHT_HOOP_PX, TOP_HOOP_PY, COURT_UPPER_BOUND, // 场地上边缘
				COURT_MIDDLE_BOUND; // 场地中间
		/**
		 * 虚拟重力大小
		 */
		public static float GRAVITY = 10f;
		/**
		 * 投篮的仰角
		 */
		public static float ALPHA = (float) (Math.PI / 20f);
		/**
		 * 投篮速度的阀值（最大）
		 */
		public static float BOUND_VELOCITY;
		/**
		 * 每帧，篮球对应的移动时间
		 */
		public static float MOVE_TIME;

		/**
		 * #Tips# 篮球在投向篮筐过程中，由于透视，在屏幕上看起来越来越小，BALL_RADIUS
		 * 为篮球投出前 显示的半径，透视效果应使得Ball在篮筐位置附近时半径小于Hoop的半径
		 */
		public static float BALL_RADIUS;
		/** 可见3维区域的 z 坐标范围，最远即为篮板的 z 坐标，最近即为篮球初始位置 */
		public static float FARTHEST, NEAREST;

		/** 树叶的长宽 */
		public static float LEAF_WIDTH, LEAF_HEIGHT;
		/** 背景音乐，开 */
		public static boolean GAME_MUSIC_ON;
		/** 音效，开 */
		public static boolean SOUND_EFFECT_ON;
		/** 游戏是否暂停 */
		public static boolean GAME_PAUSE;

		/** 显示时间数字大小 */
		public static float TIME_NUM_WIDTH, TIME_NUM_HEIGHT;
		/** 显示分数大小 */
		public static float SCORE_NUM_WIDTH, SCORE_NUM_HEIGHT;

		/** 显示进球提示大小 */
		public static float HINT_WIDTH, HINT_HEIGHT;

		/** 显示关卡大小 */
		public static float LEVEL_WIDTH, LEVEL_HEIGHT;

		/** 是否为训练模式 */
		public static boolean IS_TRAIN;

		/** 每个关卡剩余的时间 */
		public static int GAME_REMAIN_TIME;
	}
}
