package com.king.app.dialog.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.king.app.dialog.BaseDialogConfig;
import com.king.app.dialog.R;

/**
 * App dialog Fragment: encapsulates a convenient dialog API, making it easier to use
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppDialogFragment extends BaseDialogFragment {
    /**
     * Dialog configuration
     */
    private BaseDialogConfig config;

    /**
     * Create a new {@link AppDialogFragment} instance
     *
     * @param config
     * @return
     */
    public static AppDialogFragment newInstance(BaseDialogConfig config) {
        Bundle args = new Bundle();
        AppDialogFragment fragment = new AppDialogFragment();
        fragment.config = config;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getRootLayoutId() {
        if (config != null) {
            return config.getLayoutId();
        }
        return R.layout.app_dialog;
    }

    /**
     * Initialization
     *
     * @param rootView
     */
    public void init(View rootView) {
        if (config != null) {
            TextView tvDialogTitle = rootView.findViewById(config.getTitleId());
            if (tvDialogTitle != null) {
                setText(tvDialogTitle, config.getTitle());
                tvDialogTitle.setVisibility(config.isHideTitle() ? View.GONE : View.VISIBLE);
            }

            TextView tvDialogContent = rootView.findViewById(config.getContentId());
            if (tvDialogContent != null) {
                setText(tvDialogContent, config.getContent());
            }

            Button btnDialogCancel = rootView.findViewById(config.getCancelId());
            if (btnDialogCancel != null) {
                setText(btnDialogCancel, config.getCancel());
                btnDialogCancel.setOnClickListener(config.getOnClickCancel() != null ? config.getOnClickCancel() : getOnClickDismiss());
                btnDialogCancel.setVisibility(config.isHideCancel() ? View.GONE : View.VISIBLE);
            }

            View line = rootView.findViewById(config.getLineId());
            if (line != null) {
                line.setVisibility(config.isHideCancel() ? View.GONE : View.VISIBLE);
            }

            Button btnDialogConfirm = rootView.findViewById(config.getConfirmId());
            if (btnDialogConfirm != null) {
                setText(btnDialogConfirm, config.getConfirm());
                btnDialogConfirm.setOnClickListener(config.getOnClickConfirm() != null ? config.getOnClickConfirm() : getOnClickDismiss());
            }

        }
    }

    /**
     * Initialize the dialog
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
    @Override
    protected void initDialogWindow(Context context, Dialog dialog, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight, int animationStyleId) {
        if (config != null) {
            super.initDialogWindow(context, dialog, config.getGravity(), config.getWidthRatio(), config.getX(), config.getY(), config.getHorizontalMargin(), config.getVerticalMargin(), config.getHorizontalWeight(), config.getVerticalWeight(), config.getAnimationStyleId());
        } else {
            super.initDialogWindow(context, dialog, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, horizontalWeight, verticalWeight, animationStyleId);
        }
    }
}