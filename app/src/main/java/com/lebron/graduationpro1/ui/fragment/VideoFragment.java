package com.lebron.graduationpro1.ui.fragment;

import android.app.Activity;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.AppApplication;
import com.lebron.graduationpro1.ui.activity.MainActivity;
import com.lebron.graduationpro1.utils.NetStatusUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {} interface
 * to handle interaction events.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    @BindView(R.id.surfaceView)
    SurfaceView mSurfaceView;
    //得到一个unbinder对象然后在视图销毁的时候调用unbinder.unbind().在Fragment生命周期中这样用,Activity不用这么做
    private Unbinder unbinder;
    //视频地址
    private String mVideoUrl;
    private SurfaceHolder mSurfaceHolder;
    //每帧图像的Bitmap,用于保存图片到SD卡
    private Bitmap mBitmap;
    private int mWidth;
    private int mHeight;
    private static final String TAG = "VideoFragment";
    private Activity mMainActivity;
    private MyHandler mMyHandler;
    private SurfaceThread mSurfaceThread;

    private static class MyHandler extends Handler {
        WeakReference<VideoFragment> weakReference;
        public MyHandler(VideoFragment fragment){
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            VideoFragment fragment = weakReference.get();
            if (fragment != null){//如果fragment仍然在弱引用中,执行...
                switch (msg.what){
                    case 0:
                        fragment.createNetFailedDialog("貌似ip地址不对哦");
                        break;
                }
            }
        }
    }
    private static final String URL_PARAM = "url_param";

    public VideoFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param videoUrl Parameter 1.
     * @return A new instance of fragment VideoFragment.
     */
    public static VideoFragment newInstance(String videoUrl) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(URL_PARAM, videoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity){
            mMainActivity = activity;
        }else {
            throw new IllegalArgumentException("The activity must to be MainActivity");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideoUrl = getArguments().getString(URL_PARAM);
            mVideoUrl = "http://" + "192.168.1.100" + ":8080/?action=snapshot";
        }
        mMyHandler = new MyHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        mWidth = mMainActivity.getWindowManager().getDefaultDisplay().getWidth();
        mHeight = mMainActivity.getWindowManager().getDefaultDisplay().getHeight();
        Log.i(TAG, "onCreateView: mWidth = " + mWidth + ", mHeight = " + mHeight);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSurfaceView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 网络连接失败,弹出对话框提醒
     */
    private void createNetFailedDialog(String errorInfo) {
        MaterialDialog dialog = new MaterialDialog.Builder(mMainActivity)
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

    private void initSurfaceView() {
        //保持屏幕常亮
        mSurfaceView.setKeepScreenOn(true);
        mSurfaceHolder = mSurfaceView.getHolder();
        //按下home键之后先是执行surfaceDestroyed方法,当前线程其实结束了
        //返回app时会重新执行下面前两个个方法,由于之前的线程销毁了所以需要在surfaceCreated重新开启线程
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.i(TAG, "surfaceCreated: ");
                //如果网络没问题,开始刷新SurfaceView,线程一定要在这里创建.因为按下home键重新返回的时候还会再次执行该方法
                if (NetStatusUtils.isNetWorkConnected(AppApplication.getAppContext())){
                    mSurfaceThread = new SurfaceThread(mSurfaceHolder, mVideoUrl);
                    mSurfaceThread.isStartRefresh = true;
                    mSurfaceThread.start();
                }else {
                    //弹出对话框提示
                    createNetFailedDialog("貌似没有网啦～");
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.i(TAG, "surfaceChanged: ");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.i(TAG, "surfaceDestroyed: ");
                //取消线程的刷新,退出线程
                mSurfaceThread.isStartRefresh = false;
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
    private class SurfaceThread extends Thread{
        final SurfaceHolder surfaceHolder;
        String videoUrl;
        boolean isStartRefresh;
        SurfaceThread(SurfaceHolder holder, String videoUrl){
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
            //由于是无状态的连接,所以需要一直不断的申请连接
            while (isStartRefresh){
                try {
                    url = new URL(this.videoUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    //如果返回码是200表示状态正常
                    if (connection.getResponseCode() == 200){
                        //由于图片是一帧一帧的,所以io流每次都是新值
                        inputStream = connection.getInputStream();
                        //得到输入流来的Bitmap
                        mBitmap = BitmapFactory.decodeStream(inputStream);
                        synchronized (this.surfaceHolder){
                            canvas = surfaceHolder.lockCanvas();
                            //避免报空指针异常
                            if (canvas != null){
                                canvas.drawColor(Color.WHITE);
                                canvas.drawBitmap(mBitmap, null, rectF, null);
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
                }finally {
                    if (canvas != null){
                        //放在这里为了保证正常的提交画布,避免报空指针异常
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
