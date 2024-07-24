package com.king.app.updater.callback;

import java.io.File;

/**
 * Update callback interface
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface UpdateCallback {

    /**
     * Initial call (called before onStart)
     *
     * @param isDownloading When it is true, it means that the download is already started; when it is false, it means that the download has not started yet and will start soon
     */
    void onDownloading(boolean isDownloading);

    /**
     * start
     */
    void onStart(String url);

    /**
     * Update progress
     *
     * @param progress  Current progress size
     * @param total     total file size
     * @param isChanged Whether the progress percentage has changed (mainly used to filter useless refreshes, thereby reducing the refresh frequency)
     */
    void onProgress(long progress, long total, boolean isChanged);

    /**
     * Finish
     *
     * @param file APK file
     */
    void onFinish(File file);

    /**
     * mistake
     *
     * @param e exception
     */
    void onError(Exception e);

    /**
     * Cancel
     */
    void onCancel();
}
