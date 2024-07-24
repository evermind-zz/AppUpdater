package com.king.app.updater;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.king.app.updater.constant.Constants;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.DrawableRes;

/**
 * AppUpdater configuration information
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class UpdateConfig implements Parcelable {

    /**
     * APK download URL
     */
    private String mUrl;
    /**
     * save route
     */
    private String mPath;
    /**
     * Save file name
     */
    private String mFilename;

    /**
     * Whether to display the notification bar
     */
    private boolean isShowNotification = true;
    /**
     * Whether to automatically pop up the installation after downloading is complete
     */
    private boolean isInstallApk = true;
    /**
     * Notification bar icon: default app icon
     */
    private int mNotificationIcon;

    /**
     * Notification bar ID
     */
    private int mNotificationId = Constants.DEFAULT_NOTIFICATION_ID;

    /**
     * Notification channel ID
     */
    private String mChannelId;
    /**
     * Notification channel name
     */
    private String mChannelName;
    /**
     * Default {@link Context#getPackageName() + ".AppUpdaterFileProvider"}
     */
    private String mAuthority;
    /**
     * If the download fails, can you click the notification bar to re-download?
     */
    private boolean isReDownload = true;
    /**
     * Maximum number of re-downloads after a download fails
     */
    private int reDownloads = 3;
    /**
     * Whether to display percentage
     */
    private boolean isShowPercentage = true;

    /**
     * Whether to vibrate the notification. If true, use the default vibration of the notification.
     */
    private boolean isVibrate;

    /**
     * Whether to ring a reminder, if true, use the default notification ringtone
     */
    private boolean isSound;

    /**
     * The versionCode of the APK to download
     */
    private long versionCode = Constants.NONE;

    /**
     * Request header parameters
     */
    private Map<String, String> mRequestProperty;

    /**
     * Whether to delete the files that were canceled from downloading
     */
    private boolean isDeleteCancelFile = true;

    /**
     * Whether to support canceling download by deleting the notification bar
     */
    private boolean isSupportCancelDownload = false;

    /**
     * MD5 of APK file
     */
    private String apkMD5;

    public UpdateConfig() {

    }

    public String getUrl() {
        return mUrl;
    }

    /**
     * Set APK download address
     *
     * @param url download address
     */
    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getPath() {
        return mPath;
    }

    /**
     * Set the save path (it is recommended to use the default and not set it)
     *
     * @param path Download and save file path
     * @return
     * @deprecated This method has been deprecated to adapt to Android Q's partition storage and is not recommended.
     */
    @Deprecated
    public void setPath(String path) {
        this.mPath = path;
    }

    public String getFilename() {
        return mFilename;
    }

    /**
     * Set the saved file name
     *
     * @param filename The downloaded and saved apk file name (the URL file name is preferred by default)
     */
    public void setFilename(String filename) {
        this.mFilename = filename;
    }

    public boolean isShowNotification() {
        return isShowNotification;
    }

    /**
     * Set whether to display the notification bar
     *
     * @param isShowNotification whether to display the notification bar (default true)
     */
    public void setShowNotification(boolean isShowNotification) {
        this.isShowNotification = isShowNotification;
    }

    public String getChannelId() {
        return mChannelId;
    }

    /**
     * Set the notification channel ID
     *
     * @param channelId Notification channel ID (default compatible with O)
     */
    public void setChannelId(String channelId) {
        this.mChannelId = channelId;
    }

    public String getChannelName() {
        return mChannelName;
    }

    /**
     * Set the notification channel name
     *
     * @param channelName notification channel name (default compatible with O)
     */
    public void setChannelName(String channelName) {
        this.mChannelName = channelName;
    }

    /**
     * Set notification ID
     *
     * @param notificationId notification ID
     */
    public void setNotificationId(int notificationId) {
        this.mNotificationId = notificationId;
    }

    public int getNotificationId() {
        return this.mNotificationId;
    }


    /**
     * Set notification icon
     *
     * @param icon The default notification bar icon is the App icon)
     */
    public void setNotificationIcon(@DrawableRes int icon) {
        this.mNotificationIcon = icon;
    }

    public int getNotificationIcon() {
        return this.mNotificationIcon;
    }

    public boolean isInstallApk() {
        return isInstallApk;
    }

    /**
     * Set whether to automatically trigger the installation of APK after downloading
     *
     * @param isInstallApk Whether to automatically call the installation APK after downloading is complete (default true)
     */
    public void setInstallApk(boolean isInstallApk) {
        this.isInstallApk = isInstallApk;
    }

    public String getAuthority() {
        return mAuthority;
    }

    /**
     * Set the FileProvider authority
     *
     * @param authority FileProvider authority (default compatible with N, default value {@link Context#getPackageName() + ".AppUpdaterFileProvider"})
     */
    public void setAuthority(String authority) {
        this.mAuthority = authority;
    }

    public boolean isShowPercentage() {
        return isShowPercentage;
    }

    /**
     * Set whether to display the download percentage in the notification bar when downloading
     *
     * @param showPercentage Whether the notification bar displays the percentage when downloading
     */
    public void setShowPercentage(boolean showPercentage) {
        isShowPercentage = showPercentage;
    }

    public boolean isReDownload() {
        return isReDownload;
    }

    /**
     * Set whether to support re-downloading by clicking the notification bar when downloading fails. The associated method is {@link #setReDownloads(int)}
     *
     * @param reDownload Whether to support clicking the notification bar to re-download when download fails, the default is true
     */
    public void setReDownload(boolean reDownload) {
        isReDownload = reDownload;
    }

    public int getReDownloads() {
        return reDownloads;
    }

    /**
     * Set the maximum number of re-downloads when download fails. The associated method is {@link #setReDownload(boolean)}
     *
     * @param reDownloads Whether to support clicking the notification bar to re-download when download fails. The default is to re-download up to 3 times
     */
    public void setReDownloads(int reDownloads) {
        this.reDownloads = reDownloads;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    /**
     * Set whether the notification should vibrate
     *
     * @param vibrate indicates whether to vibrate the notification. When true, the default vibration is used. This setting is only valid for Android O (8.0) and above. It is only valid when the channel is first created. Subsequent modification of the attribute is invalid. To make it valid again, you need to modify the channelId or uninstall the App and reinstall it.
     */
    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }

    public boolean isSound() {
        return isSound;
    }

    /**
     * Set whether notification ringtone should be used
     *
     * @param sound indicates whether to use ringtone. If true, the default ringtone is used. This setting is only valid for Android O (8.0) and above. It is only valid when the channel is first created. Subsequent modification of the attribute will be invalid. To make it valid again, you need to modify the channelId or uninstall the App and reinstall it.
     */
    public void setSound(boolean sound) {
        isSound = sound;
    }

    public long getVersionCode() {
        return versionCode;
    }

    /**
     * Set the versionCode of the APK to be downloaded. This is used to verify whether the APK files are consistent when the cache is first retrieved.
     * Cache verification currently supports two methods: one is through versionCode verification, that is, {@link #setVersionCode(long)}; the other is file MD5 verification, that is, {@link #setApkMD5(String)}. It is recommended to use the MD5 verification method
     * If both methods are set, only MD5 is verified
     *
     * @param versionCode is null, which means no processing. If it does not exist, it will be downloaded by default. If it exists, it will be re-downloaded. If it is not null, it means that the local APK with the downloaded version number versionCode will be checked first.
     *                    If it exists, it will not be downloaded again (AppUpdater will automatically check the consistency of packageName). It will directly get the local APK. Otherwise, it will be downloaded again.
     */
    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }

    public Map<String, String> getRequestProperty() {
        return mRequestProperty;
    }

    /**
     * Set the MD5 of the APK file to verify whether the file APK is consistent through MD5 when taking the cache first.
     * Cache verification currently supports two methods: one is through versionCode verification, that is, {@link #setVersionCode(long)}; the other is file MD5 verification, that is, {@link #setApkMD5(String)}. It is recommended to use the MD5 verification method
     * If both methods are set, only MD5 is verified
     *
     * @param md5 is null, which means no processing. If MD5 is set, if the MD5 of the cached APK is the same, it will only be downloaded once, and the local cache will be given priority.
     */
    public void setApkMD5(String md5) {
        this.apkMD5 = md5;
    }

    public String getApkMD5() {
        return apkMD5;
    }

    /**
     * Add parameters to the request header
     *
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        initRequestProperty();
        mRequestProperty.put(key, value);
    }

    /**
     * Add parameters to the request header
     *
     * @param headers
     */
    public void addHeader(Map<String, String> headers) {
        initRequestProperty();
        mRequestProperty.putAll(headers);
    }

    private void initRequestProperty() {
        if (mRequestProperty == null) {
            mRequestProperty = new HashMap<>();
        }
    }

    public boolean isDeleteCancelFile() {
        return isDeleteCancelFile;
    }

    /**
     * Set whether to automatically delete the files that were canceled from downloading
     *
     * @param deleteCancelFile whether to delete the file that was canceled (default: true)
     */
    public void setDeleteCancelFile(boolean deleteCancelFile) {
        isDeleteCancelFile = deleteCancelFile;
    }


    public boolean isSupportCancelDownload() {
        return isSupportCancelDownload;
    }

    /**
     * Whether to support canceling downloads by removing the notification bar (default: false)
     *
     * @param cancelDownload
     * @return
     * @deprecated This method has been marked as deprecated and may be deleted later; please use {@link #setSupportCancelDownload(boolean)}
     */
    @Deprecated
    public void setCancelDownload(boolean cancelDownload) {
        setSupportCancelDownload(cancelDownload);
    }

    /**
     * Whether to support canceling downloads by removing the notification bar (default: false)
     *
     * @param supportCancelDownload
     */
    public void setSupportCancelDownload(boolean supportCancelDownload) {
        isSupportCancelDownload = supportCancelDownload;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUrl);
        dest.writeString(this.mPath);
        dest.writeString(this.mFilename);
        dest.writeByte(this.isShowNotification ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isInstallApk ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mNotificationIcon);
        dest.writeInt(this.mNotificationId);
        dest.writeString(this.mChannelId);
        dest.writeString(this.mChannelName);
        dest.writeString(this.mAuthority);
        dest.writeByte(this.isReDownload ? (byte) 1 : (byte) 0);
        dest.writeInt(this.reDownloads);
        dest.writeByte(this.isShowPercentage ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isVibrate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSound ? (byte) 1 : (byte) 0);
        dest.writeLong(this.versionCode);
        if (mRequestProperty != null) {
            dest.writeInt(this.mRequestProperty.size());
            for (Map.Entry<String, String> entry : this.mRequestProperty.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue());
            }
        } else {
            dest.writeInt(0);
        }

        dest.writeByte(this.isDeleteCancelFile ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSupportCancelDownload ? (byte) 1 : (byte) 0);
        dest.writeString(this.apkMD5);
    }

    protected UpdateConfig(Parcel in) {
        this.mUrl = in.readString();
        this.mPath = in.readString();
        this.mFilename = in.readString();
        this.isShowNotification = in.readByte() != 0;
        this.isInstallApk = in.readByte() != 0;
        this.mNotificationIcon = in.readInt();
        this.mNotificationId = in.readInt();
        this.mChannelId = in.readString();
        this.mChannelName = in.readString();
        this.mAuthority = in.readString();
        this.isReDownload = in.readByte() != 0;
        this.reDownloads = in.readInt();
        this.isShowPercentage = in.readByte() != 0;
        this.isVibrate = in.readByte() != 0;
        this.isSound = in.readByte() != 0;
        this.versionCode = in.readLong();
        int mRequestPropertySize = in.readInt();
        this.mRequestProperty = new HashMap<>(mRequestPropertySize);
        for (int i = 0; i < mRequestPropertySize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.mRequestProperty.put(key, value);
        }
        this.isDeleteCancelFile = in.readByte() != 0;
        this.isSupportCancelDownload = in.readByte() != 0;
        this.apkMD5 = in.readString();
    }

    public static final Creator<UpdateConfig> CREATOR = new Creator<UpdateConfig>() {
        @Override
        public UpdateConfig createFromParcel(Parcel source) {
            return new UpdateConfig(source);
        }

        @Override
        public UpdateConfig[] newArray(int size) {
            return new UpdateConfig[size];
        }
    };
}