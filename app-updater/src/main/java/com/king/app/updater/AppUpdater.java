package com.king.app.updater;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

import com.king.app.updater.callback.UpdateCallback;
import com.king.app.updater.constant.Constants;
import com.king.app.updater.http.HttpManager;
import com.king.app.updater.http.IHttpManager;
import com.king.app.updater.http.OkHttpManager;
import com.king.app.updater.notify.INotification;
import com.king.app.updater.notify.NotificationImpl;
import com.king.app.updater.service.DownloadService;
import com.king.app.updater.util.LogUtils;
import com.king.app.updater.util.PermissionUtils;

import java.util.Map;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * AppUpdater: A lightweight open source library that focuses on App updates and integrates App version upgrades in a one-click fool-proof manner
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppUpdater {
    /**
     * {@link #mContext} is not required to be {@link Activity}, but it is recommended to pass {@link Activity} whenever possible. AppUpdater should only focus on App updates and try not to involve dynamic permission-related processing. If mContext passes {@link Activity}, dynamic permissions will be checked once by default.
     */
    private Context mContext;
    /**
     * AppUpdater configuration information
     */
    private UpdateConfig mConfig;
    /**
     * Update callback
     */
    private UpdateCallback mCallback;
    /**
     * http management interface, which can be implemented by your own custom interface. For example, using okHttp
     */
    private IHttpManager mHttpManager;
    /**
     * Notification bar: If the current notification bar layout does not meet your needs, you can refer to {@link NotificationImpl} to customize a {@link INotification}
     */
    private INotification mNotification;

    /**
     * ServiceConnection
     */
    private ServiceConnection mServiceConnection;

    /**
     * Construction
     *
     * @param context {@link Context}
     * @param config  {@link UpdateConfig}
     */
    public AppUpdater(@NonNull Context context, @NonNull UpdateConfig config) {
        this.mContext = context;
        this.mConfig = config;
    }

    /**
     * Construction
     *
     * @param context {@link Context}
     * @param url     download address
     */
    public AppUpdater(@NonNull Context context, @NonNull String url) {
        this.mContext = context;
        mConfig = new UpdateConfig();
        mConfig.setUrl(url);
    }

    /**
     * Set download update progress callback
     *
     * @param callback update callback
     * @return
     */
    public AppUpdater setUpdateCallback(@Nullable UpdateCallback callback) {
        this.mCallback = callback;
        return this;
    }

    /**
     * Set up a {@link IHttpManager}
     *
     * @param httpManager AppUpdater provides two built-in implementations: {@link HttpManager} and {@link OkHttpManager}.
     *                    If not set, {@link HttpManager} will be used by default. You can also use {@link OkHttpManager} or implement one yourself
     *                    {@link IHttpManager}.
     *                    When using {@link OkHttpManager}, you must rely on the okhttp library
     * @return
     */
    public AppUpdater setHttpManager(@Nullable IHttpManager httpManager) {
        this.mHttpManager = httpManager;
        return this;
    }

    /**
     * Set a {@link INotification}
     *
     * @param notification If the current notification bar layout does not meet your needs, you can refer to {@link NotificationImpl} to customize a {@link INotification}
     * @return
     */
    public AppUpdater setNotification(@Nullable INotification notification) {
        this.mNotification = notification;
        return this;
    }

    /**
     * start download
     */
    public void start() {
        if (mConfig != null && !TextUtils.isEmpty(mConfig.getUrl())) {
            // If mContext is an Activity and a download path is configured, dynamic permissions will be checked once by default.
            if (mContext instanceof Activity && !TextUtils.isEmpty(mConfig.getPath())) {
                PermissionUtils.verifyReadAndWritePermissions((Activity) mContext, Constants.RE_CODE_STORAGE_PERMISSION);
            }

            if (mConfig.isShowNotification() && !PermissionUtils.isNotificationEnabled(mContext)) {
                LogUtils.w("Notification permission is not enabled.");
            }
            // Start the download service
            startDownloadService();
        } else {
            throw new IllegalArgumentException("Url must not be empty.");
        }
    }

    /**
     * Start download service
     */
    private void startDownloadService() {
        Intent intent = new Intent(mContext, DownloadService.class);
        if (mCallback != null || mHttpManager != null || mNotification != null) {
            // Start the download service through bindService
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    DownloadService.DownloadBinder binder = ((DownloadService.DownloadBinder) service);
                    if (mNotification != null) {
                        binder.start(mConfig, mHttpManager, mCallback, mNotification);
                    } else {
                        binder.start(mConfig, mHttpManager, mCallback);
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

            mContext.getApplicationContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        } else {
            // Start the download service by starting Service
            intent.putExtra(Constants.KEY_UPDATE_CONFIG, mConfig);
            mContext.startService(intent);
        }
    }

    /**
     * Cancel download
     */
    public void stop() {
        stopDownloadService();
    }

    /**
     * Stop download service
     */
    private void stopDownloadService() {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_STOP_DOWNLOAD_SERVICE, true);
        mContext.startService(intent);
    }

    /**
     * AppUpdater Builder
     */
    public static class Builder {
        /**
         * {@link Context}
         */
        private Context mContext;
        /**
         * AppUpdater configuration information
         */
        private UpdateConfig mConfig;

        /**
         * Construction
         *
         * @deprecated This method has been marked as deprecated and may be deleted later; please use {@link #Builder(Context)}
         */
        @Deprecated
        public Builder() {
            mConfig = new UpdateConfig();
        }

        /**
         * Construction
         *
         * @param context {@link Context}
         */
        public Builder(@NonNull Context context) {
            setContext(context);
            mConfig = new UpdateConfig();
        }

        /**
         * Set the context
         *
         * @param context {@link Context}
         * @return
         */
        public Builder setContext(@NonNull Context context) {
            mContext = context;
            return this;
        }

        /**
         * Set APK download address
         *
         * @param url download address
         * @return
         */
        public Builder setUrl(@NonNull String url) {
            mConfig.setUrl(url);
            return this;
        }

        /**
         * Set the save path (it is recommended to use the default and not set it)
         *
         * @param path Download and save file path
         * @return
         * @deprecated This method has been deprecated to adapt to Android Q's partition storage and is not recommended.
         */
        @Deprecated
        public Builder setPath(String path) {
            mConfig.setPath(path);
            return this;
        }

        /**
         * Set the saved file name
         *
         * @param filename The downloaded and saved apk file name (the URL file name is preferred by default)
         * @return
         */
        public Builder setFilename(String filename) {
            mConfig.setFilename(filename);
            return this;
        }

        /**
         * Set whether to display the notification bar
         *
         * @param isShowNotification whether to display the notification bar (default true)
         * @return
         */
        public Builder setShowNotification(boolean isShowNotification) {
            mConfig.setShowNotification(isShowNotification);
            return this;
        }

        /**
         * Set notification ID
         *
         * @param notificationId notification ID
         * @return
         */
        public Builder setNotificationId(int notificationId) {
            mConfig.setNotificationId(notificationId);
            return this;
        }

        /**
         * Set the notification channel ID
         *
         * @param channelId Notification channel ID (default compatible with O)
         * @return
         */
        public Builder setChannelId(String channelId) {
            mConfig.setChannelId(channelId);
            return this;
        }

        /**
         * Set the notification channel name
         *
         * @param channelName notification channel name (default compatible with O)
         * @return
         */
        public Builder setChannelName(String channelName) {
            mConfig.setChannelName(channelName);
            return this;
        }

        /**
         * Set notification icon
         *
         * @param icon Notification bar icon (App icon by default)
         * @return
         */
        public Builder setNotificationIcon(@DrawableRes int icon) {
            mConfig.setNotificationIcon(icon);
            return this;
        }

        /**
         * Set whether the notification should vibrate
         *
         * @param vibrate indicates whether to vibrate the notification. When true, the default vibration is used. This setting is only valid for Android O (8.0) and above. It is only valid when the channel is first created. Subsequent modification of the attribute is invalid. To make it valid again, you need to modify the channelId or uninstall the App and reinstall it.
         * @return
         */
        public Builder setVibrate(boolean vibrate) {
            mConfig.setVibrate(vibrate);
            return this;
        }

        /**
         * Set whether notification ringtone should be used
         *
         * @param sound indicates whether to use ringtone. If true, the default ringtone is used. This setting is only valid for Android O (8.0) and above. It is only valid when the channel is first created. Subsequent modification of the attribute will be invalid. To make it valid again, you need to modify the channelId or uninstall the App and reinstall it.
         * @return
         */
        public Builder setSound(boolean sound) {
            mConfig.setSound(sound);
            return this;
        }


        /**
         * Set whether to automatically trigger the installation of APK after downloading
         *
         * @param isInstallApk Whether to automatically call the installation APK after downloading is complete (default true)
         * @return
         */
        public Builder setInstallApk(boolean isInstallApk) {
            mConfig.setInstallApk(isInstallApk);
            return this;
        }

        /**
         * Set the FileProvider authority
         *
         * @param authority FileProvider authority (default compatible with N, default value {@link Context#getPackageName() + ".AppUpdaterFileProvider"})
         * @return
         */
        public Builder setAuthority(String authority) {
            mConfig.setAuthority(authority);
            return this;
        }

        /**
         * Set whether to display the download percentage in the notification bar when downloading
         *
         * @param showPercentage Whether the notification bar displays the percentage when downloading
         * @return
         */
        public Builder setShowPercentage(boolean showPercentage) {
            mConfig.setShowPercentage(showPercentage);
            return this;
        }

        /**
         * Set whether to support re-downloading by clicking the notification bar when downloading fails. The associated method is {@link #setReDownloads(int)}
         *
         * @param reDownload Whether to support clicking the notification bar to re-download when download fails, the default is true
         * @return
         */
        public Builder setReDownload(boolean reDownload) {
            mConfig.setReDownload(reDownload);
            return this;
        }

        /**
         * Set the maximum number of re-downloads when download fails. The associated method is {@link #setReDownload(boolean)}
         *
         * @param reDownloads Whether to support clicking the notification bar to re-download when download fails. The default is to re-download up to 3 times
         * @return
         */
        public Builder setReDownloads(int reDownloads) {
            mConfig.setReDownloads(reDownloads);
            return this;
        }

        /**
         * Set the versionCode of the APK to be downloaded. This is used to verify whether the APK files are consistent when the cache is first retrieved.
         * Cache verification currently supports two methods: one is through versionCode verification, that is, {@link #setVersionCode(long)}; the other is file MD5 verification, that is, {@link #setApkMD5(String)}. It is recommended to use the MD5 verification method
         * If both methods are set, only MD5 is verified
         *
         * @param versionCode is null, which means no processing. If it does not exist, it will be downloaded by default. If it exists, it will be re-downloaded. If it is not null, it means that the local APK with the downloaded version number versionCode will be checked first.
         *                    If it exists, it will not be downloaded again (AppUpdater will automatically check the consistency of packageName). It will directly get the local APK. Otherwise, it will be downloaded again.
         * @return
         */
        public Builder setVersionCode(long versionCode) {
            mConfig.setVersionCode(versionCode);
            return this;
        }

        /**
         * Set the MD5 of the APK file to verify whether the file APK is consistent through MD5 when taking the cache first.
         * Cache verification currently supports two methods: one is through versionCode verification, that is, {@link #setVersionCode(long)}; the other is file MD5 verification, that is, {@link #setApkMD5(String)}. It is recommended to use the MD5 verification method
         * If both methods are set, only MD5 is verified
         *
         * @param md5 is null, which means no processing. If MD5 is set, if the MD5 of the cached APK is the same, it will only be downloaded once, and the local cache will be given priority.
         * @return
         */
        public Builder setApkMD5(String md5) {
            mConfig.setApkMD5(md5);
            return this;
        }

        /**
         * Add parameters to the request header
         *
         * @param key
         * @param value
         * @return
         */
        public Builder addHeader(String key, String value) {
            mConfig.addHeader(key, value);
            return this;
        }

        /**
         * Add parameters to the request header
         *
         * @param headers
         * @return
         */
        public Builder addHeader(Map<String, String> headers) {
            mConfig.addHeader(headers);
            return this;
        }

        /**
         * Set whether to automatically delete the files that were canceled from downloading
         *
         * @param deleteCancelFile whether to delete the file that was canceled (default is true)
         */
        public Builder setDeleteCancelFile(boolean deleteCancelFile) {
            mConfig.setDeleteCancelFile(deleteCancelFile);
            return this;
        }

        /**
         * Whether to support canceling downloads by removing the notification bar (default: false)
         *
         * @param cancelDownload
         * @return
         * @deprecated This method has been marked as deprecated and may be deleted later; please use {@link #setSupportCancelDownload(boolean)}
         */
        @Deprecated
        public Builder setCancelDownload(boolean cancelDownload) {
            return setSupportCancelDownload(cancelDownload);
        }

        /**
         * Whether to support canceling downloads by removing the notification bar (default: false)
         *
         * @param supportCancelDownload
         * @return
         */
        public Builder setSupportCancelDownload(boolean supportCancelDownload) {
            mConfig.setSupportCancelDownload(supportCancelDownload);
            return this;
        }

        /**
         * Build AppUpdater
         *
         * @return {@link AppUpdater}
         */
        public AppUpdater build() {
            if (mContext == null) {
                throw new NullPointerException("Context must not be null.");
            }
            return new AppUpdater(mContext, mConfig);
        }

        /**
         * Build AppUpdater
         *
         * @param context
         * @return {@link AppUpdater}
         * @deprecated This method has been marked as deprecated and may be deleted later; please use {@link #build()}
         */
        @Deprecated
        public AppUpdater build(@NonNull Context context) {
            return new AppUpdater(context, mConfig);
        }

    }

}