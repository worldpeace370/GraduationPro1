package com.lebron.graduationpro1.videopage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * 视频传输，接收显示
 * Created by wuxiangkun on 2017/2/28.
 * Contact way wuxiangkun2015@163.com
 */

public class VideoSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;

    public VideoSurfaceView(Context context) {
        this(context, null);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setKeepScreenOn(true); //保持屏幕常亮
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    /**
     * 按下home键之后先是执行surfaceDestroyed方法,当前线程其实结束了
     * 重新打开app时会重新执行下面前两个个方法,由于之前的线程销毁了所以需要在surfaceCreated重新开启线程
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 自定义线程类,方便刷新
     */
    private class SurfaceThread extends Thread {
        final SurfaceHolder surfaceHolder;
        String videoUrl;
        boolean isStartRefresh;

        SurfaceThread(SurfaceHolder holder, String videoUrl) {
            this.surfaceHolder = holder;
            this.videoUrl = videoUrl;
        }

        @Override
        public void run() {
            super.run();
            URL url;
            HttpURLConnection connection;
            InputStream inputStream;
            RectF rectF = new RectF(0, 0, 720, 480); // mWidth = 720
            Canvas canvas = null;
            while (isStartRefresh) { //由于是无状态的连接,所以需要一直不断的申请连接
                try {
                    url = new URL(this.videoUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    if (connection.getResponseCode() == 200) { //如果返回码是200表示状态正常
                        inputStream = connection.getInputStream(); //由于图片是一帧一帧的,所以io流每次都是新值
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream); //得到输入流来的Bitmap
                        synchronized (this.surfaceHolder) {
                            canvas = surfaceHolder.lockCanvas();
                            if (canvas != null) { //避免报空指针异常
                                canvas.drawColor(Color.WHITE);
                                canvas.drawBitmap(bitmap, null, rectF, null);
                            }
                        }
                    }
                    connection.disconnect();
                    url = null;
                    connection = null;
                    inputStream = null;
                } catch (IOException e) {
                    isStartRefresh = false;
                    e.printStackTrace();
//                    Message message = mMyHandler.obtainMessage();
//                    message.what = 0;
//                    mMyHandler.sendMessage(message);
                    Log.i(TAG, "connect is fail!");
                    return;
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas); //放在这里为了保证正常的提交画布,避免报空指针异常
                    }
                }
            }
        }
    }
}
