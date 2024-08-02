[Original AppUpdater Readme](../README.md)
# About this fork
This fork is ATM basically just a translation to english.

# AppUpdater

![Image](app/src/main/ic_launcher-web.png)

[![Download](https://img.shields.io/badge/download-App-blue.svg)](https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk)
[![MavenCentral](https://img.shields.io/maven-central/v/com.github.jenly1314.AppUpdater/app-updater)](https://repo1.maven.org/maven2/com/github/jenly1314/AppUpdater)
[![JCenter](https://img.shields.io/badge/JCenter-1.0.10-46C018.svg)](https://bintray.com/beta/#/jenly/maven/app-updater)
[![JitPack](https://jitpack.io/v/jenly1314/AppUpdater.svg)](https://jitpack.io/#jenly1314/AppUpdater)
[![CI](https://travis-ci.org/jenly1314/AppUpdater.svg?branch=master)](https://travis-ci.org/jenly1314/AppUpdater)
[![CircleCI](https://circleci.com/gh/jenly1314/AppUpdater.svg?style=svg)](https://circleci.com/gh/jenly1314/AppUpdater)
[![API](https://img.shields.io/badge/API-15%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/mit-license.php)

AppUpdater for Android is a lightweight open source library that focuses on app updates and integrates app version upgrades in one click for dummies.

> There is no need to worry about various details and adaptation issues; including but not limited to: notification bar adaptation, duplicate downloads, file access authorization, app installation, etc.; all these **AppUpdater** have been taken care of for you.

* **AppUpdater** core library mainly includes **app-updater** and **app-dialog**.

> **app-updater** is mainly responsible for downloading and updating apps in the background, so you don't need to worry about all kinds of configuration-related details when downloading, and upgrade in a foolproof way with one click.

> **app-dialog** mainly provides commonly used Dialog and DialogFragment to simplify the implementation of pop-up box prompts, and the layout style can be freely customized.

* The reason why download update and popup dialog are separated is because they are two functions in the first place. They are completely independent, which can be decoupled and less intrusive at the same time.

> If you only need to download and update apps, just rely on **app-updater**;
> If you need to download and update apps and also need dialogs to interact with them, then **app-updater** + **app-dialog** can be used in conjunction with **app-updater** and **app-dialog**.

## Features
- [x] Focus on App Updates with One-Click Foolproof Upgrade
- [x] Lightweight and small size.
- [x] Support listen to download and customize download process.
- [x] Support re-download when download fails.
- [x] Supports MD5 verification to avoid duplicate downloads.
- [x] Configurable notification content and flow.
- [x] Support cancel download
- [x] Support download using HttpsURLConnection or OkHttpClient.
- [x] Support for Android 10(Q)
- [x] Support for Android 11(R)
- [x] Support for Android 12(S)

## Gif Showcase
![Image](GIF.gif)

> You can also download the [demo app](https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk) to experience the effect

## Introducing

### Gradle:

1. Add the remote repository to **build.gradle** or **setting.gradle** in Project

    ```gradle
    repositories {
        //...
        mavenCentral()
    }
    ```

2. Add the dependencies to the Module's **build.gradle**.
   ```gradle
   //----------AndroidX version
   //app-updater
   implementation 'com.github.evermind-zz.AppUpdater:app-updater:1.2.0-1.2.0'
   //app-dialog
   implementation 'com.github.evermind-zz.AppUpdater:app-dialog:1.2.0-1.2.0'
   ```

## Use

### Code examples

```Java
    // One line of code, update for dummies
    new AppUpdater(getContext(),url).start();
```
```Java
    // Simple popup box update
    AppDialogConfig config = new AppDialogConfig(context);
    config.setTitle(“Simple Popup Box Upgrade”)
            .setConfirm(“Upgrade”) //old versions use setOk
            .setContent(“1, add so-and-so function,\n2, modify so-and-so problem,\n3, optimize so-and-so bug,”)
            .setOnClickConfirm(new View.OnClickListener() { // Older versions use setOnClickOk
                @Override
                public void onClick(View v) {
                    new AppUpdater.Builder(getContext())
                            .setUrl(mUrl)
                            .build()
                            .build() .start();
                    AppDialog.INSTANCE.dismissDialog();
                }
            });
    AppDialog.INSTANCE.showDialog(getContext(),config);
```
```Java
//Simple DialogFragment upgrade
    AppDialogConfig config = new AppDialogConfig(getContext());
    config.setTitle("Simple DialogFragment Upgrade")
            .setConfirm("Upgrade")
            .setContent("1. Add a new function, \n2. Fix a problem, \n3. Optimize a bug, ")
            .setOnClickConfirm(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUpdater appUpdater = new AppUpdater.Builder(getContext())
                            .setUrl(mUrl)
                            .build();
                    appUpdater.setHttpManager(OkHttpManager.getInstance()) // Download using OkHttp implementation
                            .setUpdateCallback(new UpdateCallback() { // Update callback
                                // Downloading: When isDownloading is true, it means that the download is already started,
                                // that is, the download has been started before; when it is false,
                                // it means that the download has not started yet and will start soon
                                @Override
                                public void onDownloading(boolean isDownloading) {
                                }

                                // start download
                                @Override
                                public void onStart(String url) {
                                }

                                // Download progress update: It is recommended to update the progress of the interface
                                // only when isChanged is true; because the actual progress changes frequently
                                @Override
                                public void onProgress(long progress, long total, boolean isChanged) {
                                }

                                // Download completed
                                @Override
                                public void onFinish(File file) {
                                }

                                // download failed
                                @Override
                                public void onError(Exception e) {
                                }

                                // Cancel download
                                @Override
                                public void onCancel() {
                                }
                            }).start();

                    AppDialog.INSTANCE.dismissDialogFragment(getSupportFragmentManager());
                }
            });
    AppDialog.INSTANCE.showDialogFragment(getSupportFragmentManager(), config);

```

For more usage details, please check the source code usage examples in [app](app) or directly check the [API help documentation](https://javadoc.jitpack.io/com/github/jenly1314/AppUpdater/1.1.0/javadoc/index.html)

### Additional Notes

#### app-updater

* When **HttpManager** is not set, **HttpManager** implemented by **HttpsURLConnection** is used for downloading by default. If you want to use **OkHttpClient** to implement downloading, you need to rely on the **okhttp** library; (**HttpManager** and **OkHttpManager** are provided by default internally)
* When supporting APK download, the local cache strategy is prioritized to avoid repeated downloading of the same APK file; (the verification method supports **file MD5** or **VersionCode** comparison)
* If you need to customize the relevant text information in the notification bar when updating the App, you only need to define the same name in **string.xml** to overwrite it (the resource definitions in **app-updater** all start with **app_updater**).
* When **Notification** is not set, **NotificationImpl** is used by default. If the current layout of the notification bar does not meet your needs, you can refer to **NotificationImpl** to customize an **INotification**;
* **AppUpdater** logs are uniformly managed using **LogUtils**. **LogUtils.setShowLog** can be used to globally set whether to display logs. When you need to locate **AppUpdater** internal log information, you only need to filter the **Log Tag** that starts with **AppUpdater**.
* For more configuration instructions of **AppUpdater**, please see **AppUpdater.Builder** or **UpdateConfig**; the methods basically have detailed instructions.

#### app-dialog

* **AppDialogConfig** mainly provides some dialog configurations. It provides a set of default configurations internally. You can also customize the dialog configurations through **AppDialogConfig**'s external exposure method; **AppDialog** is mainly responsible for the display and disappearance of the dialog box; through **AppDialog** and **AppDialogConfig**, you can easily implement a custom dialog box;
* **AppDialog** is general enough. It implements a set of most common dialog boxes and provides a series of default configurations, so that users can implement functions with as little configuration as possible. **AppDialog** is also abstract enough, and the layout style of the dialog box can be customized at will;
* Based on the above points, here is a special scenario description: If you do not want to define the dialog layout through a custom layout, and the default dialog text or button color does not meet your needs, and you only want to modify the color of the default dialog prompt text (including button text) in **AppDialog**, you can define the same name in **colors.xml** to override it (the resource definitions in **app-dialog** all start with **app_dialog**).

### Obfuscation

**app-updater** [Proguard rules](app-updater/proguard-rules.pro)

**app-dialog** [Proguard rules](app-dialog/proguard-rules.pro)

## related suggestion
#### [compose-component](https://github.com/jenly1314/compose-component) A component library for Jetpack Compose; it mainly provides some small components for quick use.

## Version History
#### v1.2.0-1.2.0: 2024-07-29
* relay "Back" Buton pressed via onDismissListener for AppDialogFragment

#### v1.2.0-1.1.0: 2024-07-29
* enclose TextView with ScrollView (for dialog content)

#### v1.2.0-1.0.0: 2024-07-25
* translate README to english

#### v1.2.0: 2023-7-9
* Updated Gradle to v7.3.3
* Optimize lint detection

#### v1.1.4: 2023-2-5
* Optimize comments
* Optimize details

#### v1.1.3: 2022-4-25
* Unified log management
* Compatible with Android 12(S)
* Optimize details

#### v1.1.2: 2021-11-18
* AppDialog provides more configuration related to WindowManager.LayoutParams

#### v1.1.1: 2021-9-14
* Provide more configurable parameters to the outside world
* Optimize details

#### v1.1.0: 2021-7-2 (starting from v1.1.0, no longer published to JCenter)
* Subsequent versions only support androidx, and the version name no longer contains the androidx logo
* Optimize details
* Migrate and publish to Maven Central

#### v1.0.10: 2021-3-4
* AppDialogConfig adds construction parameters to simplify custom extension usage
* Optimize details

#### v1.0.9: 2020-12-11
* Optimize the display details of the default Dialog style

#### v1.0.8: 2020-1-2
* Support MD5 verification
* Provide external access to Dialog methods

#### v1.0.7: 2019-12-18
* Optimize details

#### v1.0.6: 2019-11-27
* Added OkHttpManager. If you use OkHttpManager, you must rely on [okhttp](https://github.com/square/okhttp)
* Optimization details (progress, total changed from int -> long)

#### v1.0.5: 2019-9-4
* Support cancel download

#### v1.0.4: 2019-6-4 [Started to support AndroidX version](https://github.com/jenly1314/AppUpdater/tree/androidx)
* Support adding request headers

#### v1.0.3: 2019-5-9
* Added support for downloading APKs with priority to local cache to avoid downloading the same version of APK files multiple times
* AppDialog supports hiding the Dialog title

#### v1.0.2: 2019-3-18
* Added new settings for whether the notification bar should vibrate and ring tone
* AppDialogConfig adds getView(Context context) method

#### v1.0.1: 2019-1-10
* Upgrade Gradle to 4.6

#### v1.0 ：2018-6-29
* AppUpdater initial version

## Appreciation
If you like AppUpdater, or feel that AppUpdater has helped you, you can click the "Star" in the upper right corner to support it. Your support is my motivation. Thank you :smiley:
<p>You can also scan the QR code below to buy the author a cup of coffee :coffee:

<div>
    <img src="https://jenly1314.github.io/image/page/rewardcode.png">
</div>

## about me

| My Blog | GitHub | Gitee | CSDN | Blog Garden |
|:------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------|
| <a title="My Blog" href="https://jenly1314.github.io" target="_blank">Jenly's Blog</a> | <a title="GitHub Open Source Project" href="https://github.com/jenly1314" target="_blank">jenly1314</a> | <a title="Gitee Open Source Project" href="https://gitee.com/jenly1314" target="_blank">jenly1314</a> | <a title="CSDN Blog" href="http://blog.csdn.net/jenly121" target="_blank">jenly121</a> | <a title="Blog Park" href="https://www.cnblogs.com/jenly" target="_blank">jenly</a> |

## contact me

| WeChat public account | Gmail mailbox | QQ mailbox | QQ group | QQ group |
|:------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------|
| [Jenly666](http://weixin.qq.com/r/wzpWTuPEQL4-ract92-R) | <a title="Send me an email" href="mailto: jenly1314@gmail.com " target="_blank"> jenly1314</a> | <a title="Send me an email" href="mailto: jenly1314@vip.qq.com " target="_blank"> jenly1314</a> | <a title="Click to join the QQ group" href="https://qm.qq.com/cgi-bin/qm/qr?k=6_RukjAhwjAdDHEk2G7nph-o8fBFFzZz" target="_blank">20867961</a> | <a title="Click to join the QQ group" href="https://qm.qq.com/cgi-bin/qm/qr?k=Z9pobM8bzAW7tM_8xC31W8IcbIl0A-zT" target="_blank">64020761</a> |

<div>
    <img src="https://jenly1314.github.io/image/page/footer.png">
</div>


