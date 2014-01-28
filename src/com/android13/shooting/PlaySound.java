package com.android13.shooting;

import java.util.HashMap;

import android.content.Context;
import android.media.SoundPool;

/**
 * 初始化播放音乐的接口
 * 
 * @author 11331197 林家访 <98905067@qq.com>
 * @author 11331173 李明宽 <sysu_limingkuan@163.com>
 * @author 11331185 连凌淦 <839021322@qq.com>
 * 
 */
public class PlaySound {

	private static HashMap<String, Integer> hashMap;
	public static SoundPool soundPool;

	public static void init(Context context, int maxStreams, int streamType, int srcQuality) {
		soundPool = new SoundPool(maxStreams, streamType, srcQuality);
		hashMap = new HashMap<String, Integer>();
		hashMap.put("ball", soundPool.load(context, R.raw.sound_ballhit, 1));
		hashMap.put("wind", soundPool.load(context, R.raw.sound_wind, 1));
	}

	public static void play(String sound, int loop) {
		float volume = 1f;
		soundPool.play(hashMap.get(sound), volume, volume, 1, loop, 1.0f);
	}

}
