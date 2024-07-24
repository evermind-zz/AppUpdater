package com.king.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.king.app.dialog.fragment.AppDialogFragment;

import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * App dialog box: encapsulates a convenient dialog box API, making it easier to use
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public enum AppDialog {

    INSTANCE;
    /**
     * Default dialog width ratio (based on screen width)
     */
    final float DEFAULT_WIDTH_RATIO = 0.85f;
    /**
     * Dialog box
     */
    private Dialog mDialog;
    /**
     * Label
     */
    private String mTag;


    //-------------------------------------------

    /**
     * Click listener - dismiss the dialog
     */
    View.OnClickListener mOnClickDismissDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismissDialog();
        }
    };

    //-------------------------------------------

    /**
     * {@link DialogFragment#dismiss()}
     *
     * @param fragmentManager {@link FragmentManager}
     */
    public void dismissDialogFragment(FragmentManager fragmentManager) {
        dismissDialogFragment(fragmentManager, mTag);
        mTag = null;
    }

    /**
     * {@link DialogFragment#dismiss()}
     *
     * @param fragmentManager {@link FragmentManager}
     * @param tag             dialogFragment corresponding label
     */
    public void dismissDialogFragment(FragmentManager fragmentManager, String tag) {
        if (tag != null) {
            DialogFragment dialogFragment = (DialogFragment) fragmentManager.findFragmentByTag(tag);
            dismissDialogFragment(dialogFragment);
        }
    }

    /**
     * {@link DialogFragment#dismiss()}
     *
     * @param dialogFragment {@link DialogFragment}
     */
    public void dismissDialogFragment(DialogFragment dialogFragment) {
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    //-------------------------------------------

    /**
     * Display DialogFragment
     *
     * @param fragmentManager {@link FragmentManager}
     * @param config          {@link AppDialogConfig}
     * @return
     */
    public String showDialogFragment(FragmentManager fragmentManager, AppDialogConfig config) {
        AppDialogFragment dialogFragment = AppDialogFragment.newInstance(config);
        String tag = dialogFragment.getTag() != null ? dialogFragment.getTag() : dialogFragment.getClass().getSimpleName();
        showDialogFragment(fragmentManager, dialogFragment, tag);
        mTag = tag;
        return tag;
    }

    /**
     * Display DialogFragment
     *
     * @param fragmentManager {@link FragmentManager}
     * @param dialogFragment  {@link DialogFragment}
     * @return
     */
    public String showDialogFragment(FragmentManager fragmentManager, DialogFragment dialogFragment) {
        String tag = dialogFragment.getTag() != null ? dialogFragment.getTag() : dialogFragment.getClass().getSimpleName();
        showDialogFragment(fragmentManager, dialogFragment, tag);
        mTag = tag;
        return tag;
    }

    /**
     * Display DialogFragment
     *
     * @param fragmentManager {@link FragmentManager}
     * @param dialogFragment  {@link DialogFragment}
     * @param tag             dialogFragment corresponding label
     * @return
     */
    public String showDialogFragment(FragmentManager fragmentManager, DialogFragment dialogFragment, String tag) {
        dismissDialogFragment(fragmentManager);
        dialogFragment.show(fragmentManager, tag);
        mTag = tag;
        return tag;
    }

    //-------------------------------------------

    /**
     * Show popup
     *
     * @param config pop-up configuration {@link AppDialogConfig}
     */
    public void showDialog(AppDialogConfig config) {
        showDialog(config, true);
    }

    /**
     * Show popup
     *
     * @param config   pop-up configuration {@link AppDialogConfig}
     * @param isCancel whether it is cancelable (default is true, false intercepts the back key)
     */
    public void showDialog(AppDialogConfig config, boolean isCancel) {
        showDialog(config.getContext(), config, isCancel);
    }

    /**
     * Show popup
     *
     * @param context context
     * @param config  pop-up configuration {@link AppDialogConfig}
     */
    public void showDialog(Context context, AppDialogConfig config) {
        showDialog(context, config, true);
    }

    /**
     * Show popup
     *
     * @param context  context
     * @param config   pop-up configuration {@link AppDialogConfig}
     * @param isCancel whether it is cancelable (default is true, false intercepts the back key)
     */
    public void showDialog(Context context, AppDialogConfig config, boolean isCancel) {
        showDialog(context, config.buildAppDialogView(), config.getStyleId(), config.getGravity(),
                config.getWidthRatio(), config.x, config.y, config.horizontalMargin, config.verticalMargin,
                config.horizontalWeight, config.verticalWeight, isCancel);
    }

    /**
     * Show popup
     *
     * @param context     context
     * @param contentView pop-up content view
     */
    public void showDialog(Context context, View contentView) {
        showDialog(context, contentView, DEFAULT_WIDTH_RATIO);
    }

    /**
     * Show popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param isCancel    whether it is cancelable (default is true, false intercepts the back key)
     */
    public void showDialog(Context context, View contentView, boolean isCancel) {
        showDialog(context, contentView, R.style.app_dialog, DEFAULT_WIDTH_RATIO, isCancel);
    }

    /**
     * Show popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param widthRatio  width ratio, calculated based on screen width
     */
    public void showDialog(Context context, View contentView, float widthRatio) {
        showDialog(context, contentView, widthRatio, true);
    }

    /**
     * Show popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param widthRatio  width ratio, calculated based on screen width
     * @param isCancel    whether it is cancelable (default is true, false intercepts the back key)
     */
    public void showDialog(Context context, View contentView, float widthRatio, boolean isCancel) {
        showDialog(context, contentView, R.style.app_dialog, widthRatio, isCancel);
    }


    /**
     * Show popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param styleId     Dialog style
     * @param widthRatio  width ratio, calculated based on screen width
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, float widthRatio) {
        showDialog(context, contentView, styleId, widthRatio, true);
    }

    /**
     * Show popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param styleId     Dialog style
     * @param widthRatio  width ratio, calculated based on screen width
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio) {
        showDialog(context, contentView, styleId, gravity, widthRatio, true);
    }

    /**
     * Show popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param styleId     Dialog style
     * @param widthRatio  width ratio, calculated based on screen width
     * @param isCancel    whether it is cancelable (default is true, false intercepts the back key)
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, float widthRatio, final boolean isCancel) {
        showDialog(context, contentView, styleId, Gravity.NO_GRAVITY, widthRatio, isCancel);
    }

    /**
     * Show popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param styleId     Dialog style
     * @param gravity     Dialog alignment
     * @param widthRatio  width ratio, calculated based on screen width
     * @param isCancel    whether it is cancelable (default is true, false intercepts the back key)
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, final boolean isCancel) {
        showDialog(context, contentView, styleId, gravity, widthRatio, 0, 0, isCancel);
    }


    /**
     * Show popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param styleId     Dialog style
     * @param gravity     Dialog alignment
     * @param widthRatio  width ratio, calculated based on screen width
     * @param x           x axis offset, needs to be used in conjunction with gravity
     * @param y           y axis offset, needs to be used in conjunction with gravity
     * @param isCancel    whether it is cancelable (default is true, false intercepts the back key)
     * @return
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, final boolean isCancel) {
        showDialog(context, contentView, styleId, gravity, widthRatio, x, y, 0, 0, isCancel);
    }

    /**
     * Show popup
     *
     * @param context          context
     * @param contentView      pop-up content view
     * @param styleId          Dialog style
     * @param gravity          Dialog alignment
     * @param widthRatio       width ratio, calculated based on screen width
     * @param x                x axis offset, needs to be used in conjunction with gravity
     * @param y                y axis offset, needs to be used in conjunction with gravity
     * @param horizontalMargin horizontal margin
     * @param verticalMargin   vertical margin
     * @param isCancel         whether it is cancelable (default is true, false intercepts the back key)
     * @return
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, final boolean isCancel) {
        showDialog(context, contentView, styleId, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, 0, 0, isCancel);
    }

    /**
     * Show popup
     *
     * @param context          context
     * @param contentView      pop-up content view
     * @param styleId          Dialog style
     * @param gravity          Dialog alignment
     * @param widthRatio       width ratio, calculated based on screen width
     * @param x                x axis offset, needs to be used in conjunction with gravity
     * @param y                y axis offset, needs to be used in conjunction with gravity
     * @param horizontalMargin horizontal margin
     * @param verticalMargin   vertical margin
     * @param horizontalWeight horizontal weight
     * @param verticalWeight   vertical weight
     * @param isCancel         whether it is cancelable (default is true, false intercepts the back key)
     * @return
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight, final boolean isCancel) {
        dismissDialog();
        mDialog = createDialog(context, contentView, styleId, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, horizontalWeight, verticalWeight, isCancel);
        mDialog.show();
    }

    /**
     * Set the pop-up window configuration
     *
     * @param context          context
     * @param dialog           Dialog dialog
     * @param gravity          Dialog alignment
     * @param widthRatio       width ratio, calculated based on screen width
     * @param x                x axis offset, needs to be used in conjunction with gravity
     * @param y                y axis offset, needs to be used in conjunction with gravity
     * @param horizontalMargin horizontal margin
     * @param verticalMargin   vertical margin
     * @param horizontalWeight horizontal weight
     * @param verticalWeight   vertical weight
     */
    private void setDialogWindow(Context context, Dialog dialog, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
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
     * Create a popup
     *
     * @param config pop-up configuration {@link AppDialogConfig}
     */
    public Dialog createDialog(AppDialogConfig config) {
        return createDialog(config, true);
    }

    /**
     * Create a popup
     *
     * @param config   pop-up configuration {@link AppDialogConfig}
     * @param isCancel whether it is cancelable (default is true, false intercepts the back key)
     */
    public Dialog createDialog(AppDialogConfig config, boolean isCancel) {
        return createDialog(config.getContext(), config.buildAppDialogView(), config.getStyleId(), DEFAULT_WIDTH_RATIO, isCancel);
    }

    /**
     * Create a popup
     *
     * @param context context
     * @param config  pop-up configuration {@link AppDialogConfig}
     */
    public Dialog createDialog(Context context, AppDialogConfig config) {
        return createDialog(context, config, true);
    }

    /**
     * Create a popup
     *
     * @param context  context
     * @param config   pop-up configuration {@link AppDialogConfig}
     * @param isCancel whether it is cancelable (default is true, false intercepts the back key)
     */
    public Dialog createDialog(Context context, AppDialogConfig config, boolean isCancel) {
        return createDialog(context, config.buildAppDialogView(), config.getStyleId(), DEFAULT_WIDTH_RATIO, isCancel);
    }

    /**
     * Create a popup
     *
     * @param context     context
     * @param contentView pop-up content view
     */
    public Dialog createDialog(Context context, View contentView) {
        return createDialog(context, contentView, DEFAULT_WIDTH_RATIO);
    }

    /**
     * Create a popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param isCancel    whether it is cancelable (default is true, false intercepts the back key)
     */
    public Dialog createDialog(Context context, View contentView, boolean isCancel) {
        return createDialog(context, contentView, R.style.app_dialog, DEFAULT_WIDTH_RATIO, isCancel);
    }

    /**
     * Create a popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param widthRatio  width ratio, calculated based on screen width
     */
    public Dialog createDialog(Context context, View contentView, float widthRatio) {
        return createDialog(context, contentView, widthRatio, true);
    }

    /**
     * Create a popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param widthRatio  width ratio, calculated based on screen width
     * @param isCancel    whether it is cancelable (default is true, false intercepts the back key)
     */
    public Dialog createDialog(Context context, View contentView, float widthRatio, boolean isCancel) {
        return createDialog(context, contentView, R.style.app_dialog, widthRatio, isCancel);
    }

    /**
     * Create a popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param styleId     Dialog style
     * @param widthRatio  width ratio, calculated based on screen width
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, float widthRatio) {
        return createDialog(context, contentView, styleId, Gravity.NO_GRAVITY, widthRatio, true);
    }

    /**
     * Create a popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param styleId     Dialog style
     * @param gravity     Dialog alignment
     * @param widthRatio  width ratio, calculated based on screen width
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio) {
        return createDialog(context, contentView, styleId, gravity, widthRatio, true);
    }


    /**
     * Create a popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param styleId     Dialog style
     * @param widthRatio  width ratio, calculated based on screen width
     * @param isCancel    whether it is cancelable (default is true, false intercepts the back key)
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, float widthRatio, final boolean isCancel) {
        return createDialog(context, contentView, styleId, Gravity.NO_GRAVITY, widthRatio, isCancel);
    }

    /**
     * Create a popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param styleId     Dialog style
     * @param gravity     Dialog alignment
     * @param widthRatio  width ratio, calculated based on screen width
     * @param isCancel    whether it is cancelable (default is true, false intercepts the back key)
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, final boolean isCancel) {
        return createDialog(context, contentView, styleId, gravity, widthRatio, 0, 0, isCancel);
    }

    /**
     * Create a popup
     *
     * @param context     context
     * @param contentView pop-up content view
     * @param styleId     Dialog style
     * @param gravity     Dialog alignment
     * @param widthRatio  width ratio, calculated based on screen width
     * @param x           x axis offset, needs to be used in conjunction with gravity
     * @param y           y axis offset, needs to be used in conjunction with gravity
     * @param isCancel    whether it is cancelable (default is true, false intercepts the back key)
     * @return
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, final boolean isCancel) {
        return createDialog(context, contentView, styleId, gravity, widthRatio, x, y, 0, 0, isCancel);
    }

    /**
     * Create a popup
     *
     * @param context          context
     * @param contentView      pop-up content view
     * @param styleId          Dialog style
     * @param gravity Dialog alignment
     * @param widthRatio       width ratio, calculated based on screen width
     * @param x                x axis offset, needs to be used in conjunction with gravity
     * @param y                y axis offset, needs to be used in conjunction with gravity
     * @param horizontalMargin horizontal margin
     * @param verticalMargin vertical margin
     * @param isCancel whether it is cancelable (default is true, false intercepts the back key)
     * @return
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, final boolean isCancel) {
        return createDialog(context, contentView, styleId, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, 0, 0, isCancel);
    }

    /**
     * Create a popup
     *
     * @param context          context
     * @param contentView      pop-up content view
     * @param styleId          Dialog style
     * @param gravity          Dialog alignment
     * @param widthRatio       width ratio, calculated based on screen width
     * @param x                x axis offset, needs to be used in conjunction with gravity
     * @param y                y axis offset, needs to be used in conjunction with gravity
     * @param horizontalMargin horizontal margin
     * @param verticalMargin   vertical margin
     * @param horizontalWeight horizontal weight
     * @param verticalWeight   vertical weight
     * @param isCancel         whether it is cancelable (default is true, false intercepts the back key)
     * @return
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight, final boolean isCancel) {
        Dialog dialog = new Dialog(context, styleId);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isCancel) {
                        dismissDialog();
                    }
                    return true;
                }
                return false;

            }
        });
        setDialogWindow(context, dialog, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, horizontalWeight, verticalWeight);
        return dialog;
    }

    /**
     * Get Dialog
     *
     * @return {@link #mDialog}
     */
    public Dialog getDialog() {
        return mDialog;
    }

    /**
     * {@link Dialog#dismiss()}
     */
    public void dismissDialog() {
        dismissDialog(mDialog);
        mDialog = null;
    }

    /**
     * {@link Dialog#dismiss()}
     *
     * @param dialog {@link Dialog}
     */
    public void dismissDialog(Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    //-------------------------------------------

}