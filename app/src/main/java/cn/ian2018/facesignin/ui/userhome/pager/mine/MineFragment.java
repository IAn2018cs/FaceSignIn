package cn.ian2018.facesignin.ui.userhome.pager.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.faceserver.FaceServer;
import cn.ian2018.facesignin.ui.activity.FeedbackActivity;
import cn.ian2018.facesignin.ui.base.BaseFragment;
import cn.ian2018.facesignin.ui.setting.SettingActivity;
import cn.ian2018.facesignin.utils.Utils;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/5
 */
public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.IMineView, View.OnClickListener {

    private ImageView mAvatarIv;
    private TextView mNameTv, mGradeTv, mClassTv;
    private File mFaceImgFile;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    protected void initView(View view) {
        mAvatarIv = view.findViewById(R.id.iv_pic);
        mNameTv = view.findViewById(R.id.tv_name);
        mGradeTv = view.findViewById(R.id.tv_grade);
        mClassTv = view.findViewById(R.id.tv_class);

        LinearLayout feedbackLayout = view.findViewById(R.id.ll_feedback);
        LinearLayout repairLayout = view.findViewById(R.id.ll_repair);
        LinearLayout settingLayout = view.findViewById(R.id.ll_setting);

        mAvatarIv.setOnClickListener(this);
        feedbackLayout.setOnClickListener(this);
        repairLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mNameTv.setText(SpUtil.getString(Constant.USER_NAME, ""));
        mGradeTv.setText(String.format(getString(R.string.mine_grade_text), SpUtil.getInt(Constant.USER_GRADE, 18)));
        mClassTv.setText(String.format(getString(R.string.mine_class_text), SpUtil.getString(Constant.USER_CLASS, "")));

        // 如果没有修改头像  使用人脸注册的照片
        mFaceImgFile = Utils.getCurrentFaceImg(getContext());
        if (TextUtils.isEmpty(SpUtil.getString(Constant.USER_IMAGE, "")) && mFaceImgFile != null) {
            Glide.with(getContext()).load(mFaceImgFile)
                    .placeholder(R.drawable.avatar_placeholder)
                    .centerCrop()
                    .error(R.drawable.avatar_placeholder)
                    .into(mAvatarIv);
        } else {
            Glide.with(getContext()).load(SpUtil.getString(Constant.USER_IMAGE, ""))
                    .placeholder(R.drawable.avatar_placeholder)
                    .centerCrop()
                    .error(R.drawable.avatar_placeholder)
                    .into(mAvatarIv);
        }
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pic:
                showAvatarDialog();
                break;
            case R.id.ll_feedback:
                FeedbackActivity.start(getContext());
                break;
            case R.id.ll_repair:
                getPresenter().repair();
                break;
            case R.id.ll_setting:
                SettingActivity.start(getContext());
                break;
        }
    }

    // 显示报修对话框
    @Override
    public void showRepairDialog(String name, final String phone) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        //设置对话框左上角图标
        builder.setIcon(R.mipmap.ic_launcher);
        //设置对话框标题
        builder.setTitle(R.string.mine_repair_title);
        //设置对话框内容
        builder.setMessage(String.format(getString(R.string.mine_dialog_msg), name, phone));
        //设置积极的按钮
        builder.setPositiveButton(R.string.mine_dialog_call_phone, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 跳转到拨号界面
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });
        //设置消极的按钮
        builder.setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    // 放大头像
    private void showAvatarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getContext(), R.layout.dialog_image_avatar, null);
        dialog.setView(view, 0, 0, 0, 0);

        ImageView iv_avatar = view.findViewById(R.id.iv_avatar);

        if (TextUtils.isEmpty(SpUtil.getString(Constant.USER_IMAGE, "")) && mFaceImgFile != null) {
            Glide.with(getContext()).load(mFaceImgFile)
                    .placeholder(R.drawable.avatar_placeholder)
                    .centerCrop()
                    .error(R.drawable.avatar_placeholder)
                    .into(iv_avatar);
        } else {
            Glide.with(getContext()).load(SpUtil.getString(Constant.USER_IMAGE, ""))
                    .placeholder(R.drawable.avatar_placeholder)
                    .centerCrop()
                    .error(R.drawable.avatar_placeholder)
                    .into(iv_avatar);
        }

        dialog.show();
    }
}
