package cn.ian2018.facesignin.ui.setting.changeinfo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

import java.io.File;
import java.util.concurrent.Executors;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.ui.widget.CircleImageView;
import cn.ian2018.facesignin.utils.Logs;
import cn.ian2018.facesignin.utils.ToastUtil;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/3/16
 */
public class ChangeInfoFragment extends TakePhotoFragment implements View.OnClickListener {

    private UploadManager mUploadManager;
    private TakePhoto mTakePhoto;

    private EditText mChangeNameEt, mChangeGradeEt, mChangeClassEt, mChangePhoneEt;
    private CircleImageView mCircleImageView;

    private String mOldImage, mOldName, mOldClass, mOldPhone;
    private int mOldGrade;
    private String imagePath = "";
    public DialogPlus mChooseImageDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_change_info, container, false);

        initView(inflate);

        initData();

        return inflate;
    }

    private void initData() {
        // 初始化七牛云配置
        Configuration config = new Configuration.Builder().build();
        mUploadManager = new UploadManager(config);

        // 获取图片选择对象
        mTakePhoto = getTakePhoto();

        mOldImage = SpUtil.getString(Constant.USER_IMAGE, "");
        mOldName = SpUtil.getString(Constant.USER_NAME, "");
        mOldGrade = SpUtil.getInt(Constant.USER_GRADE, 0);
        mOldClass = SpUtil.getString(Constant.USER_CLASS, "");
        mOldPhone = SpUtil.getString(Constant.USER_PHONE, "");

        mChangeNameEt.setText(mOldName);
        mChangeGradeEt.setText(mOldGrade + "");
        mChangeClassEt.setText(mOldClass);
        mChangePhoneEt.setText(mOldPhone);
        Glide.with(this).load(mOldImage).centerCrop()
                .error(R.drawable.avatar_placeholder)
                .into(mCircleImageView);
    }

    private void initView(View rootView) {
        Button submitChangeButton = rootView.findViewById(R.id.bt_information_change);
        mCircleImageView = rootView.findViewById(R.id.icon);
        mChangeNameEt = rootView.findViewById(R.id.et_change_name);
        mChangeGradeEt = rootView.findViewById(R.id.et_change_grade);
        mChangeClassEt = rootView.findViewById(R.id.et_change_class);
        mChangePhoneEt = rootView.findViewById(R.id.et_change_phone);

        mCircleImageView.setOnClickListener(this);
        submitChangeButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon:
                showChooseImageDialog();
                break;
            case R.id.bt_information_change:
                break;
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        imagePath = result.getImages().get(0).getCompressPath();
        Glide.with(this).load(imagePath).centerCrop().into(mCircleImageView);
    }

    // 弹出底部对话框
    private void showChooseImageDialog() {
        mChooseImageDialog = DialogPlus.newDialog(getContext())
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            // 拍照
                            case R.id.bt_takePhoto:
                                fromTakePhoto();
                                dialog.dismiss();
                                break;
                            // 从相册选择
                            case R.id.bt_choosePhoto:
                                fromDCIMPhoto();
                                dialog.dismiss();
                                break;
                            case R.id.bt_cancel:
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .setHeader(R.layout.dialog_header)
                .setContentHolder(new ViewHolder(R.layout.dialog_choose))
                .setExpanded(true)
                .create();
        mChooseImageDialog.show();
    }

    // 从相机获取图片并裁剪
    private void fromTakePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        boolean fileExists = true;
        if (!file.getParentFile().exists()) {
            fileExists = file.getParentFile().mkdirs();
        }
        if (fileExists) {
            Uri imageUri = Uri.fromFile(file);
            // 进行压缩
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    CompressConfig config = new CompressConfig.Builder()
                            .setMaxSize(102400)
                            .setMaxPixel(800)
                            // ture保留原图，false删除原图，当且仅当类型为CAMERA此配置才有效
                            .enableReserveRaw(false)
                            .create();
                    mTakePhoto.onEnableCompress(config, false);
                }
            });

            TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
            // 是否纠正拍照的照片旋转角度
            builder.setCorrectImage(true);
            // 设置TakePhoto选项
            mTakePhoto.setTakePhotoOptions(builder.create());
            try {
                mTakePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
            } catch (Exception e) {
                Logs.e("打开相机失败");
                ToastUtil.show("打开相机失败");
            }
        }
    }

    // 从相册选择
    private void fromDCIMPhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        // 进行压缩
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                CompressConfig config = new CompressConfig.Builder()
                        .setMaxSize(102400)
                        .setMaxPixel(800)
                        .create();
                mTakePhoto.onEnableCompress(config, false);
            }
        });

        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        // 是否使用TakePhoto自带相册
        builder.setWithOwnGallery(false);
        // 是否纠正拍照的照片旋转角度
        builder.setCorrectImage(true);
        // 设置TakePhoto选项
        mTakePhoto.setTakePhotoOptions(builder.create());
        // 从相册中获取图片并裁剪
        mTakePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
    }

    // 获取剪裁配置
    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        // 设置宽高尺寸
        builder.setOutputX(800).setOutputY(800);
        // 设置是否使用自带剪裁工具
        builder.setWithOwnCrop(false);
        return builder.create();
    }
}
