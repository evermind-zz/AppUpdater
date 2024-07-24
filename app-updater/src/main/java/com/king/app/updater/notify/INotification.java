package com.king.app.updater.notify;

import android.content.Context;

import com.king.app.updater.UpdateConfig;

import java.io.File;

import androidx.annotation.DrawableRes;

/**
 * Notification bar progress update
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface INotification {
    /**
     * start
     *
     * @param context                 context
     * @param notifyId                Notification ID
     * @param channelId               notification channel ID
     * @param channelName             notification channel name
     * @param smallIcon               notification icon
     * @param title Notification      title
     * @param content Notification    content
     * @param isVibrate Notification  whether vibration is allowed
     * @param isSound                 Whether the notification has a ringtone
     * @param isSupportCancelDownload whether to support canceling download
     */
    void onStart(Context context, int notifyId, String channelId, String channelName, @DrawableRes int smallIcon, CharSequence title, CharSequence content, boolean isVibrate, boolean isSound, boolean isSupportCancelDownload);

    /**
     * Update progress
     *
     * @param context                 context
     * @param notifyId                Notification ID
     * @param channelId               notification channel ID
     * @param smallIcon               notification icon
     * @param title                   Notification title
     * @param content                 Notification content
     * @param progress                Current progress size
     * @param size                    total progress size
     * @param isSupportCancelDownload whether to support canceling download
     */
    void onProgress(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, int progress, int size, boolean isSupportCancelDownload);

    /**
     * Finish
     *
     * @param context   context
     * @param notifyId  Notification ID
     * @param channelId notification channel ID
     * @param smallIcon notification icon
     * @param title     Notification title
     * @param content   Notification content
     * @param file      APK file
     * @param authority file access authorization
     */
    void onFinish(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, File file, String authority);

    /**
     * mistake
     *
     * @param context      context
     * @param notifyId     Notification ID
     * @param channelId    notification channel ID
     * @param smallIcon    notification icon
     * @param title        Notification title
     * @param content      Notification content
     * @param isReDownload whether to download repeatedly
     * @param config       configuration
     */
    void onError(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, boolean isReDownload, UpdateConfig config);

    /**
     * Cancel
     *
     * @param context  context
     * @param notifyId Notification ID
     */
    void onCancel(Context context, int notifyId);
}
