package cn.ian2018.facesignin.ui.setting.changeinfo;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.Executors;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.AuthTokenResult;
import cn.ian2018.facesignin.bean.SimpleResult;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import cn.ian2018.facesignin.ui.widget.CircleImageView;
import cn.ian2018.facesignin.utils.Logs;
import cn.ian2018.facesignin.utils.ToastUtil;
import cn.ian2018.facesignin.utils.Utils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    private String mChangeName, mChangeClass, mChangePhone;
    private int mOldGrade;
    private int mChangeGrade;
    private String imagePath = "";

    public DialogPlus mChooseImageDialog;
    private ProgressDialog mProgressDialog;


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

        // 如果没有修改头像  使用人脸注册的照片
        File faceImgFile = Utils.getCurrentFaceImg(getContext());
        if (TextUtils.isEmpty(mOldImage) && faceImgFile != null) {
            Glide.with(getContext()).load(faceImgFile)
                    .error(R.drawable.avatar_placeholder)
                    .into(mCircleImageView);
        } else {
            Glide.with(this).load(mOldImage).centerCrop()
                    .error(R.drawable.avatar_placeholder)
                    .into(mCircleImageView);
        }
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
                mChangeName = mChangeNameEt.getText().toString().trim();
                try {
                    mChangeGrade = Integer.valueOf(mChangeGradeEt.getText().toString().trim());
                } catch (Exception e) {
                    mChangeGrade = 15;
                    Logs.e("转换异常" + e.getMessage());
                }
                mChangeClass = mChangeClassEt.getText().toString().trim();
                mChangePhone = mChangePhoneEt.getText().toString().trim();
                if (!mChangeClass.equals(mOldClass) || mChangeGrade != mOldGrade || !mChangeName.equals(mOldName)
                        || !mChangePhone.equals(mOldPhone) || !imagePath.equals("")) {
                    if (imagePath.equals("")) {
                        // 上传修改的信息
                        changeInfo(mOldImage);
                    } else {
                        // 先将图片上传
                        uploadImage2qiniu();
                    }
                } else {
                    ToastUtil.show(R.string.change_info_no_change);
                }
                break;
        }
    }

    // 先上传图片
    private void uploadImage2qiniu() {
        showProgressDialog(R.string.change_info_dialog_msg);
        RetrofitClient.getServiceApi().getToken(Constant.QINIU_STORAGE_NAME)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AuthTokenResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        closeProgressDialog();
                        Logs.e("获取Token失败" + e.getMessage());
                        ToastUtil.show(R.string.change_info_fail);
                    }

                    @Override
                    public void onNext(AuthTokenResult authTokenResult) {
                        if (authTokenResult.isSucessed()) {
                            String token = authTokenResult.getData();
                            // 获取文件名称
                            String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                            Logs.i("文件路径：" + imagePath);
                            Logs.i("文件名称：" + fileName);
                            mUploadManager.put(imagePath, fileName, token,
                                    new UpCompletionHandler() {
                                        @Override
                                        public void complete(String key, ResponseInfo info, JSONObject res) {
                                            if (info.isOK()) {
                                                String changeImageUrl = Constant.QINIU_STORAGE_URL + key;
                                                // 修改信息
                                                changeInfo(changeImageUrl);
                                                Logs.i("上传图片成功");
                                            } else {
                                                Logs.e("上传图片失败！！！！");
                                                ToastUtil.show(R.string.change_info_fail);
                                            }
                                            Logs.i(key + ",\r\n " + info + ",\r\n " + res);
                                        }
                                    }, null);
                        } else {
                            Logs.e("获取Token失败" + authTokenResult.getData());
                            ToastUtil.show(R.string.change_info_fail);
                        }
                    }
                });
    }

    private void changeInfo(String image) {
        showProgressDialog(R.string.change_info_dialog_msg);
        RetrofitClient.getServiceApi().uploadChangeInfo(SpUtil.getString(Constant.ACCOUNT, ""),
                mChangeName, mChangeGrade, mChangeClass, 1, mChangePhone, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SimpleResult>() {
                    @Override
                    public void onCompleted() {
                        closeProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        closeProgressDialog();
                        ToastUtil.show(R.string.change_info_fail);
                        Logs.e("修改信息失败：" + e.getMessage());
                    }

                    @Override
                    public void onNext(SimpleResult simpleResult) {
                        if (simpleResult.isSucessed()) {
                            ToastUtil.show(R.string.change_info_success);

                            SpUtil.putString(Constant.USER_NAME, mChangeName);
                            SpUtil.putString(Constant.USER_CLASS, mChangeClass);
                            SpUtil.putInt(Constant.USER_GRADE, mChangeGrade);
                            SpUtil.putString(Constant.USER_PHONE, mChangePhone);
                            SpUtil.putString(Constant.USER_IMAGE, image);
                        } else {
                            ToastUtil.show(R.string.change_info_fail);
                            Logs.e("修改信息失败：");
                        }
                    }
                });
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        imagePath = result.getImages().get(0).getCompressPath();
        Glide.with(this).load(imagePath).centerCrop().into(mCircleImageView);
    }

    // 弹出底部对话框
    private void showChooseImageDialog() {
        if (mChooseImageDialog == null) {
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
        }
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
                ToastUtil.show(R.string.open_camera_fail);
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

    public void showProgressDialog(int msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
        }
        mProgressDialog.setMessage(getString(msg));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void closeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
