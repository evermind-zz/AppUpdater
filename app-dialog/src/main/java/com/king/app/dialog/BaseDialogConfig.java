package com.king.app.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

/**
 * Basic dialog configuration
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class BaseDialogConfig {

    /**
     * Layout ID
     */
    @LayoutRes
    int layoutId;
    /**
     * Title view ID
     */
    @IdRes
    int titleId = R.id.tvDialogTitle;
    /**
     * Content view ID
     */
    @IdRes
    int contentId = R.id.tvDialogContent;
    /**
     * Cancel view ID (button on the left)
     */
    @IdRes
    int cancelId = R.id.btnDialogCancel;
    /**
     * Determine the view ID (button on the right)
     */
    @IdRes
    int confirmId = R.id.btnDialogConfirm;
    /**
     * The ID of the dividing line in the middle of the button
     */
    @IdRes
    int lineId = R.id.line;

    /**
     * Style ID
     */
    @StyleRes
    int styleId = R.style.app_dialog;

    /**
     * Dialog animation style ID
     */
    @StyleRes
    int animationStyleId = R.style.app_dialog_scale_animation;

    /**
     * Title text
     */
    CharSequence title;
    /**
     * Content text
     */
    CharSequence content;
    /**
     * Cancel button text
     */
    CharSequence cancel;
    /**
     * OK button text
     */
    CharSequence confirm;
    /**
     * Whether to hide the cancel button. If the cancel button is hidden, only one button will be displayed at the bottom.
     */
    boolean isHideCancel;
    /**
     * Whether to hide the title
     */
    boolean isHideTitle;

    /**
     * Width ratio, calculated based on screen width
     */
    float widthRatio = AppDialog.INSTANCE.DEFAULT_WIDTH_RATIO;

    /**
     * Dialog alignment {@link WindowManager.LayoutParams#gravity}
     */
    int gravity = Gravity.NO_GRAVITY;

    /**
     * {@link WindowManager.LayoutParams#x}
     */
    int x;
    /**
     * {@link WindowManager.LayoutParams#y}
     */
    int y;
    /**
     * {@link WindowManager.LayoutParams#verticalMargin}
     */
    float verticalMargin;
    /**
     * {@link WindowManager.LayoutParams#horizontalMargin}
     */
    float horizontalMargin;
    /**
     * {@link WindowManager.LayoutParams#horizontalWeight}
     */
    float horizontalWeight;
    /**
     * {@link WindowManager.LayoutParams#verticalWeight}
     */
    float verticalWeight;
    /**
     * Click the "Cancel" button to listen
     */
    View.OnClickListener onClickCancel;
    /**
     * Click the "OK" button to listen
     */
    View.OnClickListener onClickConfirm;
    /**
     * "Back" button listener
     */
    DialogInterface.OnDismissListener dismissListener;

    /**
     * Construction
     */
    public BaseDialogConfig() {
        this(R.layout.app_dialog);
    }

    /**
     * Construction
     *
     * @param layoutId layout ID
     */
    public BaseDialogConfig(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    /**
     * Layout ID
     *
     * @return layout ID
     */
    @LayoutRes
    public int getLayoutId() {
        return layoutId;
    }

    /**
     * This method will be deprecated soon. Please initialize it by constructing {@link #BaseDialogConfig(int)}
     *
     * @param layoutId layout ID
     * @return {@link BaseDialogConfig}
     * @deprecated This method will be deprecated soon and may be removed in the next version.
     */
    @Deprecated
    public BaseDialogConfig setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    /**
     * Title view ID
     *
     * @return view ID
     */
    @IdRes
    public int getTitleId() {
        return titleId;
    }

    /**
     * Set the title view ID
     *
     * @param titleId view ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setTitleId(@IdRes int titleId) {
        this.titleId = titleId;
        return this;
    }

    /**
     * Style ID
     *
     * @return style ID
     */
    @StyleRes
    public int getStyleId() {
        return styleId;
    }

    /**
     * Set the Dialog style ID (valid only for Dialog, if you are using DialogFragment, please use {@link #setAnimationStyleId(int)})
     *
     * @param styleId
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setStyleId(@StyleRes int styleId) {
        this.styleId = styleId;
        return this;
    }

    /**
     * Dialog animation style ID
     *
     * @return
     */
    @StyleRes
    public int getAnimationStyleId() {
        return animationStyleId;
    }

    /**
     * Dialog animation style ID (valid only for DialogFragment. If you are using Dialog, please use {@link #setStyleId(int)})
     *
     * @param animationStyleId
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setAnimationStyleId(@StyleRes int animationStyleId) {
        this.animationStyleId = animationStyleId;
        return this;
    }

    /**
     * Content view ID
     *
     * @return view ID
     */
    @IdRes
    public int getContentId() {
        return contentId;
    }

    /**
     * Set the content view ID
     *
     * @param contentId content view ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setContentId(@IdRes int contentId) {
        this.contentId = contentId;
        return this;
    }

    /**
     * Cancel button view ID
     *
     * @return view ID
     */
    @IdRes
    public int getCancelId() {
        return cancelId;
    }

    /**
     * Set the cancel button view ID
     *
     * @param cancelId cancel button view ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setCancelId(@IdRes int cancelId) {
        this.cancelId = cancelId;
        return this;
    }

    /**
     * Get the OK button view ID
     *
     * @return Confirm button view ID
     * @Deprecated Please use {@link #getConfirmId()} instead. This method may be removed in subsequent versions.
     */
    @Deprecated
    @IdRes
    public int getOkId() {
        return getConfirmId();
    }

    /**
     * Set the OK button view ID
     *
     * @param okId OK button view ID
     * @return {@link BaseDialogConfig}
     * @Deprecated Please use {@link #setConfirmId(int)} instead. This method may be removed in subsequent versions.
     */
    @Deprecated
    public BaseDialogConfig setOkId(@IdRes int okId) {
        return setConfirmId(okId);
    }

    /**
     * Get the OK button view ID
     *
     * @return view ID
     */
    @IdRes
    public int getConfirmId() {
        return confirmId;
    }

    /**
     * Set the OK button view ID
     *
     * @param confirmId Confirm button view ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setConfirmId(@IdRes int confirmId) {
        this.confirmId = confirmId;
        return this;
    }

    /**
     * Dividing line view ID
     *
     * @return view ID
     */
    @IdRes
    public int getLineId() {
        return lineId;
    }

    /**
     * Set the dividing line view ID
     *
     * @param lineId dividing line view ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setLineId(@IdRes int lineId) {
        this.lineId = lineId;
        return this;
    }

    /**
     * Title
     *
     * @return title
     */
    public CharSequence getTitle() {
        return title;
    }

    /**
     * Set the title
     *
     * @param title title
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    /**
     * Set the title
     *
     * @param context context
     * @param resId   Title resource ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setTitle(@NonNull Context context, @StringRes int resId) {
        this.title = context.getString(resId);
        return this;
    }

    /**
     * Text content
     *
     * @return text content
     */
    public CharSequence getContent() {
        return content;
    }

    /**
     * Set the text content
     *
     * @param content text content
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setContent(CharSequence content) {
        this.content = content;
        return this;
    }

    /**
     * Cancel button text content
     *
     * @return Cancel button text content
     */
    public CharSequence getCancel() {
        return cancel;
    }

    /**
     * Set the cancel button text
     *
     * @param cancel cancel button text content
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setCancel(CharSequence cancel) {
        this.cancel = cancel;
        return this;
    }

    /**
     * Set the cancel button text
     *
     * @param context context
     * @param resId   Cancel button text content resource ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setCancel(@NonNull Context context, @StringRes int resId) {
        this.cancel = context.getString(resId);
        return this;
    }

    /**
     * Get the text content of the OK button
     *
     * @return Confirm button text content
     * @deprecated Please use {@link #getConfirm()} instead. This method may be removed in subsequent versions.
     */
    @Deprecated
    public CharSequence getOk() {
        return getConfirm();
    }

    /**
     * Set the text content of the OK button
     *
     * @param ok confirms the button text content
     * @return {@link BaseDialogConfig}
     * @deprecated Please use {@link #setConfirm(CharSequence)} instead. This method may be removed in subsequent versions.
     */
    @Deprecated
    public BaseDialogConfig setOk(CharSequence ok) {
        return setConfirm(ok);
    }

    /**
     * Set the text content of the OK button
     *
     * @param context context
     * @param resId   Confirm button text content resource ID
     * @return {@link BaseDialogConfig}
     * @deprecated Please use {@link #setConfirm(Context, int)} instead. This method may be removed in subsequent versions.
     */
    @Deprecated
    public BaseDialogConfig setOk(@NonNull Context context, @StringRes int resId) {
        return setConfirm(context, resId);
    }

    /**
     * Confirm button text content
     *
     * @return Confirm button text content
     */
    public CharSequence getConfirm() {
        return confirm;
    }

    /**
     * Set the text content of the OK button
     *
     * @param confirm confirm button text content
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setConfirm(CharSequence confirm) {
        this.confirm = confirm;
        return this;
    }

    /**
     * Set the text content of the OK button
     *
     * @param context context
     * @param resId   Confirm button text content resource ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setConfirm(@NonNull Context context, @StringRes int resId) {
        this.confirm = context.getString(resId);
        return this;
    }

    /**
     * Whether to hide the cancel button
     *
     * @return {@link #isHideCancel}
     */
    public boolean isHideCancel() {
        return isHideCancel;
    }

    /**
     * Set whether to hide the cancel button
     *
     * @param hideCancel whether to hide the cancel button
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setHideCancel(boolean hideCancel) {
        isHideCancel = hideCancel;
        return this;
    }

    /**
     * Whether to hide the title
     *
     * @return {@link #isHideTitle}
     */
    public boolean isHideTitle() {
        return isHideTitle;
    }

    /**
     * Set whether to hide the title
     *
     * @param hideTitle whether to hide the title
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setHideTitle(boolean hideTitle) {
        isHideTitle = hideTitle;
        return this;
    }

    /**
     * The width ratio of the Dialog is calculated based on the screen width
     *
     * @return {@link #widthRatio}
     */
    public float getWidthRatio() {
        return widthRatio;
    }

    /**
     * Set the width ratio of the Dialog, calculated based on the screen width
     *
     * @param widthRatio Dialog width ratio; default value: {@link AppDialog#DEFAULT_WIDTH_RATIO}
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setWidthRatio(float widthRatio) {
        this.widthRatio = widthRatio;
        return this;
    }

    /**
     * Dialog alignment {@link WindowManager.LayoutParams#gravity}
     *
     * @return Dialog alignment
     */
    public int getGravity() {
        return gravity;
    }

    /**
     * Set the alignment of the Dialog {@link WindowManager.LayoutParams#gravity}
     *
     * @param gravity Dialog alignment
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    /**
     * "Cancel" button click listener, do not set the default click to close the pop-up window
     *
     * @return "Cancel" button click listener
     */
    public View.OnClickListener getOnClickCancel() {
        return onClickCancel;
    }

    /**
     * Set the "Cancel" button click listener, do not set the default click to close the pop-up window
     *
     * @param onClickCancel "Cancel" button click listener
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setOnClickCancel(View.OnClickListener onClickCancel) {
        this.onClickCancel = onClickCancel;
        return this;
    }

    /**
     * "OK" button click listener, do not set the default click to close the pop-up window
     *
     * @return "OK" button click listener
     */
    public View.OnClickListener getOnClickConfirm() {
        return onClickConfirm;
    }

    /**
     * Set the "OK" button click listener, do not set the default click to close the pop-up window
     *
     * @param onClickConfirm "Confirm" button click listener
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setOnClickConfirm(View.OnClickListener onClickConfirm) {
        this.onClickConfirm = onClickConfirm;
        return this;
    }


    /**
     * Get the "OK" button click listener, do not set the default click to close the pop-up window
     *
     * @return "OK" button click listener
     * @deprecated Please use {@link #getOnClickConfirm()} instead. This method may be removed in subsequent versions.
     */
    @Deprecated
    public View.OnClickListener getOnClickOk() {
        return getOnClickConfirm();
    }

    /**
     * Set the "OK" button click listener, do not set the default click to close the pop-up window
     *
     * @param onClickOk "OK" button click listener
     * @return {@link BaseDialogConfig}
     * @deprecated Please use {@link #setOnClickConfirm(View.OnClickListener)} instead. This method may be removed in subsequent versions.
     */
    @Deprecated
    public BaseDialogConfig setOnClickOk(View.OnClickListener onClickOk) {
        return setOnClickConfirm(onClickOk);
    }

    /**
     * {@link WindowManager.LayoutParams#x}
     *
     * @return {@link #x}
     */
    public int getX() {
        return x;
    }

    /**
     * {@link WindowManager.LayoutParams#x}
     *
     * @param x x axis coordinate
     */
    public BaseDialogConfig setX(int x) {
        this.x = x;
        return this;
    }

    /**
     * {@link WindowManager.LayoutParams#y}
     *
     * @return {@link #y}
     */
    public int getY() {
        return y;
    }

    /**
     * {@link WindowManager.LayoutParams#y}
     *
     * @param y y axis coordinate
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setY(int y) {
        this.y = y;
        return this;
    }

    /**
     * {@link WindowManager.LayoutParams#verticalMargin}
     *
     * @return {@link #verticalMargin}
     */
    public float getVerticalMargin() {
        return verticalMargin;
    }

    /**
     * {@link WindowManager.LayoutParams#verticalMargin}
     *
     * @param verticalMargin vertical margin
     */
    public void setVerticalMargin(float verticalMargin) {
        this.verticalMargin = verticalMargin;
    }

    /**
     * {@link WindowManager.LayoutParams#horizontalMargin}
     *
     * @return {@link #horizontalMargin}
     */
    public float getHorizontalMargin() {
        return horizontalMargin;
    }

    /**
     * {@link WindowManager.LayoutParams#horizontalMargin}
     *
     * @param horizontalMargin horizontal margin
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setHorizontalMargin(float horizontalMargin) {
        this.horizontalMargin = horizontalMargin;
        return this;
    }

    /**
     * {@link WindowManager.LayoutParams#horizontalWeight}
     *
     * @return {@link #horizontalWeight}
     */
    public float getHorizontalWeight() {
        return horizontalWeight;
    }

    /**
     * {@link WindowManager.LayoutParams#horizontalWeight}
     *
     * @param horizontalWeight horizontal weight
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setHorizontalWeight(float horizontalWeight) {
        this.horizontalWeight = horizontalWeight;
        return this;
    }

    /**
     * {@link WindowManager.LayoutParams#verticalWeight}
     *
     * @return {@link #verticalWeight}
     */
    public float getVerticalWeight() {
        return verticalWeight;
    }

    /**
     * {@link WindowManager.LayoutParams#verticalWeight}
     *
     * @param verticalWeight vertical weight
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setVerticalWeight(float verticalWeight) {
        this.verticalWeight = verticalWeight;
        return this;
    }

    /**
     * Set the "Back" button click listener.
     *
     * This listener is only called if the dialog disappears by pushing the back button.
     * It will NOT be called if the dialog is recreated (eg. rotation the device).
     *
     * @param dismissListener "Back" button click listener
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
        return this;
    }

    /**
     * "Back" button click listener
     *
     * @return "Back" button click listener
     */
    public DialogInterface.OnDismissListener getOnDismissListener() {
        return this.dismissListener;
    }
}