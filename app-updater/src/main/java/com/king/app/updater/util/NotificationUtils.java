package com.king.app.updater.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.king.app.updater.UpdateConfig;
import com.king.app.updater.constant.Constants;
import com.king.app.updater.service.DownloadService;

import java.io.File;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * Notification bar tools
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class NotificationUtils {

    private NotificationUtils() {
        throw new AssertionError();
    }

    /**
     * Display notification when download starts
     *
     * @param notifyId
     * @param channelId
     * @param channelName
     * @param smallIcon
     * @param title
     * @param content
     */
    public static void showStartNotification(Context context, int notifyId, String channelId, String channelName, @DrawableRes int smallIcon, CharSequence title, CharSequence content, boolean isVibrate, boolean isSound, boolean isSupportCancelDownload) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, channelId, channelName, isVibrate, isSound);
        }
        NotificationCompat.Builder builder = buildNotification(context, channelId, smallIcon, title, content);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (isVibrate && isSound) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        } else if (isVibrate) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        } else if (isSound) {
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }
        // If download cancellation is supported, click the notification bar to cancel the download
        if (isSupportCancelDownload) {
            Intent intent = new Intent(context, DownloadService.class);
            intent.putExtra(Constants.KEY_STOP_DOWNLOAD_SERVICE, true);
            PendingIntent deleteIntent = PendingIntent.getService(context, notifyId, intent, getPendingIntentFlags(PendingIntent.FLAG_CANCEL_CURRENT));
            builder.setDeleteIntent(deleteIntent);
        }

        Notification notification = builder.build();
        if (isSupportCancelDownload) {
            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        } else {
            notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONLY_ALERT_ONCE;
        }

        notifyNotification(context, notifyId, notification);
    }

    /**
     * Display downloading notifications (update progress)
     *
     * @param notifyId
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     * @param progress
     * @param size
     */
    public static void showProgressNotification(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, int progress, int size, boolean isSupportCancelDownload) {
        NotificationCompat.Builder builder = buildNotification(context, channelId, smallIcon, title, content, progress, size);
        // If download cancellation is supported, click the notification bar to cancel the download
        if (isSupportCancelDownload) {
            Intent intent = new Intent(context, DownloadService.class);
            intent.putExtra(Constants.KEY_STOP_DOWNLOAD_SERVICE, true);
            PendingIntent deleteIntent = PendingIntent.getService(context, notifyId, intent, getPendingIntentFlags(PendingIntent.FLAG_CANCEL_CURRENT));
            builder.setDeleteIntent(deleteIntent);
        }

        Notification notification = builder.build();

        if (isSupportCancelDownload) {
            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        } else {
            notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONLY_ALERT_ONCE;
        }

        notifyNotification(context, notifyId, notification);
    }

    /**
     * Show notification when download is complete (click to install)
     *
     * @param notifyId
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     * @param file
     */
    public static void showFinishNotification(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, File file, String authority) {
        cancelNotification(context, notifyId);
        NotificationCompat.Builder builder = buildNotification(context, channelId, smallIcon, title, content);
        builder.setAutoCancel(true);
        Intent intent = AppUtils.getInstallIntent(context, file, authority);
        PendingIntent clickIntent = PendingIntent.getActivity(context, notifyId, intent, getPendingIntentFlags(PendingIntent.FLAG_UPDATE_CURRENT));
        builder.setContentIntent(clickIntent);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notifyNotification(context, notifyId, notification);
    }

    /**
     * Download failure notification now
     *
     * @param context
     * @param notifyId
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     * @param isReDownload
     * @param config
     */
    public static void showErrorNotification(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, boolean isReDownload, UpdateConfig config) {
        NotificationCompat.Builder builder = buildNotification(context, channelId, smallIcon, title, content);
        builder.setAutoCancel(true);
        int flag = getPendingIntentFlags(PendingIntent.FLAG_UPDATE_CURRENT);
        // When you click the notification bar, re-download
        if (isReDownload) {
            Intent intent = new Intent(context, DownloadService.class);
            intent.putExtra(Constants.KEY_RE_DOWNLOAD, true);
            intent.putExtra(Constants.KEY_UPDATE_CONFIG, config);
            PendingIntent clickIntent = PendingIntent.getService(context, notifyId, intent, flag);
            builder.setContentIntent(clickIntent);
        } else {
            // When you click the notification bar, the notification bar will be automatically canceled
            PendingIntent clickIntent = PendingIntent.getService(context, notifyId, new Intent(), flag);
            builder.setContentIntent(clickIntent);
        }

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notifyNotification(context, notifyId, notification);
    }


    /**
     * Display notification information (not the first time)
     *
     * @param notifyId
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     */
    public static void showNotification(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, boolean isAutoCancel) {
        NotificationCompat.Builder builder = buildNotification(context, channelId, smallIcon, title, content);
        builder.setAutoCancel(isAutoCancel);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notifyNotification(context, notifyId, notification);
    }

    /**
     * Cancellation Notice
     *
     * @param notifyId
     */
    public static void cancelNotification(Context context, int notifyId) {
        getNotificationManager(context).cancel(notifyId);
    }


    /**
     * Get the notification manager
     *
     * @return {@link NotificationManagerCompat}
     */
    public static NotificationManagerCompat getNotificationManager(Context context) {
        return NotificationManagerCompat.from(context);
    }

    /**
     * Create a notification channel (compatible with version 0 and above)
     *
     * @param channelId
     * @param channelName
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context context, String channelId, String channelName, boolean isVibrate, boolean isSound) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);
        channel.enableVibration(isVibrate);
        if (!isSound) {
            channel.setSound(null, null);
        }
        getNotificationManager(context).createNotificationChannel(channel);

    }

    /**
     * Construct a notification builder
     *
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     * @return
     */
    private static NotificationCompat.Builder buildNotification(Context context, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content) {
        return buildNotification(context, channelId, smallIcon, title, content, Constants.NONE, Constants.NONE);
    }

    /**
     * Construct a notification builder
     *
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     * @param progress
     * @param size
     * @return
     */
    private static NotificationCompat.Builder buildNotification(Context context, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, int progress, int size) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(smallIcon);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setOngoing(true);

        if (progress != Constants.NONE) {
            builder.setProgress(size, progress, size <= 0);
        }
        return builder;
    }

    /**
     * Update notification bar
     *
     * @param id
     * @param notification
     */
    private static void notifyNotification(Context context, int id, Notification notification) {
        getNotificationManager(context).notify(id, notification);
    }

    /**
     * Get the flags of the PendingIntent
     *
     * @param flag
     * @return
     */
    private static int getPendingIntentFlags(int flag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return flag | PendingIntent.FLAG_IMMUTABLE;
        }
        return flag;
    }
}
