package com.king.app.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.regex.Pattern;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * App dialog configuration
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppDialogConfig extends BaseDialogConfig {

    private Context context;

    private SparseArray<View> views;

    private View view;

    private ViewHolder viewHolder;

    /**
     * Construction
     *
     * @param context context
     */
    public AppDialogConfig(@NonNull Context context) {
        this(context, R.layout.app_dialog);
    }

    /**
     * Construction
     *
     * @param context context
     * @param layoutId layout ID
     */
    public AppDialogConfig(@NonNull Context context, @LayoutRes int layoutId) {
        super(layoutId);
        this.context = context;
        views = new SparseArray<>();
    }

    /**
     * Get context
     *
     * @return {@link #context}
     */
    public Context getContext() {
        return context;
    }

    /**
     * Get the dialog view
     *
     * @param context context
     * @return dialog view
     * @deprecated This method will be deprecated soon and may be removed in the next version.
     */
    @Deprecated
    public View getView(@NonNull Context context) {
        return getDialogView();
    }

    /**
     * Get the dialog view
     *
     * @return dialog view
     */
    private View getDialogView() {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(getLayoutId(), null);
        }
        return view;
    }

    /**
     * Find the corresponding view by view ID
     *
     * @param id view ID
     * @param <T> corresponding view class
     * @return The view corresponding to the view ID
     */
    private <T extends View> T findView(@IdRes int id) {
        return getDialogView().findViewById(id);
    }

    /**
     * Get the corresponding view according to the view ID
     *
     * @param id view ID
     * @param <T> corresponding view class
     * @return The view corresponding to the view ID
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int id) {
        View v = views.get(id);
        if (v == null) {
            v = findView(id);
            views.put(id, v);
        }

        return (T) v;
    }

    /**
     * Create a view via {@link AppDialogConfig}
     *
     * @return {@link View}
     */
    View buildAppDialogView() {
        TextView tvDialogTitle = getView(titleId);
        if (tvDialogTitle != null) {
            setText(tvDialogTitle, title);
            tvDialogTitle.setVisibility(isHideTitle ? View.GONE : View.VISIBLE);
        }

        TextView tvDialogContent = getView(contentId);
        if (tvDialogContent != null) {
            setText(tvDialogContent, content);
        }

        Button btnDialogCancel = getView(cancelId);
        if (btnDialogCancel != null) {
            setText(btnDialogCancel, cancel);
            btnDialogCancel.setOnClickListener(onClickCancel != null ? onClickCancel : AppDialog.INSTANCE.mOnClickDismissDialog);
            btnDialogCancel.setVisibility(isHideCancel ? View.GONE : View.VISIBLE);
        }

        View line = getView(lineId);
        if (line != null) {
            line.setVisibility(isHideCancel ? View.GONE : View.VISIBLE);
        }

        Button btnDialogConfirm = getView(confirmId);
        if (btnDialogConfirm != null) {
            setText(btnDialogConfirm, confirm);
            btnDialogConfirm.setOnClickListener(onClickConfirm != null ? onClickConfirm : AppDialog.INSTANCE.mOnClickDismissDialog);

        }

        return view;
    }

    /**
     * Set the text
     *
     * @param tv   {@link TextView}
     * @param text {@link CharSequence}
     */
    private void setText(TextView tv, CharSequence text) {
        if (text != null) {
            tv.setText(text);
        }
    }

    /**
     * Get {@link ViewHolder}
     *
     * @return {@link ViewHolder}
     */
    public final ViewHolder getViewHolder() {
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
        }
        return viewHolder;
    }


    /**
     * ViewHolder mainly provides some common settings for view controls (applicable to Dialog, not applicable to DialogFragment)
     */
    public final class ViewHolder {

        private ViewHolder() {

        }

        //--------------------- Common settings for controls

        /**
         * Set the background color of the view
         * {@link View#setBackgroundResource(int)}
         *
         * @param id    view ID
         * @param resId Drawable resource ID
         * @return {@link View}
         */
        public View setBackgroundResource(@IdRes int id, @DrawableRes int resId) {
            View v = getView(id);
            v.setBackgroundResource(resId);
            return v;
        }

        /**
         * Set the background color of the view
         * {@link View#setBackground(Drawable)}
         *
         * @param id       view ID
         * @param drawable {@link Drawable}
         * @return {@link View}
         */
        @TargetApi(16)
        public View setBackground(@IdRes int id, Drawable drawable) {
            View v = getView(id);
            v.setBackground(drawable);
            return v;
        }

        /**
         * Set the background color of the view
         * {@link View#setBackgroundColor(int)}
         *
         * @param id    view ID
         * @param color color
         * @return {@link View}
         */
        public View setBackgroundColor(@IdRes int id, @ColorInt int color) {
            View v = getView(id);
            v.setBackgroundColor(color);
            return v;
        }

        /**
         * Set the view's label
         * {@link View#setTag(Object)}
         *
         * @param id  view ID
         * @param tag tag
         * @return {@link View}
         */
        public View setTag(@IdRes int id, Object tag) {
            View v = getView(id);
            v.setTag(tag);
            return v;
        }

        /**
         * Set the view's label
         * {@link View#setTag(int, Object)}
         *
         * @param id  view ID
         * @param key tag key
         * @param tag tag
         * @return {@link View}
         */
        public View setTag(@IdRes int id, int key, Object tag) {
            View v = getView(id);
            v.setTag(key, tag);
            return v;
        }

        /**
         * Set the visibility of the view
         * {@link View#setVisibility(int)}
         *
         * @param id         view ID
         * @param visibility visibility
         * @return {@link View}
         */
        public View setVisibility(@IdRes int id, int visibility) {
            View v = getView(id);
            v.setVisibility(visibility);
            return v;
        }

        /**
         * Set the visibility of the view
         * {@link View#setVisibility(int)}
         *
         * @param id        view ID
         * @param isVisible Whether it is visible; when true, it is set to: {@link View#VISIBLE}; when false, it is set to: {@link View#GONE}
         * @return {@link View}
         */
        public View setVisibility(@IdRes int id, boolean isVisible) {
            View v = getView(id);
            if (isVisible) {
                v.setVisibility(View.VISIBLE);
            } else {
                v.setVisibility(View.GONE);
            }
            return v;
        }

        /**
         * Set the visibility of the view
         * {@link View#setVisibility(int)}
         *
         * @param id        view ID
         * @param isVisible Whether it is visible; when true, it is set to: {@link View#VISIBLE}; when false, it is set to: {@link View#INVISIBLE}
         * @return {@link View}
         */
        public View setInVisibility(@IdRes int id, boolean isVisible) {
            View v = getView(id);
            if (isVisible) {
                v.setVisibility(View.VISIBLE);
            } else {
                v.setVisibility(View.INVISIBLE);
            }
            return v;
        }

        /**
         * Set the transparency of the view
         * {@link View#setAlpha(float)}
         *
         * @param id    view ID
         * @param alpha transparency
         * @return {@link View}
         */
        public View setAlpha(@IdRes int id, float alpha) {
            View v = getView(id);
            v.setAlpha(alpha);
            return v;
        }

        /**
         * Set the composite drawing {@link Drawable} on the left side of the view
         * {@link #setCompoundDrawables(int, Drawable, Drawable, Drawable, Drawable)}
         *
         * @param id       view ID
         * @param drawable {@link Drawable}
         * @return {@link TextView}
         */
        public TextView setCompoundDrawableLeft(@IdRes int id, Drawable drawable) {
            return setCompoundDrawables(id, drawable, null, null, null);
        }

        /**
         * Set the composite drawing {@link Drawable} above the view
         * {@link #setCompoundDrawables(int, Drawable, Drawable, Drawable, Drawable)}
         *
         * @param id       view ID
         * @param drawable {@link Drawable}
         * @return {@link TextView}
         */
        public TextView setCompoundDrawableTop(@IdRes int id, Drawable drawable) {
            return setCompoundDrawables(id, null, drawable, null, null);
        }

        /**
         * Set the composite drawing {@link Drawable} to the right of the view
         * {@link #setCompoundDrawables(int, Drawable, Drawable, Drawable, Drawable)}
         *
         * @param id       view ID
         * @param drawable {@link Drawable}
         * @return {@link TextView}
         */
        public TextView setCompoundDrawableRight(@IdRes int id, Drawable drawable) {
            return setCompoundDrawables(id, null, null, drawable, null);
        }

        /**
         * Set the composite drawing {@link Drawable} below the view
         * {@link #setCompoundDrawables(int, Drawable, Drawable, Drawable, Drawable)}
         *
         * @param id       view ID
         * @param drawable {@link Drawable}
         * @return {@link TextView}
         */
        public TextView setCompoundDrawableBottom(@IdRes int id, Drawable drawable) {
            return setCompoundDrawables(id, null, null, null, drawable);
        }

        /**
         * Set the view's composite drawing {@link Drawable}
         * {@link TextView#setCompoundDrawables(Drawable, Drawable, Drawable, Drawable)}
         *
         * @param id     view ID
         * @param left   Drawable on the left
         * @param top    Drawable above
         * @param right  Drawable on the right
         * @param bottom Drawable below
         * @return {@link TextView}
         */
        public TextView setCompoundDrawables(@IdRes int id, Drawable left, Drawable top, Drawable right, Drawable bottom) {
            TextView tv = getView(id);
            tv.setCompoundDrawables(left, top, right, bottom);
            return tv;
        }

        /**
         * Set the view's fillable padding
         * {@link TextView#setCompoundDrawablePadding(int)}
         *
         * @param id      view ID
         * @param padding internal padding spacing
         * @return
         */
        public TextView setCompoundDrawablePadding(@IdRes int id, int padding) {
            TextView tv = getView(id);
            tv.setCompoundDrawablePadding(padding);
            return tv;
        }

        /**
         * Set the view's intrinsic composite drawing {@link Drawable}
         * {@link TextView#setCompoundDrawablesWithIntrinsicBounds(int, int, int, int)}
         *
         * @param id     view ID
         * @param left   Drawable on the left
         * @param top    Drawable above
         * @param right  Drawable on the right
         * @param bottom Drawable below
         * @return {@link TextView}
         */
        public TextView setCompoundDrawablesWithIntrinsicBounds(@IdRes int id, @Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
            TextView tv = getView(id);
            tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
            return tv;
        }

        /**
         * Set the text content
         * {@link TextView#setText(int)}
         *
         * @param id    view ID
         * @param resId string resource ID
         * @return {@link TextView}
         */
        public TextView setText(@IdRes int id, @StringRes int resId) {
            TextView tv = getView(id);
            tv.setText(resId);
            return tv;
        }

        /**
         * Set the text content
         * {@link TextView#setText(CharSequence)}
         *
         * @param id   view ID
         * @param text text
         * @return {@link TextView}
         */
        public TextView setText(@IdRes int id, CharSequence text) {
            TextView tv = getView(id);
            tv.setText(text);
            return tv;
        }

        /**
         * Set the font color
         * {@link TextView#setTextColor(int)}
         *
         * @param id    view ID
         * @param color color
         * @return {@link TextView}
         */
        public TextView setTextColor(@IdRes int id, int color) {
            TextView tv = getView(id);
            tv.setTextColor(color);
            return tv;
        }

        /**
         * Set the font color
         * {@link TextView#setTextColor(ColorStateList)}
         *
         * @param id     view ID
         * @param colors color state list
         * @return {@link TextView}
         */
        public TextView setTextColor(@IdRes int id, @NonNull ColorStateList colors) {
            TextView tv = getView(id);
            tv.setTextColor(colors);
            return tv;
        }

        /**
         * Set the font size
         * {@link TextView#setTextSize(float)}
         *
         * @param id   view ID
         * @param size font size (unit: sp)
         * @return {@link TextView}
         */
        public TextView setTextSize(@IdRes int id, float size) {
            return setTextSize(id, size);
        }

        /**
         * Set the font size
         * {@link TextView#setTextSize(int, float)}
         *
         * @param id   view ID
         * @param unit unit; it is recommended to use {@link TypedValue#COMPLEX_UNIT_SP}
         * @param size font size
         * @return {@link TextView}
         */
        public TextView setTextSize(@IdRes int id, int unit, float size) {
            TextView tv = getView(id);
            tv.setTextSize(unit, size);
            return tv;
        }

        /**
         * Set the font
         * {@link TextView#setTypeface(Typeface)}
         *
         * @param id view ID
         * @param tf font
         * @return {@link TextView}
         */
        public TextView setTypeface(@IdRes int id, @Nullable Typeface tf) {
            TextView tv = getView(id);
            tv.setTypeface(tf);
            return tv;
        }

        /**
         * Set the font
         * {@link TextView#setTypeface(Typeface, int)}
         *
         * @param id    view ID
         * @param tf    font
         * @param style font style
         * @return {@link TextView}
         */
        public TextView setTypeface(@IdRes int id, @Nullable Typeface tf, int style) {
            TextView tv = getView(id);
            tv.setTypeface(tf, style);
            return tv;
        }

        /**
         * Add a link
         * {@link #addLinks(int, int)}
         *
         * @param id view ID
         * @return {@link TextView}
         */
        public TextView addLinks(@IdRes int id) {
            return addLinks(id, Linkify.ALL);
        }

        /**
         * Add a link
         * {@link Linkify#addLinks(TextView, int)}
         *
         * @param id   view ID
         * @param mask connection mask; such as: {@link Linkify#ALL}
         * @return {@link TextView}
         */
        public TextView addLinks(@IdRes int id, int mask) {
            TextView tv = getView(id);
            Linkify.addLinks(tv, mask);
            return tv;
        }

        /**
         * Add a link
         * {@link Linkify#addLinks(TextView, Pattern, String)}
         *
         * @param id      view ID
         * @param pattern regular expression pattern
         * @param scheme  scheme
         * @return {@link TextView}
         */
        public TextView addLinks(@IdRes int id, @NonNull Pattern pattern, @Nullable String scheme) {
            TextView tv = getView(id);
            Linkify.addLinks(tv, pattern, scheme);
            return tv;
        }

        /**
         * Set the image according to the Drawable resource ID
         * {@link ImageView#setImageResource(int)}
         *
         * @param id    view ID
         * @param resId Drawable resource ID
         * @return {@link ImageView}
         */
        public ImageView setImageResource(@IdRes int id, @DrawableRes int resId) {
            ImageView iv = getView(id);
            iv.setImageResource(resId);
            return iv;
        }

        /**
         * Set the image according to the bitmap
         * {@link ImageView#setImageBitmap(Bitmap)}
         *
         * @param id     view ID
         * @param bitmap bitmap
         * @return {@link ImageView}
         */
        public ImageView setImageBitmap(@IdRes int id, Bitmap bitmap) {
            ImageView iv = getView(id);
            iv.setImageBitmap(bitmap);
            return iv;
        }

        /**
         * Set the image based on {@link Drawable}
         * {@link ImageView#setImageResource(int)}
         *
         * @param id       view ID
         * @param drawable {@link Drawable}
         * @return {@link ImageView}
         */
        public ImageView setImageDrawable(@IdRes int id, Drawable drawable) {
            ImageView iv = getView(id);
            iv.setImageDrawable(drawable);
            return iv;
        }

        /**
         * Set whether to select
         * {@link CompoundButton#setChecked(boolean)}
         *
         * @param id        view ID
         * @param isChecked whether it is checked
         * @return {@link CompoundButton}
         */
        public CompoundButton setChecked(@IdRes int id, boolean isChecked) {
            CompoundButton cb = getView(id);
            cb.setChecked(isChecked);
            return cb;
        }

        /**
         * Checked
         * {@link CompoundButton#isChecked()}
         *
         * @param id view ID
         * @return {@code true} or {@code false}
         */
        public boolean isChecked(@IdRes int id) {
            CompoundButton cb = getView(id);
            return cb.isChecked();
        }

        /**
         * Switch
         * {@link CompoundButton#toggle()}
         *
         * @param id view ID
         * @return {@link CompoundButton}
         */
        public CompoundButton toggle(@IdRes int id) {
            CompoundButton cb = getView(id);
            cb.toggle();
            return cb;
        }

        /**
         * Set the progress value
         * {@link ProgressBar#setProgress(int)}
         *
         * @param id       view ID
         * @param progress progress
         * @return {@link ProgressBar}
         */
        public ProgressBar setProgress(@IdRes int id, int progress) {
            ProgressBar progressBar = getView(id);
            progressBar.setProgress(progress);
            return progressBar;
        }

        /**
         * Set the maximum progress value
         * {@link ProgressBar#setMax(int)}
         *
         * @param id  view ID
         * @param max maximum progress value
         * @return {@link ProgressBar}
         */
        public ProgressBar setMax(@IdRes int id, int max) {
            ProgressBar progressBar = getView(id);
            progressBar.setMax(max);
            return progressBar;
        }

        /**
         * Set rating
         * {@link RatingBar#setRating(float)}
         *
         * @param id     view ID
         * @param rating Rating
         * @return {@link RatingBar}
         */
        public RatingBar setRating(@IdRes int id, float rating) {
            RatingBar ratingBar = getView(id);
            ratingBar.setRating(rating);
            return ratingBar;
        }

        /**
         * Set the rating and maximum rating value
         * {@link RatingBar#setRating(float)} and {@link RatingBar#setMax(int)}
         *
         * @param id     view ID
         * @param rating Rating
         * @param max    maximum score value
         * @return {@link RatingBar}
         */
        public RatingBar setRating(@IdRes int id, float rating, int max) {
            RatingBar ratingBar = getView(id);
            ratingBar.setRating(rating);
            ratingBar.setMax(max);
            return ratingBar;
        }

        /**
         * Set the number of stars
         * {@link RatingBar#setNumStars(int)}
         *
         * @param id       view ID
         * @param numStars number of stars
         * @return {@link RatingBar}
         */
        public RatingBar setNumStars(@IdRes int id, int numStars) {
            RatingBar ratingBar = getView(id);
            ratingBar.setNumStars(numStars);
            return ratingBar;
        }

        /**
         * Set whether to select
         * {@link View#setSelected(boolean)}
         *
         * @param id       view ID
         * @param selected whether to select
         * @return {@link View}
         */
        public View setSelected(@IdRes int id, boolean selected) {
            View view = getView(id);
            view.setSelected(selected);
            return view;
        }

        /**
         * Select
         * {@link View#isSelected()}
         *
         * @param id view ID
         * @return {@code true} or {@code false}
         */
        public boolean isSelected(@IdRes int id) {
            return getView(id).isSelected();
        }

        /**
         * Set whether to enable
         * {@link View#setEnabled(boolean)}
         *
         * @param id      view ID
         * @param enabled whether to enable
         * @return {@link View}
         */
        public View setEnabled(@IdRes int id, boolean enabled) {
            View view = getView(id);
            view.setEnabled(enabled);
            return view;
        }

        /**
         * Whether to enable
         * {@link View#isEnabled()}
         *
         * @param id view ID
         * @return {@code true} or {@code false}
         */
        public boolean isEnabled(@IdRes int id) {
            return getView(id).isEnabled();
        }


        //--------------------- Listen for events

        /**
         * Set click listener
         * {@link View#setOnClickListener(View.OnClickListener)}
         *
         * @param id              view ID
         * @param onClickListener {@link View.OnClickListener}
         */
        public void setOnClickListener(@IdRes int id, View.OnClickListener onClickListener) {
            getView(id).setOnClickListener(onClickListener);
        }

        /**
         * Set touch monitoring
         * {@link View#setOnTouchListener(View.OnTouchListener)}
         *
         * @param id              view ID
         * @param onTouchListener {@link View.OnTouchListener}
         */
        public void setOnTouchListener(@IdRes int id, View.OnTouchListener onTouchListener) {
            getView(id).setOnTouchListener(onTouchListener);
        }

        /**
         * Set long press monitoring
         * {@link View#setOnLongClickListener(View.OnLongClickListener)}
         *
         * @param id                  view ID
         * @param onLongClickListener {@link View.OnLongClickListener}
         */
        public void setOnLongClickListener(@IdRes int id, View.OnLongClickListener onLongClickListener) {
            getView(id).setOnLongClickListener(onLongClickListener);
        }

        /**
         * Set up key monitoring
         * {@link View#setOnKeyListener(View.OnKeyListener)}
         *
         * @param id            view ID
         * @param onKeyListener {@link View.OnKeyListener}
         */
        public void setOnKeyListener(@IdRes int id, View.OnKeyListener onKeyListener) {
            getView(id).setOnKeyListener(onKeyListener);
        }

    }

}