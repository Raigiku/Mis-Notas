package android.support.design.widget;

import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.AnimatorRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.VisibleForTesting;
import android.support.design.C0026R;
import android.support.design.animation.MotionSpec;
import android.support.design.expandable.ExpandableTransformationWidget;
import android.support.design.expandable.ExpandableWidgetHelper;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.internal.ViewUtils;
import android.support.design.resources.MaterialResources;
import android.support.design.stateful.ExtendableSavedState;
import android.support.design.widget.CoordinatorLayout.DefaultBehavior;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TintableImageSourceView;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatImageHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView.ScaleType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@DefaultBehavior(Behavior.class)
public class FloatingActionButton extends VisibilityAwareImageButton implements TintableBackgroundView, TintableImageSourceView, ExpandableTransformationWidget {
    private static final int AUTO_MINI_LARGEST_SCREEN_WIDTH = 470;
    private static final String EXPANDABLE_WIDGET_HELPER_KEY = "expandableWidgetHelper";
    private static final String LOG_TAG = "FloatingActionButton";
    public static final int NO_CUSTOM_SIZE = 0;
    public static final int SIZE_AUTO = -1;
    public static final int SIZE_MINI = 1;
    public static final int SIZE_NORMAL = 0;
    private ColorStateList backgroundTint;
    private Mode backgroundTintMode;
    private int borderWidth;
    boolean compatPadding;
    private int customSize;
    private final ExpandableWidgetHelper expandableWidgetHelper;
    private final AppCompatImageHelper imageHelper;
    @Nullable
    private Mode imageMode;
    private int imagePadding;
    @Nullable
    private ColorStateList imageTint;
    private FloatingActionButtonImpl impl;
    private int maxImageSize;
    private ColorStateList rippleColor;
    final Rect shadowPadding;
    private int size;
    private final Rect touchArea;

    public static abstract class OnVisibilityChangedListener {
        public void onHidden(FloatingActionButton floatingActionButton) {
        }

        public void onShown(FloatingActionButton floatingActionButton) {
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Size {
    }

    protected static class BaseBehavior<T extends FloatingActionButton> extends android.support.design.widget.CoordinatorLayout.Behavior<T> {
        private static final boolean AUTO_HIDE_DEFAULT = true;
        private boolean autoHideEnabled;
        private OnVisibilityChangedListener internalAutoHideListener;
        private Rect tmpRect;

        public BaseBehavior() {
            this.autoHideEnabled = AUTO_HIDE_DEFAULT;
        }

        public BaseBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, C0026R.styleable.FloatingActionButton_Behavior_Layout);
            this.autoHideEnabled = context.getBoolean(C0026R.styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide, AUTO_HIDE_DEFAULT);
            context.recycle();
        }

        public void setAutoHideEnabled(boolean z) {
            this.autoHideEnabled = z;
        }

        public boolean isAutoHideEnabled() {
            return this.autoHideEnabled;
        }

        public void onAttachedToLayoutParams(@NonNull LayoutParams layoutParams) {
            if (layoutParams.dodgeInsetEdges == 0) {
                layoutParams.dodgeInsetEdges = 80;
            }
        }

        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            if (view instanceof AppBarLayout) {
                updateFabVisibilityForAppBarLayout(coordinatorLayout, (AppBarLayout) view, floatingActionButton);
            } else if (isBottomSheet(view) != null) {
                updateFabVisibilityForBottomSheet(view, floatingActionButton);
            }
            return null;
        }

        private static boolean isBottomSheet(@NonNull View view) {
            view = view.getLayoutParams();
            return view instanceof LayoutParams ? ((LayoutParams) view).getBehavior() instanceof BottomSheetBehavior : null;
        }

        @VisibleForTesting
        public void setInternalAutoHideListener(OnVisibilityChangedListener onVisibilityChangedListener) {
            this.internalAutoHideListener = onVisibilityChangedListener;
        }

        private boolean shouldUpdateVisibility(View view, FloatingActionButton floatingActionButton) {
            LayoutParams layoutParams = (LayoutParams) floatingActionButton.getLayoutParams();
            if (this.autoHideEnabled && layoutParams.getAnchorId() == view.getId() && floatingActionButton.getUserSetVisibility() == null) {
                return AUTO_HIDE_DEFAULT;
            }
            return false;
        }

        private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, FloatingActionButton floatingActionButton) {
            if (!shouldUpdateVisibility(appBarLayout, floatingActionButton)) {
                return false;
            }
            if (this.tmpRect == null) {
                this.tmpRect = new Rect();
            }
            Rect rect = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect(coordinatorLayout, appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                floatingActionButton.hide(this.internalAutoHideListener, false);
            } else {
                floatingActionButton.show(this.internalAutoHideListener, false);
            }
            return AUTO_HIDE_DEFAULT;
        }

        private boolean updateFabVisibilityForBottomSheet(View view, FloatingActionButton floatingActionButton) {
            if (!shouldUpdateVisibility(view, floatingActionButton)) {
                return false;
            }
            if (view.getTop() < (floatingActionButton.getHeight() / 2) + ((LayoutParams) floatingActionButton.getLayoutParams()).topMargin) {
                floatingActionButton.hide(this.internalAutoHideListener, false);
            } else {
                floatingActionButton.show(this.internalAutoHideListener, false);
            }
            return AUTO_HIDE_DEFAULT;
        }

        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, int i) {
            List dependencies = coordinatorLayout.getDependencies(floatingActionButton);
            int size = dependencies.size();
            for (int i2 = 0; i2 < size; i2++) {
                View view = (View) dependencies.get(i2);
                if (!(view instanceof AppBarLayout)) {
                    if (isBottomSheet(view) && updateFabVisibilityForBottomSheet(view, floatingActionButton)) {
                        break;
                    }
                } else if (updateFabVisibilityForAppBarLayout(coordinatorLayout, (AppBarLayout) view, floatingActionButton)) {
                    break;
                }
            }
            coordinatorLayout.onLayoutChild(floatingActionButton, i);
            offsetIfNeeded(coordinatorLayout, floatingActionButton);
            return AUTO_HIDE_DEFAULT;
        }

        public boolean getInsetDodgeRect(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton floatingActionButton, @NonNull Rect rect) {
            coordinatorLayout = floatingActionButton.shadowPadding;
            rect.set(floatingActionButton.getLeft() + coordinatorLayout.left, floatingActionButton.getTop() + coordinatorLayout.top, floatingActionButton.getRight() - coordinatorLayout.right, floatingActionButton.getBottom() - coordinatorLayout.bottom);
            return AUTO_HIDE_DEFAULT;
        }

        private void offsetIfNeeded(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton) {
            Rect rect = floatingActionButton.shadowPadding;
            if (rect != null && rect.centerX() > 0 && rect.centerY() > 0) {
                LayoutParams layoutParams = (LayoutParams) floatingActionButton.getLayoutParams();
                int i = 0;
                int i2 = floatingActionButton.getRight() >= coordinatorLayout.getWidth() - layoutParams.rightMargin ? rect.right : floatingActionButton.getLeft() <= layoutParams.leftMargin ? -rect.left : 0;
                if (floatingActionButton.getBottom() >= coordinatorLayout.getHeight() - layoutParams.bottomMargin) {
                    i = rect.bottom;
                } else if (floatingActionButton.getTop() <= layoutParams.topMargin) {
                    i = -rect.top;
                }
                if (i != 0) {
                    ViewCompat.offsetTopAndBottom(floatingActionButton, i);
                }
                if (i2 != 0) {
                    ViewCompat.offsetLeftAndRight(floatingActionButton, i2);
                }
            }
        }
    }

    private class ShadowDelegateImpl implements ShadowViewDelegate {
        ShadowDelegateImpl() {
        }

        public float getRadius() {
            return ((float) FloatingActionButton.this.getSizeDimension()) / 2.0f;
        }

        public void setShadowPadding(int i, int i2, int i3, int i4) {
            FloatingActionButton.this.shadowPadding.set(i, i2, i3, i4);
            FloatingActionButton.this.setPadding(i + FloatingActionButton.this.imagePadding, i2 + FloatingActionButton.this.imagePadding, i3 + FloatingActionButton.this.imagePadding, i4 + FloatingActionButton.this.imagePadding);
        }

        public void setBackgroundDrawable(Drawable drawable) {
            super.setBackgroundDrawable(drawable);
        }

        public boolean isCompatPaddingEnabled() {
            return FloatingActionButton.this.compatPadding;
        }
    }

    public static class Behavior extends BaseBehavior<FloatingActionButton> {
        public /* bridge */ /* synthetic */ boolean getInsetDodgeRect(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton floatingActionButton, @NonNull Rect rect) {
            return super.getInsetDodgeRect(coordinatorLayout, floatingActionButton, rect);
        }

        public /* bridge */ /* synthetic */ boolean isAutoHideEnabled() {
            return super.isAutoHideEnabled();
        }

        public /* bridge */ /* synthetic */ void onAttachedToLayoutParams(@NonNull LayoutParams layoutParams) {
            super.onAttachedToLayoutParams(layoutParams);
        }

        public /* bridge */ /* synthetic */ boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            return super.onDependentViewChanged(coordinatorLayout, floatingActionButton, view);
        }

        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, int i) {
            return super.onLayoutChild(coordinatorLayout, floatingActionButton, i);
        }

        public /* bridge */ /* synthetic */ void setAutoHideEnabled(boolean z) {
            super.setAutoHideEnabled(z);
        }

        @VisibleForTesting
        public /* bridge */ /* synthetic */ void setInternalAutoHideListener(OnVisibilityChangedListener onVisibilityChangedListener) {
            super.setInternalAutoHideListener(onVisibilityChangedListener);
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }
    }

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0026R.attr.floatingActionButtonStyle);
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.shadowPadding = new Rect();
        this.touchArea = new Rect();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, C0026R.styleable.FloatingActionButton, i, C0026R.style.Widget_Design_FloatingActionButton, new int[0]);
        this.backgroundTint = MaterialResources.getColorStateList(context, obtainStyledAttributes, C0026R.styleable.FloatingActionButton_backgroundTint);
        this.backgroundTintMode = ViewUtils.parseTintMode(obtainStyledAttributes.getInt(C0026R.styleable.FloatingActionButton_backgroundTintMode, -1), null);
        this.rippleColor = MaterialResources.getColorStateList(context, obtainStyledAttributes, C0026R.styleable.FloatingActionButton_rippleColor);
        this.size = obtainStyledAttributes.getInt(C0026R.styleable.FloatingActionButton_fabSize, -1);
        this.customSize = obtainStyledAttributes.getDimensionPixelSize(C0026R.styleable.FloatingActionButton_fabCustomSize, 0);
        this.borderWidth = obtainStyledAttributes.getDimensionPixelSize(C0026R.styleable.FloatingActionButton_borderWidth, 0);
        float dimension = obtainStyledAttributes.getDimension(C0026R.styleable.FloatingActionButton_elevation, 0.0f);
        float dimension2 = obtainStyledAttributes.getDimension(C0026R.styleable.FloatingActionButton_hoveredFocusedTranslationZ, 0.0f);
        float dimension3 = obtainStyledAttributes.getDimension(C0026R.styleable.FloatingActionButton_pressedTranslationZ, 0.0f);
        this.compatPadding = obtainStyledAttributes.getBoolean(C0026R.styleable.FloatingActionButton_useCompatPadding, false);
        this.maxImageSize = obtainStyledAttributes.getDimensionPixelSize(C0026R.styleable.FloatingActionButton_maxImageSize, 0);
        MotionSpec createFromAttribute = MotionSpec.createFromAttribute(context, obtainStyledAttributes, C0026R.styleable.FloatingActionButton_showMotionSpec);
        context = MotionSpec.createFromAttribute(context, obtainStyledAttributes, C0026R.styleable.FloatingActionButton_hideMotionSpec);
        obtainStyledAttributes.recycle();
        this.imageHelper = new AppCompatImageHelper(this);
        this.imageHelper.loadFromAttributes(attributeSet, i);
        this.expandableWidgetHelper = new ExpandableWidgetHelper(this);
        getImpl().setBackgroundDrawable(this.backgroundTint, this.backgroundTintMode, this.rippleColor, this.borderWidth);
        getImpl().setElevation(dimension);
        getImpl().setHoveredFocusedTranslationZ(dimension2);
        getImpl().setPressedTranslationZ(dimension3);
        getImpl().setMaxImageSize(this.maxImageSize);
        getImpl().setShowMotionSpec(createFromAttribute);
        getImpl().setHideMotionSpec(context);
        setScaleType(ScaleType.MATRIX);
    }

    protected void onMeasure(int i, int i2) {
        int sizeDimension = getSizeDimension();
        this.imagePadding = (sizeDimension - this.maxImageSize) / 2;
        getImpl().updatePadding();
        i = Math.min(resolveAdjustedSize(sizeDimension, i), resolveAdjustedSize(sizeDimension, i2));
        setMeasuredDimension((this.shadowPadding.left + i) + this.shadowPadding.right, (i + this.shadowPadding.top) + this.shadowPadding.bottom);
    }

    @ColorInt
    @Deprecated
    public int getRippleColor() {
        return this.rippleColor != null ? this.rippleColor.getDefaultColor() : 0;
    }

    @Nullable
    public ColorStateList getRippleColorStateList() {
        return this.rippleColor;
    }

    public void setRippleColor(@ColorInt int i) {
        setRippleColor(ColorStateList.valueOf(i));
    }

    public void setRippleColor(@Nullable ColorStateList colorStateList) {
        if (this.rippleColor != colorStateList) {
            this.rippleColor = colorStateList;
            getImpl().setRippleColor(this.rippleColor);
        }
    }

    @Nullable
    public ColorStateList getBackgroundTintList() {
        return this.backgroundTint;
    }

    public void setBackgroundTintList(@Nullable ColorStateList colorStateList) {
        if (this.backgroundTint != colorStateList) {
            this.backgroundTint = colorStateList;
            getImpl().setBackgroundTintList(colorStateList);
        }
    }

    @Nullable
    public Mode getBackgroundTintMode() {
        return this.backgroundTintMode;
    }

    public void setBackgroundTintMode(@Nullable Mode mode) {
        if (this.backgroundTintMode != mode) {
            this.backgroundTintMode = mode;
            getImpl().setBackgroundTintMode(mode);
        }
    }

    public void setSupportBackgroundTintList(@Nullable ColorStateList colorStateList) {
        setBackgroundTintList(colorStateList);
    }

    @Nullable
    public ColorStateList getSupportBackgroundTintList() {
        return getBackgroundTintList();
    }

    public void setSupportBackgroundTintMode(@Nullable Mode mode) {
        setBackgroundTintMode(mode);
    }

    @Nullable
    public Mode getSupportBackgroundTintMode() {
        return getBackgroundTintMode();
    }

    public void setSupportImageTintList(@Nullable ColorStateList colorStateList) {
        if (this.imageTint != colorStateList) {
            this.imageTint = colorStateList;
            onApplySupportImageTint();
        }
    }

    @Nullable
    public ColorStateList getSupportImageTintList() {
        return this.imageTint;
    }

    public void setSupportImageTintMode(@Nullable Mode mode) {
        if (this.imageMode != mode) {
            this.imageMode = mode;
            onApplySupportImageTint();
        }
    }

    @Nullable
    public Mode getSupportImageTintMode() {
        return this.imageMode;
    }

    private void onApplySupportImageTint() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            if (this.imageTint == null) {
                DrawableCompat.clearColorFilter(drawable);
                return;
            }
            int colorForState = this.imageTint.getColorForState(getDrawableState(), 0);
            Mode mode = this.imageMode;
            if (mode == null) {
                mode = Mode.SRC_IN;
            }
            drawable.mutate().setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(colorForState, mode));
        }
    }

    public void setBackgroundDrawable(Drawable drawable) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.");
    }

    public void setBackgroundResource(int i) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.");
    }

    public void setBackgroundColor(int i) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.");
    }

    public void setImageResource(@DrawableRes int i) {
        this.imageHelper.setImageResource(i);
    }

    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        getImpl().updateImageMatrixScale();
    }

    public void show() {
        show(null);
    }

    public void show(@Nullable OnVisibilityChangedListener onVisibilityChangedListener) {
        show(onVisibilityChangedListener, true);
    }

    void show(OnVisibilityChangedListener onVisibilityChangedListener, boolean z) {
        getImpl().show(wrapOnVisibilityChangedListener(onVisibilityChangedListener), z);
    }

    public void addOnShowAnimationListener(@NonNull AnimatorListener animatorListener) {
        getImpl().addOnShowAnimationListener(animatorListener);
    }

    public void removeOnShowAnimationListener(@NonNull AnimatorListener animatorListener) {
        getImpl().removeOnShowAnimationListener(animatorListener);
    }

    public void hide() {
        hide(null);
    }

    public void hide(@Nullable OnVisibilityChangedListener onVisibilityChangedListener) {
        hide(onVisibilityChangedListener, true);
    }

    void hide(@Nullable OnVisibilityChangedListener onVisibilityChangedListener, boolean z) {
        getImpl().hide(wrapOnVisibilityChangedListener(onVisibilityChangedListener), z);
    }

    public void addOnHideAnimationListener(@NonNull AnimatorListener animatorListener) {
        getImpl().addOnHideAnimationListener(animatorListener);
    }

    public void removeOnHideAnimationListener(@NonNull AnimatorListener animatorListener) {
        getImpl().removeOnHideAnimationListener(animatorListener);
    }

    public boolean setExpanded(boolean z) {
        return this.expandableWidgetHelper.setExpanded(z);
    }

    public boolean isExpanded() {
        return this.expandableWidgetHelper.isExpanded();
    }

    public void setExpandedComponentIdHint(@IdRes int i) {
        this.expandableWidgetHelper.setExpandedComponentIdHint(i);
    }

    public int getExpandedComponentIdHint() {
        return this.expandableWidgetHelper.getExpandedComponentIdHint();
    }

    public void setUseCompatPadding(boolean z) {
        if (this.compatPadding != z) {
            this.compatPadding = z;
            getImpl().onCompatShadowChanged();
        }
    }

    public boolean getUseCompatPadding() {
        return this.compatPadding;
    }

    public void setSize(int i) {
        this.customSize = 0;
        if (i != this.size) {
            this.size = i;
            requestLayout();
        }
    }

    public int getSize() {
        return this.size;
    }

    @Nullable
    private InternalVisibilityChangedListener wrapOnVisibilityChangedListener(@Nullable final OnVisibilityChangedListener onVisibilityChangedListener) {
        return onVisibilityChangedListener == null ? null : new InternalVisibilityChangedListener() {
            public void onShown() {
                onVisibilityChangedListener.onShown(FloatingActionButton.this);
            }

            public void onHidden() {
                onVisibilityChangedListener.onHidden(FloatingActionButton.this);
            }
        };
    }

    public boolean isOrWillBeHidden() {
        return getImpl().isOrWillBeHidden();
    }

    public boolean isOrWillBeShown() {
        return getImpl().isOrWillBeShown();
    }

    public void setCustomSize(@Px int i) {
        if (i >= 0) {
            this.customSize = i;
            return;
        }
        throw new IllegalArgumentException("Custom size must be non-negative");
    }

    @Px
    public int getCustomSize() {
        return this.customSize;
    }

    public void clearCustomSize() {
        setCustomSize(0);
    }

    int getSizeDimension() {
        return getSizeDimension(this.size);
    }

    private int getSizeDimension(int i) {
        if (this.customSize != 0) {
            return this.customSize;
        }
        Resources resources = getResources();
        if (i == -1) {
            if (Math.max(resources.getConfiguration().screenWidthDp, resources.getConfiguration().screenHeightDp) < AUTO_MINI_LARGEST_SCREEN_WIDTH) {
                i = getSizeDimension(1);
            } else {
                i = getSizeDimension(0);
            }
            return i;
        } else if (i != 1) {
            return resources.getDimensionPixelSize(C0026R.dimen.design_fab_size_normal);
        } else {
            return resources.getDimensionPixelSize(C0026R.dimen.design_fab_size_mini);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getImpl().onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getImpl().onDetachedFromWindow();
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        getImpl().onDrawableStateChanged(getDrawableState());
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        getImpl().jumpDrawableToCurrentState();
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable extendableSavedState = new ExtendableSavedState(super.onSaveInstanceState());
        extendableSavedState.extendableStates.put(EXPANDABLE_WIDGET_HELPER_KEY, this.expandableWidgetHelper.onSaveInstanceState());
        return extendableSavedState;
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof ExtendableSavedState) {
            ExtendableSavedState extendableSavedState = (ExtendableSavedState) parcelable;
            super.onRestoreInstanceState(extendableSavedState.getSuperState());
            this.expandableWidgetHelper.onRestoreInstanceState((Bundle) extendableSavedState.extendableStates.get(EXPANDABLE_WIDGET_HELPER_KEY));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Deprecated
    public boolean getContentRect(@NonNull Rect rect) {
        if (!ViewCompat.isLaidOut(this)) {
            return false;
        }
        rect.set(0, 0, getWidth(), getHeight());
        offsetRectWithShadow(rect);
        return true;
    }

    public void getMeasuredContentRect(@NonNull Rect rect) {
        rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        offsetRectWithShadow(rect);
    }

    private void offsetRectWithShadow(@NonNull Rect rect) {
        rect.left += this.shadowPadding.left;
        rect.top += this.shadowPadding.top;
        rect.right -= this.shadowPadding.right;
        rect.bottom -= this.shadowPadding.bottom;
    }

    @NonNull
    public Drawable getContentBackground() {
        return getImpl().getContentBackground();
    }

    private static int resolveAdjustedSize(int i, int i2) {
        int mode = MeasureSpec.getMode(i2);
        i2 = MeasureSpec.getSize(i2);
        if (mode == Integer.MIN_VALUE) {
            return Math.min(i, i2);
        }
        if (mode == 0) {
            return i;
        }
        if (mode == 1073741824) {
            return i2;
        }
        throw new IllegalArgumentException();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && getContentRect(this.touchArea) && !this.touchArea.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            return null;
        }
        return super.onTouchEvent(motionEvent);
    }

    public float getCompatElevation() {
        return getImpl().getElevation();
    }

    public void setCompatElevation(float f) {
        getImpl().setElevation(f);
    }

    public void setCompatElevationResource(@DimenRes int i) {
        setCompatElevation(getResources().getDimension(i));
    }

    public float getCompatHoveredFocusedTranslationZ() {
        return getImpl().getHoveredFocusedTranslationZ();
    }

    public void setCompatHoveredFocusedTranslationZ(float f) {
        getImpl().setHoveredFocusedTranslationZ(f);
    }

    public void setCompatHoveredFocusedTranslationZResource(@DimenRes int i) {
        setCompatHoveredFocusedTranslationZ(getResources().getDimension(i));
    }

    public float getCompatPressedTranslationZ() {
        return getImpl().getPressedTranslationZ();
    }

    public void setCompatPressedTranslationZ(float f) {
        getImpl().setPressedTranslationZ(f);
    }

    public void setCompatPressedTranslationZResource(@DimenRes int i) {
        setCompatPressedTranslationZ(getResources().getDimension(i));
    }

    public MotionSpec getShowMotionSpec() {
        return getImpl().getShowMotionSpec();
    }

    public void setShowMotionSpec(MotionSpec motionSpec) {
        getImpl().setShowMotionSpec(motionSpec);
    }

    public void setShowMotionSpecResource(@AnimatorRes int i) {
        setShowMotionSpec(MotionSpec.createFromResource(getContext(), i));
    }

    public MotionSpec getHideMotionSpec() {
        return getImpl().getHideMotionSpec();
    }

    public void setHideMotionSpec(MotionSpec motionSpec) {
        getImpl().setHideMotionSpec(motionSpec);
    }

    public void setHideMotionSpecResource(@AnimatorRes int i) {
        setHideMotionSpec(MotionSpec.createFromResource(getContext(), i));
    }

    private FloatingActionButtonImpl getImpl() {
        if (this.impl == null) {
            this.impl = createImpl();
        }
        return this.impl;
    }

    private FloatingActionButtonImpl createImpl() {
        if (VERSION.SDK_INT >= 21) {
            return new FloatingActionButtonImplLollipop(this, new ShadowDelegateImpl());
        }
        return new FloatingActionButtonImpl(this, new ShadowDelegateImpl());
    }
}
