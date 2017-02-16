package com.lebron.graduationpro1.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.AppApplication;
import com.lebron.graduationpro1.base.BaseFragment;
import com.lebron.graduationpro1.utils.AppLog;
import com.lebron.graduationpro1.utils.NetStatusUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 视频传输显示 Fragment
 */
public class VideoFragment extends BaseFragment {

    private static final String TAG = "VideoFragment";
    private SurfaceView mSurfaceView;
    private View mRootView;
    private String mVideoUrlString;     //视频地址
    private SurfaceHolder mSurfaceHolder;
    private int mWidth;
    private MyHandler mMyHandler;
    private SurfaceThread mSurfaceThread;

    private static class MyHandler extends Handler {
        WeakReference<VideoFragment> weakReference;

        MyHandler(VideoFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            VideoFragment fragment = weakReference.get();
            if (fragment != null) { //如果fragment仍然在弱引用中,执行...
                switch (msg.what) {
                    case 0:
                        fragment.createNetFailedDialog("貌似ip地址不对哦");
                        break;
                }
            }
        }
    }

    public VideoFragment() {
    }

    //重写onAttach(Context context)也可以,互相调用而已
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AppLog.i(TAG, "onAttach: 执行了");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideoUrlString = "http://" + "192.168.1.100" + ":8080/?action=snapshot";
        }
        mMyHandler = new MyHandler(this);
        AppLog.i(TAG, "onCreate: 执行了");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_video, container, false);
            bindViews(mRootView);
            setListener();
            init();
        }
        AppLog.i(TAG, "onCreateView: 执行了");
        return mRootView;
    }

    @Override
    protected void bindViews(View view) {
        mSurfaceView = ((SurfaceView) view.findViewById(R.id.surfaceView));
        mWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth(); //红米2A 720
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init() {
        initSurfaceView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppLog.i(TAG, "onActivityCreated: 执行了");
    }

    @Override
    public void onStart() {
        super.onStart();
        AppLog.i(TAG, "onStart: 执行了");
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLog.i(TAG, "onResume: 执行了");
    }

    @Override
    public void onPause() {
        super.onPause();
        AppLog.i(TAG, "onPause: 执行了");
    }

    @Override
    public void onStop() {
        super.onStop();
        AppLog.i(TAG, "onStop: 执行了");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppLog.i(TAG, "onDestroyView: 执行了");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLog.i(TAG, "onDestroy: 执行了");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        AppLog.i(TAG, "onDetach: 执行了");
    }

    /**
     * 网络连接失败,弹出对话框提醒
     */
    private void createNetFailedDialog(String errorInfo) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(errorInfo)
                .content("本项目开源, 你想怎么改?快来联系我~")
                .positiveText("Github")
                .negativeText("Zhihu")
                .callback(new MaterialDialog.Callback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/worldpeace370")));
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.zhihu.com/people/hu-qi-xing-66")));
                    }
                }).build();
        dialog.show();
    }

    private void initSurfaceView() {
        mSurfaceView.setKeepScreenOn(true); //保持屏幕常亮
        mSurfaceHolder = mSurfaceView.getHolder();
        /**
         * 按下home键之后先是执行surfaceDestroyed方法,当前线程其实结束了
         * 重新打开app时会重新执行下面前两个个方法,由于之前的线程销毁了所以需要在surfaceCreated重新开启线程
         */
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.i(TAG, "surfaceCreated: ");
                //如果网络没问题,开始刷新SurfaceView,线程一定要在这里创建.因为按下home键重新返回的时候还会再次执行该方法
                if (NetStatusUtils.isWiFiConnected(AppApplication.getAppContext())) { //判断是否是wifi网络
                    mSurfaceThread = new SurfaceThread(mSurfaceHolder, mVideoUrlString);
                    mSurfaceThread.isStartRefresh = true;
                    mSurfaceThread.start();
                } else {
                    createNetFailedDialog("貌似没有网啦～"); //弹出对话框提示
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.i(TAG, "surfaceChanged: ");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.i(TAG, "surfaceDestroyed: ");
                mSurfaceThread.isStartRefresh = false; //取消线程的刷新,退出线程
                try {
                    mSurfaceThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
            RectF rectF = new RectF(0, 0, mWidth, 2 * mWidth / 3);
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
                    Message message = mMyHandler.obtainMessage();
                    message.what = 0;
                    mMyHandler.sendMessage(message);
                    Log.i(TAG, "connect is error!");
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
