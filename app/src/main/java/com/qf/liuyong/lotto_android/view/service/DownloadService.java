package com.qf.liuyong.lotto_android.view.service;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.android.volley.Request;
import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.app.App;
import com.qf.liuyong.lotto_android.model.bean.UpdateResult;
import com.qf.liuyong.lotto_android.model.http.JkhDataHandler;
import com.qf.liuyong.lotto_android.model.http.RequestInfo;
import com.qf.liuyong.lotto_android.model.http.ResponseHandler;
import com.qf.liuyong.lotto_android.model.http.ResponseListener;
import com.qf.liuyong.lotto_android.model.http.exception.RequestError;
import com.qf.liuyong.lotto_android.model.http.internal.PageData;
import com.qf.liuyong.lotto_android.presenter.BusProvider;
import com.qf.liuyong.lotto_android.presenter.FinishAppEvent;
import com.qf.liuyong.lotto_android.utils.IOUtils;
import com.qf.liuyong.lotto_android.utils.PreferencesUtils;
import com.qf.liuyong.lotto_android.utils.ToastUtils;
import com.qf.liuyong.lotto_android.utils.UrlUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/1/30 0030.
 */
public class DownloadService {

    private DownloadManager downloadManager;
    private Context mContext;
    private static DownloadService mDownloadService;
    private static final String DL_ID = "downloadId";
    private double version;

    private DownloadService() {
    }

    public static DownloadService getInstance() {

        if (mDownloadService == null) {
            mDownloadService = new DownloadService();
        }
        return mDownloadService;
    }

    public void init(Context context) {
        this.mContext = context;
        downloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
    }

    public void registReceiver(Context context) {
        this.mContext = context;
        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void unregistReceiver(Context context) {
        this.mContext = context;
        context.unregisterReceiver(receiver);
    }

    public void startDownload(String path, String name, double version, String size) {
        this.version = version;
        int state = mContext.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            ToastUtils.show(mContext, "请启用下载管理器，否则无法下载！", 1);
            try {
                //Open the specific App Info page:
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + "com.android.providers.downloads"));
                mContext.startActivity(intent);

            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                //Open the generic Apps page:
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                mContext.startActivity(intent);
            }
        } else {
            try {
                File hasDownloadFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/lotto_app_" + version + ".apk");
                int hasSize = (int) Float.parseFloat((IOUtils.byteCountToDisplaySize(hasDownloadFile.length())).replace(" MB", ""));
                int needSize = (int) Float.parseFloat(size.replace(" MB", ""));
                if (hasDownloadFile.exists() && hasSize == needSize) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/lotto_app_" + version + ".apk"),
                            "application/vnd.android.package-archive");
                    mContext.startActivity(intent);
                    return;
                }
                if (!PreferencesUtils.containsKey(DL_ID)) {
                    Uri resource = Uri.parse(path);
                    DownloadManager.Request request = new DownloadManager.Request(resource);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                    request.setAllowedOverRoaming(false);
                    //设置文件类型
                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path));
                    request.setMimeType(mimeString);
                    //在通知栏中显示
                    request.setShowRunningNotification(true);
                    request.setVisibleInDownloadsUi(true);
                    //sdcard的目录下的download文件夹
                    request.setDestinationInExternalPublicDir("/Download", "lotto_app_" + version + ".apk");
                    request.setTitle(name);
                    long id = downloadManager.enqueue(request);
                    //保存id
                    PreferencesUtils.putLong(DL_ID, id);
                } else {
                    //下载已经开始，检查状态
                    queryDownloadStatus("lotto_app_" + version + ".apk");
                }
            } catch (Exception e) {
                ToastUtils.show(mContext, "下载失败", 1);
            }
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
            queryDownloadStatus("lotto_app_" + version + ".apk");
        }
    };

    private void queryDownloadStatus(String path) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(PreferencesUtils.getLong(DL_ID, 0));
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                case DownloadManager.STATUS_PENDING:
                case DownloadManager.STATUS_RUNNING:
                    //正在下载，不做任何事情
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    //完成
                    PreferencesUtils.removeKey(DL_ID);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + path),
                            "application/vnd.android.package-archive");
                    mContext.startActivity(intent);
                    break;
                case DownloadManager.STATUS_FAILED:
                    //清除已下载的内容，重新下载
                    PreferencesUtils.removeKey(DL_ID);
                    break;
            }
        }
    }

    /**
     * 检查更新
     */
    public void checkUpdate(final boolean clickCheck) {
        JkhDataHandler<UpdateResult> jkhDataHandler = new JkhDataHandler<>(UpdateResult.class);
        RequestInfo info = new RequestInfo.Builder().method(Request.Method.POST)
                .requestMode(RequestInfo.REQUEST_NETWORK)
                .url(UrlUtils.GET_VERSION_INFO).build();
        App.request.request(info, jkhDataHandler, new ResponseListener<PageData<UpdateResult>>() {

            @Override
            public void onHandlerComplete(ResponseHandler handler, final PageData<UpdateResult> pageData) {
                if (pageData == null) {
                    return;
                }
                if (pageData.getErrorCode() == 200 && pageData.getT() != null) {
                    final String versionNum = pageData.getT().getVersionNum();
                    final String url = pageData.getT().getDownloadurl();
                    final String size = pageData.getT().getSize();
                    String desc = pageData.getT().getVersionDesc();
                    if (!TextUtils.isEmpty(versionNum) && Double.parseDouble(versionNum) > App.instance.getVersion()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                                .setTitle("版本更新")
                                .setMessage(desc)
                                .setPositiveButton(R.string.okey, null)
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if ("0".equals(pageData.getT().getForce())) {
                                            BusProvider.getInstance().post(new FinishAppEvent());
                                        }
                                    }
                                });
                        builder.setCancelable(false);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //确定按钮的点击回调
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //不是强制更新点击确定按钮对话框消失，是强制更新点击确认对话框不消失必须下载并安装新包
                                if (!"0".equals(pageData.getT().getForce())) {
                                    alertDialog.dismiss();
                                }
                                try {
                                    DownloadService.getInstance().startDownload(url, "星火乐投", Double.parseDouble(versionNum), size);
                                } catch (Exception e) {
                                    ToastUtils.show(mContext, "下载异常，请确认手机下载管理器已启用。", 1);
                                }
                            }
                        });
                    } else if (!TextUtils.isEmpty(versionNum) && Double.parseDouble(versionNum) <= App.instance.getVersion() && clickCheck) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                                .setMessage("当前已是最新版本")
                                .setPositiveButton(R.string.okey, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.create().show();
                    }
                } else if (clickCheck) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                            .setMessage("获取版本信息失败")
                            .setPositiveButton(R.string.okey, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                }
            }

            @Override
            public void onError(RequestError error) {

            }

            @Override
            public void onCacheResponse(String data) {

            }

            @Override
            public void onResponse(String data) {
            }
        });
    }
}
