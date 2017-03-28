package com.lebron.graduationpro1.minepage.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.kevin.crop.UCrop;
import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.minepage.view.PicSelectPopupWindow;
import com.lebron.graduationpro1.utils.LebronPreference;
import com.lebron.graduationpro1.view.CustomSettingItem;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 用户中心Activity
 */
public class UserCenterActivity extends BaseActivity implements View.OnClickListener
        , PicSelectPopupWindow.OnSelectedListener {
    private static final String TAG = "UserCenterActivity";
    private static final int GALLERY_REQUEST_CODE = 0;    // 相册选图标记
    private static final int CAMERA_REQUEST_CODE = 1;    // 相机拍照标记
    private String mTempPhotoPath; // 拍照临时图片
    private Uri mDestinationUri;   // 剪切后图像文件
    private CollapsingToolbarLayout mCollapsingToolbar;
    private LinearLayout mHeadLayout;
    private PicSelectPopupWindow mPicSelectPopupWindow;
    private CircleImageView mImgHead;
    private CustomSettingItem mNickNameItem;
    private CustomSettingItem mPhoneNumItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        bindViews();
        setListener();
        init();
    }

    @Override
    protected void bindViews() {
        initToolbar(R.string.mine_account);
        /**
         * 设置工具栏标题, 设置展开和收缩的颜色
         */
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle("我的账户");
        mCollapsingToolbar.setExpandedTitleColor(Color.BLACK);
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

        mHeadLayout = ((LinearLayout) findViewById(R.id.layout_head));
        mImgHead = (CircleImageView) findViewById(R.id.img_head);
        mNickNameItem = (CustomSettingItem) findViewById(R.id.item_nick_name);
        mPhoneNumItem = (CustomSettingItem) findViewById(R.id.item_phone_num);
    }

    @Override
    protected void setListener() {
        mHeadLayout.setOnClickListener(this);
        mNickNameItem.setOnClickListener(this);
    }

    @Override
    protected void init() {
        mDestinationUri = Uri.fromFile(new File(getCacheDir(), "cropImage.jpeg"));
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        mPhoneNumItem.setSubText("18744024728");
        if (null != LebronPreference.getInstance().getHeadImageUrl()){
            String urlStr = LebronPreference.getInstance().getHeadImageUrl();
            Bitmap bitmap = BitmapFactory.decodeFile(urlStr);
            mImgHead.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_head:
                showHeadImageChoiceWindow();
                break;
            case R.id.item_nick_name:
                startActivityByClassName(ChangeNickNameActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 弹出头像选择渠道的界面,设置窗体整体透明度,窗体消失后恢复窗体透明度
     */
    private void showHeadImageChoiceWindow() {
        changeWindowAlpha(0.7f);
        if (mPicSelectPopupWindow == null) {
            mPicSelectPopupWindow = new PicSelectPopupWindow(
                    UserCenterActivity.this);
        }
        mPicSelectPopupWindow.setOnSelectedListener(this);
        mPicSelectPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPicSelectPopupWindow.showAtLocation(
                UserCenterActivity.this.findViewById(R.id.toolbar),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPicSelectPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeWindowAlpha(1.0f);
            }
        });
    }

    /**
     * 图片选择的回调
     *
     * @param view     点击的按键View
     * @param position 按键对应的编号
     */
    @Override
    public void onSelected(View view, int position) {
        switch (position) {
            case 0:
                // "拍照"按钮被点击了
                takePhoto();
                break;
            case 1:
                // "从相册选择"按钮被点击了
                pickFromGallery();
                break;
            case 2:
                // "取消"按钮被点击了
                dismissPicSelectWindow();
                break;
        }
    }

    private void takePhoto() {
        dismissPicSelectWindow();
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTempPhotoPath)));
        startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);

    }

    private void pickFromGallery() {
        dismissPicSelectWindow();
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
    }

    private void dismissPicSelectWindow() {
        if (mPicSelectPopupWindow != null && mPicSelectPopupWindow.isShowing()) {
            mPicSelectPopupWindow.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:   // 调用相机拍照
                    File temp = new File(mTempPhotoPath);
                    startCropActivity(Uri.fromFile(temp));
                    break;
                case GALLERY_REQUEST_CODE:  // 直接从相册获取
                    startCropActivity(intent.getData());
                    break;
                case UCrop.REQUEST_CROP:    // 裁剪图片结果
                    handleCropResult(intent);
                    break;
                case UCrop.RESULT_ERROR:    // 裁剪图片错误
                    handleCropError(intent);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startCropActivity(Uri uri) {
        UCrop.of(uri, mDestinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(512, 512)
                .withTargetActivity(CropActivity.class)
                .start(this);
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        deleteTempPhotoFile();
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            onPictureSelected(resultUri, bitmap);
        } else {
            Toast.makeText(this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result) {
        deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除拍照临时文件
     */
    private void deleteTempPhotoFile() {
        File tempFile = new File(mTempPhotoPath);
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }

    private void onPictureSelected(Uri fileUri, Bitmap bitmap) {
        mImgHead.setImageBitmap(bitmap);
        String filePath = fileUri.getEncodedPath();
        String imagePath = Uri.decode(filePath);
        LebronPreference.getInstance().saveHeadImageUrl(imagePath);
        Intent intent = new Intent("picture.has.cropped"); // 指定广播目标action
        sendBroadcast(intent);
    }

}
