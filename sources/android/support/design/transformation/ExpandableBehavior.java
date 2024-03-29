package android.support.design.transformation;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.expandable.ExpandableWidget;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import java.util.List;

public abstract class ExpandableBehavior extends Behavior<View> {
    private static final int STATE_COLLAPSED = 2;
    private static final int STATE_EXPANDED = 1;
    private static final int STATE_UNINITIALIZED = 0;
    private int currentState = 0;

    public abstract boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2);

    protected abstract boolean onExpandedStateChange(View view, View view2, boolean z, boolean z2);

    public ExpandableBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @CallSuper
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, final View view, int i) {
        if (ViewCompat.isLaidOut(view) == 0) {
            coordinatorLayout = findExpandableWidget(coordinatorLayout, view);
            if (!(coordinatorLayout == null || didStateChange(coordinatorLayout.isExpanded()) == 0)) {
                this.currentState = coordinatorLayout.isExpanded() != 0 ? 1 : 2;
                i = this.currentState;
                view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                    public boolean onPreDraw() {
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (ExpandableBehavior.this.currentState == i) {
                            ExpandableBehavior.this.onExpandedStateChange((View) coordinatorLayout, view, coordinatorLayout.isExpanded(), false);
                        }
                        return false;
                    }
                });
            }
        }
        return null;
    }

    @CallSuper
    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
        ExpandableWidget expandableWidget = (ExpandableWidget) view2;
        if (didStateChange(expandableWidget.isExpanded()) == null) {
            return null;
        }
        this.currentState = expandableWidget.isExpanded() != null ? true : 2;
        return onExpandedStateChange((View) expandableWidget, view, expandableWidget.isExpanded(), true);
    }

    @Nullable
    protected ExpandableWidget findExpandableWidget(CoordinatorLayout coordinatorLayout, View view) {
        List dependencies = coordinatorLayout.getDependencies(view);
        int size = dependencies.size();
        for (int i = 0; i < size; i++) {
            View view2 = (View) dependencies.get(i);
            if (layoutDependsOn(coordinatorLayout, view, view2)) {
                return (ExpandableWidget) view2;
            }
        }
        return null;
    }

    private boolean didStateChange(boolean z) {
        boolean z2 = false;
        if (z) {
            if (!this.currentState || this.currentState) {
                z2 = true;
            }
            return z2;
        }
        if (this.currentState) {
            z2 = true;
        }
        return z2;
    }

    public static <T extends ExpandableBehavior> T from(View view, Class<T> cls) {
        view = view.getLayoutParams();
        if (view instanceof LayoutParams) {
            view = ((LayoutParams) view).getBehavior();
            if (view instanceof ExpandableBehavior) {
                return (ExpandableBehavior) cls.cast(view);
            }
            throw new IllegalArgumentException("The view is not associated with ExpandableBehavior");
        }
        throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
    }
}
