package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.StringRes;
import android.support.design.C0026R;
import android.support.design.snackbar.ContentViewCallback;
import android.support.design.widget.BaseTransientBottomBar.BaseCallback;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Snackbar extends BaseTransientBottomBar<Snackbar> {
    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    private static final int[] SNACKBAR_BUTTON_STYLE_ATTR = new int[]{C0026R.attr.snackbarButtonStyle};
    private final AccessibilityManager accessibilityManager;
    @Nullable
    private BaseCallback<Snackbar> callback;
    private boolean hasAction;

    @IntRange(from = 1)
    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    public static class Callback extends BaseCallback<Snackbar> {
        public static final int DISMISS_EVENT_ACTION = 1;
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;
        public static final int DISMISS_EVENT_MANUAL = 3;
        public static final int DISMISS_EVENT_SWIPE = 0;
        public static final int DISMISS_EVENT_TIMEOUT = 2;

        public void onDismissed(Snackbar snackbar, int i) {
        }

        public void onShown(Snackbar snackbar) {
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static final class SnackbarLayout extends SnackbarBaseLayout {
        public SnackbarLayout(Context context) {
            super(context);
        }

        public SnackbarLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            i = getChildCount();
            i2 = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
            for (int i3 = 0; i3 < i; i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getLayoutParams().width == -1) {
                    childAt.measure(MeasureSpec.makeMeasureSpec(i2, 1073741824), MeasureSpec.makeMeasureSpec(childAt.getMeasuredHeight(), 1073741824));
                }
            }
        }
    }

    private Snackbar(ViewGroup viewGroup, View view, ContentViewCallback contentViewCallback) {
        super(viewGroup, view, contentViewCallback);
        this.accessibilityManager = (AccessibilityManager) viewGroup.getContext().getSystemService("accessibility");
    }

    public void show() {
        super.show();
    }

    public void dismiss() {
        super.dismiss();
    }

    public boolean isShown() {
        return super.isShown();
    }

    @NonNull
    public static Snackbar make(@NonNull View view, @NonNull CharSequence charSequence, int i) {
        view = findSuitableParent(view);
        if (view != null) {
            SnackbarContentLayout snackbarContentLayout = (SnackbarContentLayout) LayoutInflater.from(view.getContext()).inflate(hasSnackbarButtonStyleAttr(view.getContext()) ? C0026R.layout.mtrl_layout_snackbar_include : C0026R.layout.design_layout_snackbar_include, view, false);
            Snackbar snackbar = new Snackbar(view, snackbarContentLayout, snackbarContentLayout);
            snackbar.setText(charSequence);
            snackbar.setDuration(i);
            return snackbar;
        }
        throw new IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.");
    }

    protected static boolean hasSnackbarButtonStyleAttr(Context context) {
        context = context.obtainStyledAttributes(SNACKBAR_BUTTON_STYLE_ATTR);
        int resourceId = context.getResourceId(0, -1);
        context.recycle();
        if (resourceId != -1) {
            return true;
        }
        return false;
    }

    @NonNull
    public static Snackbar make(@NonNull View view, @StringRes int i, int i2) {
        return make(view, view.getResources().getText(i), i2);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup viewGroup = null;
        while (!(view instanceof CoordinatorLayout)) {
            if (view instanceof FrameLayout) {
                if (view.getId() == 16908290) {
                    return (ViewGroup) view;
                }
                viewGroup = (ViewGroup) view;
            }
            if (view != null) {
                view = view.getParent();
                if (view instanceof View) {
                    view = view;
                    continue;
                } else {
                    view = null;
                    continue;
                }
            }
            if (view == null) {
                return viewGroup;
            }
        }
        return (ViewGroup) view;
    }

    @NonNull
    public Snackbar setText(@NonNull CharSequence charSequence) {
        ((SnackbarContentLayout) this.view.getChildAt(0)).getMessageView().setText(charSequence);
        return this;
    }

    @NonNull
    public Snackbar setText(@StringRes int i) {
        return setText(getContext().getText(i));
    }

    @NonNull
    public Snackbar setAction(@StringRes int i, OnClickListener onClickListener) {
        return setAction(getContext().getText(i), onClickListener);
    }

    @NonNull
    public Snackbar setAction(CharSequence charSequence, final OnClickListener onClickListener) {
        TextView actionView = ((SnackbarContentLayout) this.view.getChildAt(0)).getActionView();
        if (!TextUtils.isEmpty(charSequence)) {
            if (onClickListener != null) {
                this.hasAction = true;
                actionView.setVisibility(0);
                actionView.setText(charSequence);
                actionView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        onClickListener.onClick(view);
                        Snackbar.this.dispatchDismiss(1);
                    }
                });
                return this;
            }
        }
        actionView.setVisibility(8);
        actionView.setOnClickListener(null);
        this.hasAction = false;
        return this;
    }

    public int getDuration() {
        if (this.hasAction && this.accessibilityManager.isTouchExplorationEnabled()) {
            return -2;
        }
        return super.getDuration();
    }

    @NonNull
    public Snackbar setActionTextColor(ColorStateList colorStateList) {
        ((SnackbarContentLayout) this.view.getChildAt(0)).getActionView().setTextColor(colorStateList);
        return this;
    }

    @NonNull
    public Snackbar setActionTextColor(@ColorInt int i) {
        ((SnackbarContentLayout) this.view.getChildAt(0)).getActionView().setTextColor(i);
        return this;
    }

    @Deprecated
    @NonNull
    public Snackbar setCallback(Callback callback) {
        if (this.callback != null) {
            removeCallback(this.callback);
        }
        if (callback != null) {
            addCallback(callback);
        }
        this.callback = callback;
        return this;
    }
}
