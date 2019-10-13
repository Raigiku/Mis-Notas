package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.ripple.RippleUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import java.util.ArrayList;

@RequiresApi(21)
class FloatingActionButtonImplLollipop extends FloatingActionButtonImpl {
    private InsetDrawable insetDrawable;

    static class AlwaysStatefulGradientDrawable extends GradientDrawable {
        public boolean isStateful() {
            return true;
        }

        AlwaysStatefulGradientDrawable() {
        }
    }

    void jumpDrawableToCurrentState() {
    }

    boolean requirePreDrawListener() {
        return false;
    }

    FloatingActionButtonImplLollipop(VisibilityAwareImageButton visibilityAwareImageButton, ShadowViewDelegate shadowViewDelegate) {
        super(visibilityAwareImageButton, shadowViewDelegate);
    }

    void setBackgroundDrawable(ColorStateList colorStateList, Mode mode, ColorStateList colorStateList2, int i) {
        this.shapeDrawable = DrawableCompat.wrap(createShapeDrawable());
        DrawableCompat.setTintList(this.shapeDrawable, colorStateList);
        if (mode != null) {
            DrawableCompat.setTintMode(this.shapeDrawable, mode);
        }
        if (i > 0) {
            this.borderDrawable = createBorderDrawable(i, colorStateList);
            colorStateList = new LayerDrawable(new Drawable[]{this.borderDrawable, this.shapeDrawable});
        } else {
            this.borderDrawable = null;
            colorStateList = this.shapeDrawable;
        }
        this.rippleDrawable = new RippleDrawable(RippleUtils.convertToRippleDrawableColor(colorStateList2), colorStateList, null);
        this.contentBackground = this.rippleDrawable;
        this.shadowViewDelegate.setBackgroundDrawable(this.rippleDrawable);
    }

    void setRippleColor(ColorStateList colorStateList) {
        if (this.rippleDrawable instanceof RippleDrawable) {
            ((RippleDrawable) this.rippleDrawable).setColor(RippleUtils.convertToRippleDrawableColor(colorStateList));
        } else {
            super.setRippleColor(colorStateList);
        }
    }

    void onElevationsChanged(float f, float f2, float f3) {
        if (VERSION.SDK_INT == 21) {
            this.view.refreshDrawableState();
        } else {
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, createElevationAnimator(f, f3));
            stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, createElevationAnimator(f, f2));
            stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, createElevationAnimator(f, f2));
            stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, createElevationAnimator(f, f2));
            f2 = new AnimatorSet();
            f3 = new ArrayList();
            f3.add(ObjectAnimator.ofFloat(this.view, "elevation", new float[]{f}).setDuration(0));
            if (VERSION.SDK_INT >= 22 && VERSION.SDK_INT <= 24) {
                f3.add(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[]{this.view.getTranslationZ()}).setDuration(100));
            }
            f3.add(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[]{0.0f}).setDuration(100));
            f2.playSequentially((Animator[]) f3.toArray(new Animator[0]));
            f2.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
            stateListAnimator.addState(ENABLED_STATE_SET, f2);
            stateListAnimator.addState(EMPTY_STATE_SET, createElevationAnimator(0.0f, 0.0f));
            this.view.setStateListAnimator(stateListAnimator);
        }
        if (this.shadowViewDelegate.isCompatPaddingEnabled() != null) {
            updatePadding();
        }
    }

    @NonNull
    private Animator createElevationAnimator(float f, float f2) {
        Animator animatorSet = new AnimatorSet();
        animatorSet.play(ObjectAnimator.ofFloat(this.view, "elevation", new float[]{f}).setDuration(0)).with(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[]{f2}).setDuration(100));
        animatorSet.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
        return animatorSet;
    }

    public float getElevation() {
        return this.view.getElevation();
    }

    void onCompatShadowChanged() {
        updatePadding();
    }

    void onPaddingUpdated(Rect rect) {
        if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
            this.insetDrawable = new InsetDrawable(this.rippleDrawable, rect.left, rect.top, rect.right, rect.bottom);
            this.shadowViewDelegate.setBackgroundDrawable(this.insetDrawable);
            return;
        }
        this.shadowViewDelegate.setBackgroundDrawable(this.rippleDrawable);
    }

    void onDrawableStateChanged(int[] iArr) {
        if (VERSION.SDK_INT != 21) {
            return;
        }
        if (this.view.isEnabled() != null) {
            this.view.setElevation(this.elevation);
            if (this.view.isPressed() != null) {
                this.view.setTranslationZ(this.pressedTranslationZ);
                return;
            }
            if (this.view.isFocused() == null) {
                if (this.view.isHovered() == null) {
                    this.view.setTranslationZ(0.0f);
                    return;
                }
            }
            this.view.setTranslationZ(this.hoveredFocusedTranslationZ);
            return;
        }
        this.view.setElevation(0.0f);
        this.view.setTranslationZ(0.0f);
    }

    CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawableLollipop();
    }

    GradientDrawable newGradientDrawableForShape() {
        return new AlwaysStatefulGradientDrawable();
    }

    void getPadding(Rect rect) {
        if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
            float radius = this.shadowViewDelegate.getRadius();
            float elevation = getElevation() + this.pressedTranslationZ;
            int ceil = (int) Math.ceil((double) ShadowDrawableWrapper.calculateHorizontalPadding(elevation, radius, false));
            int ceil2 = (int) Math.ceil((double) ShadowDrawableWrapper.calculateVerticalPadding(elevation, radius, false));
            rect.set(ceil, ceil2, ceil, ceil2);
            return;
        }
        rect.set(0, 0, 0, 0);
    }
}
