package com.king.app.updater.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * Permission Tools
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public final class PermissionUtils {

    private PermissionUtils() {
        throw new AssertionError();
    }

    /**
     * Verify permissions
     *
     * @param activity
     * @param requestCode
     * @return
     */
    public static boolean verifyReadAndWritePermissions(@NonNull Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int readResult = checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writeResult = checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (readResult != PackageManager.PERMISSION_GRANTED || writeResult != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                return false;
            }
        }
        return true;
    }

    /**
     * Check permissions
     *
     * @param context
     * @param permission
     * @return
     */
    public static int checkPermission(@NonNull Context context, @NonNull String permission) {
        return ActivityCompat.checkSelfPermission(context, permission);
    }

    /**
     * Whether the notification bar is enabled
     *
     * @param context
     */
    public static boolean isNotificationEnabled(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

}