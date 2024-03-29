package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.StyleRes;
import android.support.design.C0026R;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.util.Pools.Pool;
import android.support.v4.util.Pools.SynchronizedPool;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.appcompat.C0228R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

@RestrictTo({Scope.LIBRARY_GROUP})
public class BottomNavigationMenuView extends ViewGroup implements MenuView {
    private static final long ACTIVE_ANIMATION_DURATION_MS = 115;
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private static final int[] DISABLED_STATE_SET = new int[]{-16842910};
    private final int activeItemMaxWidth;
    private final int activeItemMinWidth;
    private BottomNavigationItemView[] buttons;
    private final int inactiveItemMaxWidth;
    private final int inactiveItemMinWidth;
    private Drawable itemBackground;
    private int itemBackgroundRes;
    private final int itemHeight;
    private boolean itemHorizontalTranslationEnabled;
    @Dimension
    private int itemIconSize;
    private ColorStateList itemIconTint;
    private final Pool<BottomNavigationItemView> itemPool;
    @StyleRes
    private int itemTextAppearanceActive;
    @StyleRes
    private int itemTextAppearanceInactive;
    private final ColorStateList itemTextColorDefault;
    private ColorStateList itemTextColorFromUser;
    private int labelVisibilityMode;
    private MenuBuilder menu;
    private final OnClickListener onClickListener;
    private BottomNavigationPresenter presenter;
    private int selectedItemId;
    private int selectedItemPosition;
    private final TransitionSet set;
    private int[] tempChildWidths;

    /* renamed from: android.support.design.internal.BottomNavigationMenuView$1 */
    class C00401 implements OnClickListener {
        C00401() {
        }

        public void onClick(View view) {
            view = ((BottomNavigationItemView) view).getItemData();
            if (!BottomNavigationMenuView.this.menu.performItemAction(view, BottomNavigationMenuView.this.presenter, 0)) {
                view.setChecked(true);
            }
        }
    }

    private boolean isShifting(int i, int i2) {
        if (i == -1) {
            if (i2 <= 3) {
                return false;
            }
        } else if (i != 0) {
            return false;
        }
        return true;
    }

    public int getWindowAnimations() {
        return 0;
    }

    public BottomNavigationMenuView(Context context) {
        this(context, null);
    }

    public BottomNavigationMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.itemPool = new SynchronizedPool(5);
        this.selectedItemId = 0;
        this.selectedItemPosition = 0;
        Resources resources = getResources();
        this.inactiveItemMaxWidth = resources.getDimensionPixelSize(C0026R.dimen.design_bottom_navigation_item_max_width);
        this.inactiveItemMinWidth = resources.getDimensionPixelSize(C0026R.dimen.design_bottom_navigation_item_min_width);
        this.activeItemMaxWidth = resources.getDimensionPixelSize(C0026R.dimen.design_bottom_navigation_active_item_max_width);
        this.activeItemMinWidth = resources.getDimensionPixelSize(C0026R.dimen.design_bottom_navigation_active_item_min_width);
        this.itemHeight = resources.getDimensionPixelSize(C0026R.dimen.design_bottom_navigation_height);
        this.itemTextColorDefault = createDefaultColorStateList(16842808);
        this.set = new AutoTransition();
        this.set.setOrdering(0);
        this.set.setDuration((long) ACTIVE_ANIMATION_DURATION_MS);
        this.set.setInterpolator(new FastOutSlowInInterpolator());
        this.set.addTransition(new TextScale());
        this.onClickListener = new C00401();
        this.tempChildWidths = new int[5];
    }

    public void initialize(MenuBuilder menuBuilder) {
        this.menu = menuBuilder;
    }

    protected void onMeasure(int i, int i2) {
        View childAt;
        i = MeasureSpec.getSize(i);
        i2 = this.menu.getVisibleItems().size();
        int childCount = getChildCount();
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(this.itemHeight, 1073741824);
        int min;
        if (isShifting(this.labelVisibilityMode, i2) && this.itemHorizontalTranslationEnabled) {
            childAt = getChildAt(this.selectedItemPosition);
            int i3 = this.activeItemMinWidth;
            if (childAt.getVisibility() != 8) {
                childAt.measure(MeasureSpec.makeMeasureSpec(this.activeItemMaxWidth, Integer.MIN_VALUE), makeMeasureSpec);
                i3 = Math.max(i3, childAt.getMeasuredWidth());
            }
            i2 -= childAt.getVisibility() != 8 ? 1 : 0;
            min = Math.min(i - (this.inactiveItemMinWidth * i2), Math.min(i3, this.activeItemMaxWidth));
            i -= min;
            i3 = Math.min(i / (i2 == 0 ? 1 : i2), this.inactiveItemMaxWidth);
            i2 = i - (i2 * i3);
            i = 0;
            while (i < childCount) {
                if (getChildAt(i).getVisibility() != 8) {
                    this.tempChildWidths[i] = i == this.selectedItemPosition ? min : i3;
                    if (i2 > 0) {
                        int[] iArr = this.tempChildWidths;
                        iArr[i] = iArr[i] + 1;
                        i2--;
                    }
                } else {
                    this.tempChildWidths[i] = 0;
                }
                i++;
            }
        } else {
            min = Math.min(i / (i2 == 0 ? 1 : i2), this.activeItemMaxWidth);
            i2 = i - (i2 * min);
            for (i = 0; i < childCount; i++) {
                if (getChildAt(i).getVisibility() != 8) {
                    this.tempChildWidths[i] = min;
                    if (i2 > 0) {
                        int[] iArr2 = this.tempChildWidths;
                        iArr2[i] = iArr2[i] + 1;
                        i2--;
                    }
                } else {
                    this.tempChildWidths[i] = 0;
                }
            }
        }
        i2 = 0;
        for (i = 0; i < childCount; i++) {
            childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                childAt.measure(MeasureSpec.makeMeasureSpec(this.tempChildWidths[i], 1073741824), makeMeasureSpec);
                childAt.getLayoutParams().width = childAt.getMeasuredWidth();
                i2 += childAt.getMeasuredWidth();
            }
        }
        setMeasuredDimension(View.resolveSizeAndState(i2, MeasureSpec.makeMeasureSpec(i2, 1073741824), 0), View.resolveSizeAndState(this.itemHeight, makeMeasureSpec, 0));
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        z = getChildCount();
        i3 -= i;
        i4 -= i2;
        int i5 = 0;
        for (boolean z2 = false; z2 < z; z2++) {
            View childAt = getChildAt(z2);
            if (childAt.getVisibility() != 8) {
                if (ViewCompat.getLayoutDirection(this) == 1) {
                    int i6 = i3 - i5;
                    childAt.layout(i6 - childAt.getMeasuredWidth(), 0, i6, i4);
                } else {
                    childAt.layout(i5, 0, childAt.getMeasuredWidth() + i5, i4);
                }
                i5 += childAt.getMeasuredWidth();
            }
        }
    }

    public void setIconTintList(ColorStateList colorStateList) {
        this.itemIconTint = colorStateList;
        if (this.buttons != null) {
            for (BottomNavigationItemView iconTintList : this.buttons) {
                iconTintList.setIconTintList(colorStateList);
            }
        }
    }

    @Nullable
    public ColorStateList getIconTintList() {
        return this.itemIconTint;
    }

    public void setItemIconSize(@Dimension int i) {
        this.itemIconSize = i;
        if (this.buttons != null) {
            for (BottomNavigationItemView iconSize : this.buttons) {
                iconSize.setIconSize(i);
            }
        }
    }

    @Dimension
    public int getItemIconSize() {
        return this.itemIconSize;
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.itemTextColorFromUser = colorStateList;
        if (this.buttons != null) {
            for (BottomNavigationItemView textColor : this.buttons) {
                textColor.setTextColor(colorStateList);
            }
        }
    }

    public ColorStateList getItemTextColor() {
        return this.itemTextColorFromUser;
    }

    public void setItemTextAppearanceInactive(@StyleRes int i) {
        this.itemTextAppearanceInactive = i;
        if (this.buttons != null) {
            for (BottomNavigationItemView bottomNavigationItemView : this.buttons) {
                bottomNavigationItemView.setTextAppearanceInactive(i);
                if (this.itemTextColorFromUser != null) {
                    bottomNavigationItemView.setTextColor(this.itemTextColorFromUser);
                }
            }
        }
    }

    @StyleRes
    public int getItemTextAppearanceInactive() {
        return this.itemTextAppearanceInactive;
    }

    public void setItemTextAppearanceActive(@StyleRes int i) {
        this.itemTextAppearanceActive = i;
        if (this.buttons != null) {
            for (BottomNavigationItemView bottomNavigationItemView : this.buttons) {
                bottomNavigationItemView.setTextAppearanceActive(i);
                if (this.itemTextColorFromUser != null) {
                    bottomNavigationItemView.setTextColor(this.itemTextColorFromUser);
                }
            }
        }
    }

    @StyleRes
    public int getItemTextAppearanceActive() {
        return this.itemTextAppearanceActive;
    }

    public void setItemBackgroundRes(int i) {
        this.itemBackgroundRes = i;
        if (this.buttons != null) {
            for (BottomNavigationItemView itemBackground : this.buttons) {
                itemBackground.setItemBackground(i);
            }
        }
    }

    @Deprecated
    public int getItemBackgroundRes() {
        return this.itemBackgroundRes;
    }

    public void setItemBackground(@Nullable Drawable drawable) {
        this.itemBackground = drawable;
        if (this.buttons != null) {
            for (BottomNavigationItemView itemBackground : this.buttons) {
                itemBackground.setItemBackground(drawable);
            }
        }
    }

    @Nullable
    public Drawable getItemBackground() {
        if (this.buttons == null || this.buttons.length <= 0) {
            return this.itemBackground;
        }
        return this.buttons[0].getBackground();
    }

    public void setLabelVisibilityMode(int i) {
        this.labelVisibilityMode = i;
    }

    public int getLabelVisibilityMode() {
        return this.labelVisibilityMode;
    }

    public void setItemHorizontalTranslationEnabled(boolean z) {
        this.itemHorizontalTranslationEnabled = z;
    }

    public boolean isItemHorizontalTranslationEnabled() {
        return this.itemHorizontalTranslationEnabled;
    }

    public ColorStateList createDefaultColorStateList(int i) {
        TypedValue typedValue = new TypedValue();
        if (getContext().getTheme().resolveAttribute(i, typedValue, true) == 0) {
            return null;
        }
        i = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
        if (!getContext().getTheme().resolveAttribute(C0228R.attr.colorPrimary, typedValue, true)) {
            return null;
        }
        int i2 = typedValue.data;
        int defaultColor = i.getDefaultColor();
        return new ColorStateList(new int[][]{DISABLED_STATE_SET, CHECKED_STATE_SET, EMPTY_STATE_SET}, new int[]{i.getColorForState(DISABLED_STATE_SET, defaultColor), i2, defaultColor});
    }

    public void setPresenter(BottomNavigationPresenter bottomNavigationPresenter) {
        this.presenter = bottomNavigationPresenter;
    }

    public void buildMenuView() {
        int i;
        removeAllViews();
        if (this.buttons != null) {
            for (Object obj : this.buttons) {
                if (obj != null) {
                    this.itemPool.release(obj);
                }
            }
        }
        if (this.menu.size() == 0) {
            this.selectedItemId = 0;
            this.selectedItemPosition = 0;
            this.buttons = null;
            return;
        }
        this.buttons = new BottomNavigationItemView[this.menu.size()];
        boolean isShifting = isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());
        for (i = 0; i < this.menu.size(); i++) {
            this.presenter.setUpdateSuspended(true);
            this.menu.getItem(i).setCheckable(true);
            this.presenter.setUpdateSuspended(false);
            View newItem = getNewItem();
            this.buttons[i] = newItem;
            newItem.setIconTintList(this.itemIconTint);
            newItem.setIconSize(this.itemIconSize);
            newItem.setTextColor(this.itemTextColorDefault);
            newItem.setTextAppearanceInactive(this.itemTextAppearanceInactive);
            newItem.setTextAppearanceActive(this.itemTextAppearanceActive);
            newItem.setTextColor(this.itemTextColorFromUser);
            if (this.itemBackground != null) {
                newItem.setItemBackground(this.itemBackground);
            } else {
                newItem.setItemBackground(this.itemBackgroundRes);
            }
            newItem.setShifting(isShifting);
            newItem.setLabelVisibilityMode(this.labelVisibilityMode);
            newItem.initialize((MenuItemImpl) this.menu.getItem(i), 0);
            newItem.setItemPosition(i);
            newItem.setOnClickListener(this.onClickListener);
            addView(newItem);
        }
        this.selectedItemPosition = Math.min(this.menu.size() - 1, this.selectedItemPosition);
        this.menu.getItem(this.selectedItemPosition).setChecked(true);
    }

    public void updateMenuView() {
        if (this.menu != null) {
            if (this.buttons != null) {
                int size = this.menu.size();
                if (size != this.buttons.length) {
                    buildMenuView();
                    return;
                }
                int i;
                int i2 = this.selectedItemId;
                for (i = 0; i < size; i++) {
                    MenuItem item = this.menu.getItem(i);
                    if (item.isChecked()) {
                        this.selectedItemId = item.getItemId();
                        this.selectedItemPosition = i;
                    }
                }
                if (i2 != this.selectedItemId) {
                    TransitionManager.beginDelayedTransition(this, this.set);
                }
                boolean isShifting = isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());
                for (i = 0; i < size; i++) {
                    this.presenter.setUpdateSuspended(true);
                    this.buttons[i].setLabelVisibilityMode(this.labelVisibilityMode);
                    this.buttons[i].setShifting(isShifting);
                    this.buttons[i].initialize((MenuItemImpl) this.menu.getItem(i), 0);
                    this.presenter.setUpdateSuspended(false);
                }
            }
        }
    }

    private BottomNavigationItemView getNewItem() {
        BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView) this.itemPool.acquire();
        return bottomNavigationItemView == null ? new BottomNavigationItemView(getContext()) : bottomNavigationItemView;
    }

    public int getSelectedItemId() {
        return this.selectedItemId;
    }

    void tryRestoreSelectedItemId(int i) {
        int size = this.menu.size();
        for (int i2 = 0; i2 < size; i2++) {
            MenuItem item = this.menu.getItem(i2);
            if (i == item.getItemId()) {
                this.selectedItemId = i;
                this.selectedItemPosition = i2;
                item.setChecked(1);
                return;
            }
        }
    }
}
