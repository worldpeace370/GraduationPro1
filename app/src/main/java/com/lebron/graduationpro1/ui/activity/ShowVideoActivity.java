package com.lebron.graduationpro1.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //强制横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_show_video);
        unbinder = ButterKnife.bind(this);
        //设置透明状态栏,父类的方法
        setTransparent();
        //加入ActivityManager
        MyActivityManager.getInstance().addActivity(this);
        //接收传入的视频url地址
        getVideoUrl();
        initSurfaceView();
    }

    private void initSurfaceView() {
        //保持屏幕常亮
        mSurfaceView.setKeepScreenOn(true);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //如果网络没问题,开始刷新SurfaceView
                if (NetWorkUtils.isNetWorkConnected(AppApplication.getAppContext())){
                    new Thread(refreshVideoThread).start();
                }else {
                    //弹出对话框提示
                    createNetFailedDialog();
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
    private void createNetFailedDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("貌似没有网啦")
                .content("本项目开源, 你想怎么改?快来联系我~")
                .positiveText("Github")
                .negativeText("知乎")
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
            RectF rectF = new RectF(0, 0, mWidth, mHeight);
            try {
                url = new URL(mVideoUrl);
                connection = (HttpURLConnection) url.openConnection();
                //设置get请求方式,默认也是该值
                connection.setRequestMethod("GET");
                //设置为Input模式,默认也是该值
                connection.setDoInput(true);
                //如果返回码是200表示状态正常
                if (connection.getResponseCode() == 200){
                    while (isStartRefresh){
                        //由于图片是一帧一帧的,所以io流每次都是新值
                        inputStream = connection.getInputStream();
                        //得到输入流来的Bitmap
                        mBitmap = BitmapFactory.decodeStream(inputStream);
                        Canvas canvas = mSurfaceHolder.lockCanvas();
                        canvas.drawColor(Color.WHITE);
                        canvas.drawBitmap(mBitmap, null, rectF, null);
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void getVideoUrl() {
        Intent intent = getIntent();
        if (null != intent){
            Bundle bundle = intent.getExtras();
            mVideoUrl = bundle.getString("video_url");
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
