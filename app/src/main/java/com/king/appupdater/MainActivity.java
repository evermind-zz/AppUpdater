package com.king.appupdater;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;
import com.king.app.updater.UpdateConfig;
import com.king.app.updater.callback.AppUpdateCallback;
import com.king.app.updater.callback.UpdateCallback;
import com.king.app.updater.http.OkHttpManager;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class MainActivity extends AppCompatActivity {

    private final Object mLock = new Object();

    // If the download error "Failed to connect to raw.githubusercontent.com" appears, you can try another download link. GitHub's raw.githubusercontent.com is not very stable at present.
    // private String mUrl = "https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk";
    private String mUrl = "https://gitlab.com/jenly1314/AppUpdater/-/raw/master/app/release/app-release.apk";

    private TextView tvProgress;
    private ProgressBar progressBar;

    private Toast toast;

    private AppUpdater mAppUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public Context getContext() {
        return this;
    }

    public void showToast(String text) {
        if (toast == null) {
            synchronized (mLock) {
                if (toast == null) {
                    toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
                }
            }
        }
        toast.setText(text);
        toast.show();
    }

    /**
     * Simple one-click background upgrade
     */
    private void clickBtn1() {
        mAppUpdater = new AppUpdater(getContext(), mUrl);
        mAppUpdater.start();
    }

    /**
     * One-click download and monitoring
     */
    private void clickBtn2() {
        UpdateConfig config = new UpdateConfig();
        config.setUrl(mUrl);
        config.addHeader("token", "xxxxxx");
        mAppUpdater = new AppUpdater(getContext(), config)
                .setHttpManager(OkHttpManager.getInstance())
                .setUpdateCallback(new UpdateCallback() {

                    @Override
                    public void onDownloading(boolean isDownloading) {
                        if (isDownloading) {
                            showToast("Already downloading, please do not download again.");
                        } else {
                            // showToast("Start downloading...");
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_progress, null);
                            tvProgress = view.findViewById(R.id.tvProgress);
                            progressBar = view.findViewById(R.id.progressBar);
                            AppDialog.INSTANCE.showDialog(getContext(), view, false);
                        }
                    }

                    @Override
                    public void onStart(String url) {

                    }

                    @Override
                    public void onProgress(long progress, long total, boolean isChanged) {
                        if (isChanged) {
                            updateProgress(progress, total);
                        }
                    }

                    @Override
                    public void onFinish(File file) {
                        AppDialog.INSTANCE.dismissDialog();
                        showToast("Download completed");
                    }

                    @Override
                    public void onError(Exception e) {
                        AppDialog.INSTANCE.dismissDialog();
                        showToast("Download failed");
                    }

                    @Override
                    public void onCancel() {
                        AppDialog.INSTANCE.dismissDialog();
                        showToast("Cancel download");
                    }
                });
        mAppUpdater.start();
    }

    private void updateProgress(long progress, long total) {
        if (tvProgress == null || progressBar == null) {
            return;
        }
        if (progress > 0) {
            int currProgress = (int) (progress * 1.0f / total * 100.0f);
            tvProgress.setText(getString(R.string.app_updater_progress_notification_content) + currProgress + "%");
            progressBar.setProgress(currProgress);
        } else {
            tvProgress.setText(getString(R.string.app_updater_start_notification_content));
        }

    }

    /**
     * System pop-up window upgrade
     */
    private void clickBtn3() {
        new AlertDialog.Builder(this)
                .setTitle("New version found")
                .setMessage("1. Add a new function, \n2. Fix a problem, \n3. Optimize a bug, ")
                .setPositiveButton("Upgrade", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAppUpdater = new AppUpdater.Builder(getContext())
                                .setUrl(mUrl)
                                .setSupportCancelDownload(true)
                                .build()
                                .setUpdateCallback(new AppUpdateCallback() {
                                    @Override
                                    public void onStart(String url) {
                                        super.onStart(url);
                                        // Imitate the system's built-in banner notification effect
                                        AppDialogConfig config = new AppDialogConfig(getContext(), R.layout.dialog_heads_up);
                                        config.setStyleId(R.style.app_dialog_heads_up)
                                                .setWidthRatio(.95f)
                                                .setGravity(Gravity.TOP);
                                        AppDialog.INSTANCE.showDialog(getContext(), config);
                                        new CountDownTimer(2000, 500) {

                                            @Override
                                            public void onTick(long millisUntilFinished) {

                                            }

                                            @Override
                                            public void onFinish() {
                                                AppDialog.INSTANCE.dismissDialog();
                                            }
                                        }.start();
                                    }

                                    @Override
                                    public void onProgress(long progress, long total, boolean isChanged) {

                                    }

                                    @Override
                                    public void onFinish(File file) {
                                        showToast("Download completed");
                                    }
                                });
                        mAppUpdater.start();
                    }
                }).show();
    }

    /**
     * Simple pop-up window upgrade
     */
    private void clickBtn4() {
        AppDialogConfig config = new AppDialogConfig(getContext());
        config.setTitle("Simple pop-up window upgrade")
                .setConfirm("Upgrade")
                .setContent("1. Add a new function, \n2. Fix a problem, \n3. Optimize a bug, ")
                .setOnClickConfirm(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAppUpdater = new AppUpdater(getContext(), mUrl);
                        mAppUpdater.start();
                        AppDialog.INSTANCE.dismissDialog();
                    }
                });
        AppDialog.INSTANCE.showDialog(config);
    }

    /**
     * Simple custom pop-up window upgrade
     */
    private void clickBtn5() {
        AppDialogConfig config = new AppDialogConfig(getContext(), R.layout.dialog);
        config.setConfirm("upgrade")
                .setHideCancel(true)
                .setTitle("Simple custom pop-up window upgrade")
                .setContent("1. Add a new function, \n2. Fix a problem, \n3. Optimize a bug, ")
                .setOnClickConfirm(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAppUpdater = new AppUpdater.Builder(getContext())
                                .setUrl(mUrl)
                                .build();
                        mAppUpdater.start();
                        AppDialog.INSTANCE.dismissDialog();
                    }
                });
        AppDialog.INSTANCE.showDialog(config);
    }

    /**
     * Customize pop-up window, prioritize cache upgrade
     */
    private void clickBtn6() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_custom, null);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText("Custom pop-up box upgrade");
        TextView tvContent = view.findViewById(R.id.tvContent);
        tvContent.setText("1. Add a new function, \n2. Fix a problem, \n3. Optimize a bug, ");

        View btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialog.INSTANCE.dismissDialog();
            }
        });
        View btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppUpdater = new AppUpdater.Builder(getContext())
                        .setUrl(mUrl)
                        // .setApkMD5("3df5b1c1d2bbd01b4a7ddb3f2722ccca")// Support MD5 verification. If the MD5 of the cached APK is the same as this MD5, the local cache will be directly used for installation. It is recommended to use MD5 verification
                        .setVersionCode(BuildConfig.VERSION_CODE) // Support versionCode verification. After setting the versionCode, the new version of the apk with the same versionCode is only downloaded once, and the local cache is preferred. It is recommended to use MD5 verification
                        .setFilename("AppUpdater.apk")
                        .setVibrate(true)
                        .build();
                mAppUpdater.setHttpManager(OkHttpManager.getInstance()).start();
                AppDialog.INSTANCE.dismissDialog();
            }
        });

        AppDialog.INSTANCE.showDialog(getContext(), view);
    }

    /**
     * Simple DialogFragment upgrade
     */
    private void clickBtn7() {
        AppDialogConfig config = new AppDialogConfig(getContext());
        config.setTitle("Simple DialogFragment Upgrade")
                .setConfirm("Upgrade")
                .setContent("1. Add a new function, \n2. Fix a problem, \n3. Optimize a bug, ")
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                // dismiss the dialog
                            }
                        })
                .setOnClickConfirm(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAppUpdater = new AppUpdater.Builder(getContext())
                                .setUrl(mUrl)
                                .build();
                        mAppUpdater.setHttpManager(OkHttpManager.getInstance()) // Download using OkHttp implementation
                                .setUpdateCallback(new UpdateCallback() { // Update callback
                                    @Override
                                    public void onDownloading(boolean isDownloading) {
                                        // Downloading: When isDownloading is true, it means that the download is already started,
                                        // that is, the download has been started before; when it is false, it means that the
                                        // download has not started yet and will start soon
                                    }

                                    @Override
                                    public void onStart(String url) {
                                        // start download
                                    }

                                    @Override
                                    public void onProgress(long progress, long total, boolean isChanged) {
                                        // Download progress update: It is recommended to update the progress of the
                                        // interface only when isChanged is true; because the actual progress changes frequently
                                    }

                                    @Override
                                    public void onFinish(File file) {
                                        // Download completed
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        // download failed
                                    }

                                    @Override
                                    public void onCancel() {
                                        // Cancel download
                                    }
                                }).start();

                        AppDialog.INSTANCE.dismissDialogFragment(getSupportFragmentManager());
                    }
                });
        AppDialog.INSTANCE.showDialogFragment(getSupportFragmentManager(), config);

    }

    private void clickCancel() {
        if (mAppUpdater != null) {
            mAppUpdater.stop();
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                clickBtn1();
                break;
            case R.id.btn2:
                clickBtn2();
                break;
            case R.id.btn3:
                clickBtn3();
                break;
            case R.id.btn4:
                clickBtn4();
                break;
            case R.id.btn5:
                clickBtn5();
                break;
            case R.id.btn6:
                clickBtn6();
                break;
            case R.id.btn7:
                clickBtn7();
                break;
            case R.id.btnCancel:
                clickCancel();
                break;
        }
    }
}
