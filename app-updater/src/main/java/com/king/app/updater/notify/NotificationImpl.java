package com.king.app.updater.notify;

import android.content.Context;

import com.king.app.updater.UpdateConfig;
import com.king.app.updater.util.NotificationUtils;

import java.io.File;

/**
 * Implementation of {@link INotification}. If you need to customize the layout of the notification bar and are not satisfied with the implementation of {@link NotificationImpl}, you can implement a {@link INotification} by customizing it.
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class NotificationImpl implements INotification {

    @Override
    public void onStart(Context context, int notifyId, String channelId, String channelName, int smallIcon, CharSequence title, CharSequence content, boolean isVibrate, boolean isSound, boolean isSupportCancelDownload) {
        NotificationUtils.showStartNotification(context, notifyId, channelId, channelName, smallIcon, title, content, isVibrate, isSound, isSupportCancelDownload);
    }

    @Override
    public void onProgress(Context context, int notifyId, String channelId, int smallIcon, CharSequence title, CharSequence content, int progress, int size, boolean isSupportCancelDownload) {
        NotificationUtils.showProgressNotification(context, notifyId, channelId, smallIcon, title, content, progress, size, isSupportCancelDownload);
    }

    @Override
    public void onFinish(Context context, int notifyId, String channelId, int smallIcon, CharSequence title, CharSequence content, File file, String authority) {
        NotificationUtils.showFinishNotification(context, notifyId, channelId, smallIcon, title, content, file, authority);
    }

    @Override
    public void onError(Context context, int notifyId, String channelId, int smallIcon, CharSequence title, CharSequence content, boolean isReDownload, UpdateConfig config) {
        NotificationUtils.showErrorNotification(context, notifyId, channelId, smallIcon, title, content, isReDownload, config);
    }

    @Override
    public void onCancel(Context context, int notifyId) {
        NotificationUtils.cancelNotification(context, notifyId);
    }
}
