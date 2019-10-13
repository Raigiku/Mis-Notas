package android.support.design.widget;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.support.design.C0026R;
import android.support.design.internal.ThemeEnforcement;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

@RequiresApi(21)
class ViewUtilsLollipop {
    private static final int[] STATE_LIST_ANIM_ATTRS = new int[]{16843848};

    ViewUtilsLollipop() {
    }

    static void setBoundsViewOutlineProvider(View view) {
        view.setOutlineProvider(ViewOutlineProvider.BOUNDS);
    }

    static void setStateListAnimatorFromAttrs(View view, AttributeSet attributeSet, int i, int i2) {
        Context context = view.getContext();
        attributeSet = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, STATE_LIST_ANIM_ATTRS, i, i2, new int[0]);
        try {
            if (attributeSet.hasValue(0) != 0) {
                view.setStateListAnimator(AnimatorInflater.loadStateListAnimator(context, attributeSet.getResourceId(0, 0)));
            }
            attributeSet.recycle();
        } catch (Throwable th) {
            attributeSet.recycle();
        }
    }

    static void setDefaultAppBarLayoutStateListAnimator(View view, float f) {
        int integer = view.getResources().getInteger(C0026R.integer.app_bar_elevation_anim_duration);
        StateListAnimator stateListAnimator = new StateListAnimator();
        long j = (long) integer;
        stateListAnimator.addState(new int[]{16842766, C0026R.attr.state_liftable, -C0026R.attr.state_lifted}, ObjectAnimator.ofFloat(view, "elevation", new float[]{0.0f}).setDuration(j));
        stateListAnimator.addState(new int[]{16842766}, ObjectAnimator.ofFloat(view, "elevation", new float[]{f}).setDuration(j));
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(view, "elevation", new float[]{0.0f}).setDuration(0));
        view.setStateListAnimator(stateListAnimator);
    }
}
