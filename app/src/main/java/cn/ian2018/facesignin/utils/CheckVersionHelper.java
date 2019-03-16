package cn.ian2018.facesignin.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.lang.ref.WeakReference;

import cn.ian2018.facesignin.MyApplication;
import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.AppVersion;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import okhttp3.Call;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public class CheckVersionHelper {
    private WeakReference<Activity> mActivity;
    private ProgressDialog downloadProgressDialog;
    private Context mContext;

    public CheckVersionHelper(Activity activity) {
        mActivity = new WeakReference<>(activity);
        mContext = MyApplication.getContext();
    }

    public void checkVersionCode(boolean showResult) {
        RetrofitClient.getServiceApi().checkVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppVersion>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logs.e("获取app信息失败  " + e.getMessage());
                    }

                    @Override
                    public void onNext(AppVersion appVersion) {
                        if (appVersion.isFalg()) {
                            // 如果服务器的版本号大于本地的  就更新
                            if (appVersion.getData().getAppVersion() > Utils.getVersionCode()) {
                                // 获取下载地址
                                String appUrl = appVersion.getData().getAppUrl();
                                // 获取新版app描述
                                String appDescribe = appVersion.getData().getAppDescribe();
                                // 如果sd卡可用
                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                    // 展示下载对话框
                                    showUpDataDialog(appDescribe, appUrl);
                                }
                            } else if (showResult) {
                                ToastUtil.show(R.string.check_version_toast_mag);
                            }
                        } else {
                            Logs.e("获取app信息失败");
                        }
                    }
                });
    }

    private void showUpDataDialog(String appDescribe, final String appUrl) {
        if (mActivity.get() == null) return;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity.get());
        // 设置对话框左上角图标
        builder.setIcon(R.mipmap.ic_launcher);
        // 设置不能取消
        builder.setCancelable(false);
        // 设置对话框标题
        builder.setTitle(mContext.getString(R.string.update_title));
        // 设置对话框内容
        builder.setMessage(appDescribe);
        // 设置积极的按钮
        builder.setPositiveButton(mContext.getString(R.string.update_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 下载apk
                downLoadApk(appUrl);
                // 显示一个进度条对话框
                showDownloadProgressDialog();
            }
        });
        // 设置消极的按钮
        builder.setNegativeButton(mContext.getString(R.string.not_yet), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 下载文件
    private void downLoadApk(String appUrl) {
        if (mActivity.get() == null) return;
        OkHttpUtils
                .get()
                .url(appUrl)
                .build()
                .execute(new FileCallBack(mActivity.get().getExternalFilesDir("apk").getPath(), "FaceSign.apk") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtil.show(mContext.getString(R.string.download_failed) + e.toString());
                        Logs.i("下载失败：" + e.toString() + "," + id);
                        if (downloadProgressDialog != null) {
                            downloadProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        ToastUtil.show(mContext.getString(R.string.download_successful) + response.getPath());
                        Logs.i("下载成功,保存路径:" + response.getPath());
                        // 安装应用
                        installApk(response);
                        if (downloadProgressDialog != null) {
                            downloadProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        // 设置进度
                        if (downloadProgressDialog != null) {
                            downloadProgressDialog.setProgress((int) (100 * progress));
                        }
                    }
                });
    }

    private void installApk(File file) {
        if (mActivity.get() == null) return;

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // 判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            // 参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(mActivity.get(), "cn.ian2018.hbu.centre.fileprovider", file);
            // 添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        mActivity.get().startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // 下载的进度条对话框
    protected void showDownloadProgressDialog() {
        if (mActivity.get() == null) return;
        downloadProgressDialog = new ProgressDialog(mActivity.get());
        downloadProgressDialog.setIcon(R.mipmap.ic_launcher);
        downloadProgressDialog.setTitle(mContext.getString(R.string.download_title));
        downloadProgressDialog.setCanceledOnTouchOutside(false);
        downloadProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });
        downloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadProgressDialog.show();
    }
}
