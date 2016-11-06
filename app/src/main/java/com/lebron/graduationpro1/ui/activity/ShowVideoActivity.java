package com.lebron.graduationpro1.ui.activity;

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
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.AppApplication;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.utils.MyActivityManager;
import com.lebron.graduationpro1.utils.NetWorkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 显示监控视频的Activity,用到SurfaceView
 */
public class ShowVideoActivity extends BaseActivity {
    @BindView(R.id.surfaceView) SurfaceView mSurfaceView;
    //得到一个unbinder对象然后在视图销毁的时候调用unbinder.unbind().在Fragment生命周期中这样用,Activity不用这么做
    private Unbinder unbinder;
    private String mVideoUrl;
    private SurfaceHolder mSurfaceHolder;
    private boolean isStartRefresh = true;
    //每帧图像的Bitmap,用于保存图片到SD卡
    private Bitmap mBitmap;
    private int mWidth;
    private int mHeight;
    private String TAG = "ShowVideoActivity";
    private MyHandler mMyHandler;

    private static class MyHandler extends Handler{
        WeakReference<ShowVideoActivity> weakReference;
        public MyHandler(ShowVideoActivity activity){
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ShowVideoActivity activity = weakReference.get();
            if (activity != null){//如果activity仍然在弱引用中,执行...
                switch (msg.what){
                    case 0:
                        activity.createNetFailedDialog("貌似ip地址不对哦");
                        break;
                }
            }else { //否则不执行
            }
        }
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this);
        //加入ActivityManager
        MyActivityManager.getInstance().addActivity(this);
        initSurfaceView();
    }

    @Override
    protected void initData() {
        mMyHandler = new MyHandler(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_video;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //接收传入的视频url地址
        getVideoUrl();
    }

    private void initSurfaceView() {
        //保持屏幕常亮
        mSurfaceView.setKeepScreenOn(true);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.i(TAG, "surfaceCreated: ");
                //如果网络没问题,开始刷新SurfaceView
                if (NetWorkUtils.isNetWorkConnected(AppApplication.getAppContext())){
                    new Thread(refreshVideoThread).start();
                }else {
                    //弹出对话框提示
                    createNetFailedDialog("貌似没有网啦～");
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        mWidth = getWindowManager().getDefaultDisplay().getWidth();
        mHeight = getWindowManager().getDefaultDisplay().getHeight();
    }

    /**
     * 网络连接失败,弹出对话框提醒
     */
    private void createNetFailedDialog(String errorInfo) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(errorInfo)
                .content("本项目开源, 你想怎么改?快来联系我~")
                .positiveText("Github")
                .negativeText("Zhihu")
                .callback(new MaterialDialog.Callback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/worldpeace370")));
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.zhihu.com/people/hu-qi-xing-66")));
                    }
                }).build();
        dialog.show();
    }

    private Runnable refreshVideoThread = new Runnable() {
        @Override
        public void run() {
            URL url;
            HttpURLConnection connection;
            InputStream inputStream;
            RectF rectF = new RectF(0, 0, mWidth, 4 * mHeight / 3);
            //由于是无状态的连接,所以需要一直不断的申请连接
            while (isStartRefresh){
                try {
                    url = new URL(mVideoUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    //如果返回码是200表示状态正常
                    if (connection.getResponseCode() == 200){
                        //由于图片是一帧一帧的,所以io流每次都是新值
                        inputStream = connection.getInputStream();
                        //得到输入流来的Bitmap
                        mBitmap = BitmapFactory.decodeStream(inputStream);
                        Canvas canvas = mSurfaceHolder.lockCanvas();
                        canvas.drawColor(Color.WHITE);
                        canvas.drawBitmap(mBitmap, null, rectF, null);
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
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
                }
            }
        }
    };

    private void getVideoUrl() {
        Intent intent = getIntent();
        if (null != intent){
            Bundle bundle = intent.getExtras();
            mVideoUrl = "http://" + bundle.getString("video_url") + ":8080/?action=snapshot";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        //取消线程的刷新,退出线程
        isStartRefresh = false;
    }
}
