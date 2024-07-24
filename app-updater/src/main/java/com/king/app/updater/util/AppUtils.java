package com.king.app.updater.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.king.app.updater.constant.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.pm.PackageInfoCompat;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public final class AppUtils {

    /**
     * Hexadecimal characters
     */
    private static char hexChars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private AppUtils() {
        throw new AssertionError();
    }

    /**
     * Get the full name of the App through the URL
     *
     * @param context context
     * @return Returns the name of the App; (e.g.: AppName.apk)
     */
    public static String getAppFullName(Context context, String url, String defaultName) {
        if (url.endsWith(".apk")) {
            String apkName = url.substring(url.lastIndexOf("/") + 1);
            if (apkName.length() <= 64) {
                return apkName;
            }
        }

        String filename = getAppName(context);
        LogUtils.d("AppName: " + filename);
        if (TextUtils.isEmpty(filename)) {
            filename = defaultName;
        }
        if (filename.endsWith(".apk")) {
            return filename;
        }
        return String.format("%s.apk", filename);
    }

    /**
     * Get package information
     *
     * @param context context
     * @return {@link PackageInfo}
     * @throws PackageManager.NameNotFoundException
     */
    public static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packageInfo;
    }

    /**
     * Get package information through APK path
     *
     * @param context         context
     * @param archiveFilePath file path
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String archiveFilePath) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        return packageInfo;
    }

    /**
     * Get the name of the App
     *
     * @param context context
     */
    public static String getAppName(Context context) {
        try {
            int labelRes = getPackageInfo(context).applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the App icon
     *
     * @param context context
     * @return
     */
    public static int getAppIcon(Context context) {
        try {
            return getPackageInfo(context).applicationInfo.icon;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Install APK
     *
     * @param context   context
     * @param file      APK file
     * @param authority file access authorization
     */
    public static void installApk(Context context, File file, String authority) {
        Intent intent = getInstallIntent(context, file, authority);
        context.startActivity(intent);
    }

    /**
     * Get the installation intent
     *
     * @param context   context
     * @param file      APK file
     * @param authority file access authorization
     * @return
     */
    public static Intent getInstallIntent(Context context, File file, String authority) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uriData;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriData = FileProvider.getUriForFile(context, authority, file);
        } else {
            uriData = Uri.fromFile(file);
        }
        intent.setDataAndType(uriData, type);
        return intent;
    }

    /**
     * Does the APK exist?
     *
     * @param context     context
     * @param versionCode version number
     * @param file        APK file
     * @return
     * @throws Exception
     */
    public static boolean apkExists(Context context, long versionCode, File file) {
        if (file != null && file.exists()) {
            String packageName = context.getPackageName();
            PackageInfo packageInfo = AppUtils.getPackageInfo(context, file.getAbsolutePath());

            if (packageInfo != null) {
                // Compare versionCode
                long apkVersionCode = PackageInfoCompat.getLongVersionCode(packageInfo);
                LogUtils.d(String.format(Locale.getDefault(), "ApkVersionCode: %d", apkVersionCode));
                if (versionCode == apkVersionCode) {
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    if (applicationInfo != null && packageName.equals(applicationInfo.packageName)) {//Compare packageName
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if the file exists
     *
     * @param context context
     * @param path    file path
     * @return
     */
    public static boolean isAndroidQFileExists(Context context, String path) {
        return isAndroidQFileExists(context, new File(path));
    }

    /**
     * Check if the file exists
     *
     * @param context context
     * @param file    file
     * @return
     */
    public static boolean isAndroidQFileExists(Context context, File file) {
        AssetFileDescriptor descriptor = null;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            Uri uri = Uri.fromFile(file);
            descriptor = contentResolver.openAssetFileDescriptor(uri, "r");
            if (descriptor == null) {
                return false;
            } else {
                close(descriptor);
            }
            return true;
        } catch (FileNotFoundException e) {
            LogUtils.w(e.getMessage());
        } finally {
            close(descriptor);
        }
        return false;
    }

    /**
     * Verify the MD5 of the file
     *
     * @param file file
     * @param md5  MD5
     * @return If the MD5 of the file matches the MD5 string passed in, it returns true, otherwise it returns false
     */
    public static boolean verifyFileMD5(File file, String md5) {
        String fileMD5 = getFileMD5(file);
        LogUtils.d("FileMD5: " + fileMD5);
        if (!TextUtils.isEmpty(md5)) {
            return md5.equalsIgnoreCase(fileMD5);
        }

        return false;
    }

    /**
     * Get the MD5 of the file
     *
     * @param file file
     * @return Returns the MD5 of the file
     */
    public static String getFileMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, length);
            }
            return byteArrayToHexString(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Convert bytes to hexadecimal string
     *
     * @param bytes byte array
     * @return Returns a hexadecimal string
     */
    public static String byteArrayToHexString(byte bytes[]) {
        String hexString = null;
        if (bytes != null) {
            int length = bytes.length;
            StringBuilder out = new StringBuilder(length * 2);
            for (int x = 0; x < length; x++) {
                int nybble = bytes[x] & 0xF0;
                nybble = nybble >>> 4;
                out.append(hexChars[nybble]);
                out.append(hexChars[bytes[x] & 0x0F]);
            }
            hexString = out.toString();
        }
        return hexString;
    }

    /**
     * Get file access authorization
     *
     * @param context context
     * @return Returns file access authorization
     */
    public static String getFileProviderAuthority(Context context) {
        return context.getPackageName() + Constants.DEFAULT_FILE_PROVIDER;
    }

    /**
     * closure
     *
     * @param descriptor {@link AssetFileDescriptor}
     */
    private static void close(AssetFileDescriptor descriptor) {
        if (descriptor != null) {
            try {
                descriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Delete files or folders
     *
     * @param file
     */
    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        } else if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    f.delete(); // Delete all files
                } else if (f.isDirectory()) {
                    deleteFile(f); // Delete folders recursively
                }
            }
            // Delete the directory itself
            return file.delete();
        }
        return false;
    }

    /**
     * Get the APK cache folder
     *
     * @param context
     * @return
     */
    public static String getApkCacheFilesDir(Context context) {
        File[] files = ContextCompat.getExternalFilesDirs(context, Constants.DEFAULT_DIR);
        if (files != null && files.length > 0) {
            return files[0].getAbsolutePath();
        }
        return new File(context.getFilesDir(), Constants.DEFAULT_DIR).getAbsolutePath();
    }
}