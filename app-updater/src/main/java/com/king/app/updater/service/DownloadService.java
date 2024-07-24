package com.king.app.updater.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import com.king.app.updater.R;
import com.king.app.updater.UpdateConfig;
import com.king.app.updater.callback.UpdateCallback;
import com.king.app.updater.constant.Constants;
import com.king.app.updater.http.HttpManager;
import com.king.app.updater.http.IHttpManager;
import com.king.app.updater.notify.INotification;
import com.king.app.updater.notify.NotificationImpl;
import com.king.app.updater.util.AppUtils;
import com.king.app.updater.util.LogUtils;

import java.io.File;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * Download Service
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class DownloadService extends Service {
    /**
     * DownloadBinder
     */
    private DownloadBinder mDownloadBinder = new DownloadBinder();
    /**
     * Whether downloading is in progress to prevent duplicate downloads.
     */
    private boolean isDownloading;
    /**
     * Number of re-downloads after failure
     */
    private int mCount = 0;
    /**
     * Http Manager
     */
    private IHttpManager mHttpManager;
    /**
     * Update callback
     */
    private UpdateCallback mUpdateCallback;
    /**
     * Notification bar
     */
    private INotification mNotification;
    /**
     * APK file
     */
    private File mApkFile;

    /**
     * Get Context
     *
     * @return
     */
    private Context getContext() {
        return this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            boolean isStop = intent.getBooleanExtra(Constants.KEY_STOP_DOWNLOAD_SERVICE, false);
            if (isStop) {
                stopDownload();
            } else if (!isDownloading) {
                // Whether to trigger repeated downloads through the notification bar
                boolean isReDownload = intent.getBooleanExtra(Constants.KEY_RE_DOWNLOAD, false);
                if (isReDownload) {
                    mCount++;
                }
                // Get configuration information
                UpdateConfig config = intent.getParcelableExtra(Constants.KEY_UPDATE_CONFIG);

                startDownload(config);
            } else {
                LogUtils.w("Please do not repeat the download.");
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }


    //----------------------------------------

    /**
     * start download
     *
     * @param config
     */
    private void startDownload(@NonNull UpdateConfig config) {
        startDownload(config, mHttpManager, mUpdateCallback, mNotification);
    }

    /**
     * start download
     *
     * @param config
     * @param httpManager
     * @param callback
     */
    private void startDownload(@NonNull UpdateConfig config, @Nullable IHttpManager httpManager, @Nullable UpdateCallback callback, @Nullable INotification notification) {
        if (callback != null) {
            callback.onDownloading(isDownloading);
        }

        if (isDownloading) {
            LogUtils.w("Please do not repeat the download.");
            return;
        }

        String url = config.getUrl();
        String path = config.getPath();
        String filename = config.getFilename();

        // If the save path is empty, use the cache path
        if (TextUtils.isEmpty(path)) {
            path = AppUtils.getApkCacheFilesDir(getContext());
        }
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        // If the file name is empty, use the path
        if (TextUtils.isEmpty(filename)) {
            filename = AppUtils.getAppFullName(getContext(), url, getResources().getString(R.string.app_name));
        }

        mApkFile = new File(path, filename);
        // Does the file exist?
        if (mApkFile.exists()) {
            long versionCode = config.getVersionCode();
            String apkMD5 = config.getApkMD5();
            // Does the same apk exist?
            boolean isExistApk = false;
            if (!TextUtils.isEmpty(apkMD5)) {
                // If MD5 exists, check MD5 first
                LogUtils.d(String.format(Locale.getDefault(), "UpdateConfig.apkMD5: %s", apkMD5));
                isExistApk = AppUtils.verifyFileMD5(mApkFile, apkMD5);
            } else if (versionCode > 0) {
                // If versionCode exists, check versionCode
                LogUtils.d(String.format(Locale.getDefault(), "UpdateConfig.versionCode: %d", versionCode));
                isExistApk = AppUtils.apkExists(getContext(), versionCode, mApkFile);
            }

            if (isExistApk) {
                // The APK to be downloaded already exists locally
                LogUtils.d("CacheFile: " + mApkFile);
                if (config.isInstallApk()) {
                    String authority = config.getAuthority();
                    // If empty, the default
                    if (TextUtils.isEmpty(authority)) {
                        authority = AppUtils.getFileProviderAuthority(getContext());
                    }
                    AppUtils.installApk(getContext(), mApkFile, authority);
                }
                if (callback != null) {
                    callback.onFinish(mApkFile);
                }
                stopService();
                return;
            }

            // Delete old files
            mApkFile.delete();
        }
        LogUtils.d("File: " + mApkFile);
        this.mUpdateCallback = callback;
        IHttpManager.DownloadCallback downloadCallback = new AppDownloadCallback(getContext(), this, config, mApkFile, callback, getNotification(notification));
        getHttpManager(httpManager).download(url, mApkFile.getAbsolutePath(), config.getRequestProperty(), downloadCallback);

    }

    /**
     * Get IHttpManager
     *
     * @param httpManager {@link IHttpManager}
     * @return
     */
    @NonNull
    private IHttpManager getHttpManager(@Nullable IHttpManager httpManager) {
        if (httpManager != null) {
            mHttpManager = httpManager;
        }
        if (mHttpManager == null) {
            mHttpManager = HttpManager.getInstance();
        }
        return mHttpManager;
    }

    /**
     * Get INotification
     *
     * @param notification {@link INotification}
     * @return
     */
    @NonNull
    private INotification getNotification(@Nullable INotification notification) {
        if (notification != null) {
            mNotification = notification;
        }
        if (mNotification == null) {
            mNotification = new NotificationImpl();
        }
        return mNotification;
    }

    /**
     * Stop downloading
     */
    private void stopDownload() {
        if (mHttpManager != null) {
            mHttpManager.cancel();
        }
    }

    /**
     * Out of service
     */
    private void stopService() {
        mCount = 0;
        stopSelf();
    }


    //---------------------------------------- DownloadCallback

    /**
     * App download callback interface
     */
    public static class AppDownloadCallback implements IHttpManager.DownloadCallback {

        private Context context;

        private DownloadService downloadService;

        public UpdateConfig config;

        private boolean isShowNotification;

        private int notifyId;

        private String channelId;

        private String channelName;

        private int notificationIcon;

        private boolean isInstallApk;

        private String authority;

        private boolean isShowPercentage;

        private boolean isReDownload;

        private boolean isDeleteCancelFile;

        private boolean isSupportCancelDownload;

        private UpdateCallback callback;

        private INotification notification;

        /**
         * Last update progress, used to refresh at reduced frequency
         */
        private int lastProgress;
        /**
         * Last progress update time, used to reduce refresh rate
         */
        private long lastTime;
        /**
         * APK file
         */
        private File apkFile;

        private AppDownloadCallback(Context context, DownloadService downloadService, UpdateConfig config, File apkFile, UpdateCallback callback, INotification notification) {
            this.context = context;
            this.downloadService = downloadService;
            this.config = config;
            this.apkFile = apkFile;
            this.callback = callback;
            this.notification = notification;
            this.isShowNotification = config.isShowNotification();
            this.notifyId = config.getNotificationId();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.channelId = TextUtils.isEmpty(config.getChannelId()) ? Constants.DEFAULT_NOTIFICATION_CHANNEL_ID : config.getChannelId();
                this.channelName = TextUtils.isEmpty(config.getChannelName()) ? Constants.DEFAULT_NOTIFICATION_CHANNEL_NAME : config.getChannelName();
            }
            if (config.getNotificationIcon() <= 0) {
                this.notificationIcon = AppUtils.getAppIcon(context);
            } else {
                this.notificationIcon = config.getNotificationIcon();
            }

            this.isInstallApk = config.isInstallApk();

            this.authority = config.getAuthority();
            // If empty, the default
            if (TextUtils.isEmpty(config.getAuthority())) {
                authority = AppUtils.getFileProviderAuthority(context);
            }

            this.isShowPercentage = config.isShowPercentage();
            this.isDeleteCancelFile = config.isDeleteCancelFile();
            this.isSupportCancelDownload = config.isSupportCancelDownload();

            // Support re-downloading when download fails. Re-downloading is allowed only when the number of re-downloads does not exceed the limit.
            this.isReDownload = config.isReDownload() && downloadService.mCount < config.getReDownloads();

        }

        @Override
        public void onStart(String url) {
            LogUtils.i("url: " + url);
            downloadService.isDownloading = true;
            lastProgress = 0;
            if (isShowNotification && notification != null) {
                notification.onStart(context, notifyId, channelId, channelName, notificationIcon, getString(R.string.app_updater_start_notification_title), getString(R.string.app_updater_start_notification_content), config.isVibrate(), config.isSound(), isSupportCancelDownload);
            }

            if (callback != null) {
                callback.onStart(url);
            }
        }

        @Override
        public void onProgress(long progress, long total) {
            boolean isChanged = false;
            long curTime = System.currentTimeMillis();
            // Reduce the update frequency
            if (lastTime + Constants.MINIMUM_INTERVAL_MILLIS < curTime || progress == total) {
                lastTime = curTime;
                int progressPercentage = 0;
                if (total > 0) {
                    progressPercentage = Math.round(progress * 1.0f / total * 100.0f);
                    // Update only when the percentage changes
                    if (progressPercentage != lastProgress) {
                        isChanged = true;
                        lastProgress = progressPercentage;
                    }
                    LogUtils.i(String.format(Locale.getDefault(), "%d%%\t| %d/%d", progressPercentage, progress, total));
                } else {
                    LogUtils.i(String.format(Locale.getDefault(), "%d/%d", progress, total));
                }

                if (isShowNotification && notification != null) {
                    String content = context.getString(R.string.app_updater_progress_notification_content);
                    if (total > 0) {
                        if (isShowPercentage) {
                            content = String.format(Locale.getDefault(), "%s%d%%", content, progressPercentage);
                        }
                        notification.onProgress(context, notifyId, channelId, notificationIcon, context.getString(R.string.app_updater_progress_notification_title), content, progressPercentage, 100, isSupportCancelDownload);
                    } else {
                        notification.onProgress(context, notifyId, channelId, notificationIcon, context.getString(R.string.app_updater_progress_notification_title), content, (int) progress, Constants.NONE, isSupportCancelDownload);
                    }
                }
            }

            if (callback != null && total > 0) {
                callback.onProgress(progress, total, isChanged);
            }

        }

        @Override
        public void onFinish(File file) {
            LogUtils.d("File: " + file);
            downloadService.isDownloading = false;
            if (isShowNotification && notification != null) {
                notification.onFinish(context, notifyId, channelId, notificationIcon, getString(R.string.app_updater_finish_notification_title), getString(R.string.app_updater_finish_notification_content), file, authority);
            }
            if (isInstallApk) {
                AppUtils.installApk(context, file, authority);
            }
            if (callback != null) {
                callback.onFinish(file);
            }
            downloadService.stopService();
        }

        @Override
        public void onError(Exception e) {
            LogUtils.w(e.getMessage());
            downloadService.isDownloading = false;
            if (isShowNotification && notification != null) {
                String content = isReDownload ? getString(R.string.app_updater_error_notification_content_re_download) : getString(R.string.app_updater_error_notification_content);
                notification.onError(context, notifyId, channelId, notificationIcon, getString(R.string.app_updater_error_notification_title), content, isReDownload, config);
            }

            if (callback != null) {
                callback.onError(e);
            }
            if (!isReDownload) {
                downloadService.stopService();
            }

        }

        @Override
        public void onCancel() {
            LogUtils.d("Cancel download.");
            downloadService.isDownloading = false;
            if (isShowNotification && notification != null) {
                notification.onCancel(context, notifyId);
            }
            if (callback != null) {
                callback.onCancel();
            }
            if (isDeleteCancelFile && apkFile != null) {
                apkFile.delete();
            }
            downloadService.stopService();
        }

        private String getString(@StringRes int resId) {
            return context.getString(resId);
        }
    }

    @Override
    public void onDestroy() {
        isDownloading = false;
        mHttpManager = null;
        mUpdateCallback = null;
        mNotification = null;
        super.onDestroy();
    }

    //---------------------------------------- Binder

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mDownloadBinder;
    }

    /**
     * Provide binding service for download
     */
    public class DownloadBinder extends Binder {

        /**
         * start download
         *
         * @param config {@link UpdateConfig}
         */
        public void start(@NonNull UpdateConfig config) {
            start(config, null);
        }

        /**
         * start download
         *
         * @param config   {@link UpdateConfig}
         * @param callback {@link UpdateCallback}
         */
        public void start(@NonNull UpdateConfig config, @Nullable UpdateCallback callback) {
            start(config, null, callback);
        }

        /**
         * start download
         *
         * @param config      {@link UpdateConfig}
         * @param httpManager {@link IHttpManager}
         * @param callback    {@link UpdateCallback}
         */
        public void start(@NonNull UpdateConfig config, @Nullable IHttpManager httpManager, @Nullable UpdateCallback callback) {
            start(config, httpManager, callback, null);
        }

        /**
         * start download
         *
         * @param config       {@link UpdateConfig}
         * @param httpManager  {@link IHttpManager}
         * @param callback     {@link UpdateCallback}
         * @param notification {@link INotification}
         */
        public void start(@NonNull UpdateConfig config, @Nullable IHttpManager httpManager, @Nullable UpdateCallback callback, @Nullable INotification notification) {
            startDownload(config, httpManager, callback, notification);
        }
    }

}