package com.bluewhaledt.saylove.util;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;

import com.bluewhaledt.saylove.ZhenaiApplication;


/**
 * 本地视频播放工具类
 * 
 * @author stevenFang
 * @date 2015-1-23
 * @version 1.0.0
 */
public class VideoPlayUtils implements SurfaceHolder.Callback,
		OnVideoSizeChangedListener {


	private MediaPlayer mediaPlayer;

	private SurfaceView surfaceView;

	private SurfaceHolder surfaceHolder;

	private String filePath;


	private OnCompletionListener mOnCompletionListener;

	private boolean isFullScreenH = false;


	public VideoPlayUtils( SurfaceView surfaceView,
						  String url,  boolean isFullScreenH) {
		this.isFullScreenH = isFullScreenH;
		this.filePath = url;
		this.surfaceView = surfaceView;
		surFaceWidth = surfaceView.getMeasuredWidth();
		surFaceHeight = surfaceView.getMeasuredHeight();

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		surfaceHolder = surfaceView.getHolder();// SurfaceHolder是SurfaceView的控制接口
		surfaceHolder.addCallback(this);// 因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public VideoPlayUtils( SurfaceView surfaceView,
						  String url) {
		this.filePath = url;
		this.surfaceView = surfaceView;
		surFaceWidth = surfaceView.getMeasuredWidth();
		surFaceHeight = surfaceView.getMeasuredHeight();
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		surfaceHolder = surfaceView.getHolder();// SurfaceHolder是SurfaceView的控制接口
		surfaceHolder.addCallback(this);// 因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	private boolean isprepareAsync = false;

	public void play() {
		try {
			mediaPlayer.reset();// 重置为初始状态
			mediaPlayer.setLooping(looping);
			mediaPlayer.setOnPreparedListener(onPreparedListener);
			if (mOnCompletionListener != null) {
				mediaPlayer.setOnCompletionListener(mOnCompletionListener);
			}
			mediaPlayer.setDisplay(surfaceView.getHolder());// 设置video影片以surfaceviewholder播放
			mediaPlayer.setDataSource(filePath);// 设置路径
			mediaPlayer.prepareAsync();// 缓冲
			isprepareAsync = true;
		} catch (Exception e) {
			isprepareAsync = false;
			e.printStackTrace();
		}

	}

	private boolean looping = false;

	/**
	 * 设置是否循环播放。
	 */
	public void setLooping(boolean looping) {
		this.looping = looping;

	}

	/**
	 * 为Media Player的播放完成事件绑定事件监听器。
	 * 
	 * @param onCompletionListener
	 */
	public void setOnCompletionListener(
			OnCompletionListener onCompletionListener) {
		this.mOnCompletionListener = onCompletionListener;
	}

	public void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	public void start() {
		if (mediaPlayer != null) {
			mediaPlayer.start();
		}
	}

	public boolean isPlaying() {
		if (mediaPlayer != null) {
			return mediaPlayer.isPlaying();
		}
		return false;
	}

	public void seekTo(int seek) {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(seek);
		}
	}

	public void stop() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (!isprepareAsync) {
				if (mediaPlayer == null) {
					mediaPlayer = new MediaPlayer();
				}
				mediaPlayer.reset();// 重置为初始状态
				mediaPlayer.setLooping(looping);
				mediaPlayer.setOnPreparedListener(onPreparedListener);
				if (mOnCompletionListener != null) {
					mediaPlayer.setOnCompletionListener(mOnCompletionListener);
				}
				mediaPlayer.setDisplay(surfaceView.getHolder());// 设置video影片以surfaceviewholder播放
				mediaPlayer.setDataSource(filePath);
				mediaPlayer.prepareAsync();// 缓冲
				isprepareAsync = true;
			}
		} catch (Exception e) {
			isprepareAsync = false;
			e.printStackTrace();
		}

	}

	public OnPreparedListener onPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			int width = mediaPlayer.getVideoWidth();
			int height = mediaPlayer.getVideoHeight();
			if (width != 0 && height != 0) {
				float rat = surFaceHeight * 1f / height;
				float ratW = surFaceWidth * 1f / width;
				float values = 1;
				if (rat > 0 && ratW > 0) {
					values = rat < ratW ? rat : ratW;
				}
				int surfaceWidth = (int) (width * values);
				int surfaceHeight = (int) (height * values);
				
				surfaceHolder.setFixedSize(surfaceWidth, surfaceHeight);// 设置视频高宽
				LayoutParams params = surfaceView.getLayoutParams();
				params.width = isFullScreenH ? ZhenaiApplication.getInstance().getScreenWidth()
						: surfaceWidth;
				params.height = isFullScreenH ? ZhenaiApplication.getInstance().getScreenHeight()
						: surfaceHeight;
				surfaceView.setLayoutParams(params);

			}
			mediaPlayer.start();
		}
	};

	private int surFaceWidth = 0;

	private int surFaceHeight = 0;

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
		surFaceWidth = width;
		surFaceHeight = height;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isprepareAsync=false;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 释放资源
	 */
	public void release() {
		try {
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					mediaPlayer.reset();
				}
				mediaPlayer.release();
			}
		} catch (Exception e) {
		}
	}




	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

	}

}
