package com.king.app.updater.http;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * IHttpManager provides two implementations by default: {@link HttpManager} and {@link OkHttpManager}.
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface IHttpManager {

    /**
     * download
     *
     * @param url
     * @param saveFilePath
     * @param requestProperty
     * @param callback
     */
    void download(String url, String saveFilePath, @Nullable Map<String, String> requestProperty, DownloadCallback callback);

    /**
     * Cancel download
     */
    void cancel();

    interface DownloadCallback extends Serializable {
        /**
         * start
         *
         * @param url
         */
        void onStart(String url);

        /**
         * Loading progress...
         *
         * @param progress
         * @param total
         */
        void onProgress(long progress, long total);

        /**
         * Finish
         *
         * @param file
         */
        void onFinish(File file);

        /**
         * mistake
         *
         * @param e
         */
        void onError(Exception e);

        /**
         * Cancel
         */
        void onCancel();
    }
}