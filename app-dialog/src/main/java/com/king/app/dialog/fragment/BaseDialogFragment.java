package com.king.app.dialog.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.king.app.dialog.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Basic dialog Fragment
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public abstract class BaseDialogFragment extends DialogFragment {
    /**
     * Default dialog width ratio (based on screen width)
     */
    protected static final float DEFAULT_WIDTH_RATIO = 0.85f;
    /**
     * Root view
     */
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getRootLayoutId(), container, false);
        init(mRootView);
        return mRootView;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        initDialogWindow(getContext(), dialog, Gravity.NO_GRAVITY, DEFAULT_WIDTH_RATIO, 0, 0, 0, 0, 0, 0, R.style.app_dialog_scale_animation);
        return dialog;
    }

    /**
     * Initialize the dialog view
     *
     * @param context          context
     * @param dialog           dialog box
     * @param gravity          alignment
     * @param widthRatio       width ratio, calculated based on screen width
     * @param x                x axis offset, needs to be used in conjunction with gravity
     * @param y                y axis offset, needs to be used in conjunction with gravity
     * @param horizontalMargin horizontal margin
     * @param verticalMargin   vertical margin
     * @param horizontalWeight horizontal weight
     * @param verticalWeight   vertical weight
     * @param animationStyleId dialog box animation style ID
     */
    protected void initDialogWindow(Context context, Dialog dialog, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight, int animationStyleId) {
        setDialogWindow(context, dialog, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, horizontalWeight, verticalWeight, animationStyleId);
    }

    /**
     * Set the pop-up window configuration
     *
     * @param context
     * @param dialog
     * @param gravity          Dialog alignment
     * @param widthRatio       width ratio, calculated based on screen width
     * @param x                x axis offset, needs to be used in conjunction with gravity
     * @param y                y axis offset, needs to be used in conjunction with gravity
     * @param horizontalMargin horizontal margin
     * @param verticalMargin   vertical margin
     * @param horizontalWeight horizontal weight
     * @param verticalWeight   vertical weight
     * @param animationStyleId animation style
     */
    private void setDialogWindow(Context context, Dialog dialog, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight, int animationStyleId) {
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.windowAnimations = animationStyleId;
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * widthRatio);
        lp.gravity = gravity;
        lp.x = x;
        lp.y = y;
        lp.horizontalMargin = horizontalMargin;
        lp.verticalMargin = verticalMargin;
        lp.horizontalWeight = horizontalWeight;
        lp.verticalWeight = verticalWeight;
        window.setAttributes(lp);
    }

    /**
     * Get the root view
     *
     * @return
     */
    protected View getRootView() {
        return mRootView;
    }

    /**
     * Set the text
     *
     * @param tv   {@link TextView}
     * @param text {@link CharSequence}
     */
    protected void setText(TextView tv, CharSequence text) {
        if (text != null) {
            tv.setText(text);
        }
    }

    /**
     * Click listener - dismiss the dialog
     *
     * @return {@link View.OnClickListener}
     */
    protected View.OnClickListener getOnClickDismiss() {
        return mOnClickDismissDialog;
    }

    private View.OnClickListener mOnClickDismissDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    /**
     * Get the root layout ID
     *
     * @return root layout ID
     */
    public abstract int getRootLayoutId();

    /**
     * Initialization
     *
     * @param rootView
     */
    public abstract void init(View rootView);

}