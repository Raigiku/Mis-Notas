package android.support.design.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.design.C0026R;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

@RestrictTo({Scope.LIBRARY_GROUP})
public class ScrimInsetsFrameLayout extends FrameLayout {
    Drawable insetForeground;
    Rect insets;
    private Rect tempRect;

    /* renamed from: android.support.design.internal.ScrimInsetsFrameLayout$1 */
    class C03391 implements OnApplyWindowInsetsListener {
        C03391() {
        }

        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            boolean z;
            if (ScrimInsetsFrameLayout.this.insets == null) {
                ScrimInsetsFrameLayout.this.insets = new Rect();
            }
            ScrimInsetsFrameLayout.this.insets.set(windowInsetsCompat.getSystemWindowInsetLeft(), windowInsetsCompat.getSystemWindowInsetTop(), windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
            ScrimInsetsFrameLayout.this.onInsetsChanged(windowInsetsCompat);
            view = ScrimInsetsFrameLayout.this;
            if (windowInsetsCompat.hasSystemWindowInsets()) {
                if (ScrimInsetsFrameLayout.this.insetForeground != null) {
                    z = false;
                    view.setWillNotDraw(z);
                    ViewCompat.postInvalidateOnAnimation(ScrimInsetsFrameLayout.this);
                    return windowInsetsCompat.consumeSystemWindowInsets();
                }
            }
            z = true;
            view.setWillNotDraw(z);
            ViewCompat.postInvalidateOnAnimation(ScrimInsetsFrameLayout.this);
            return windowInsetsCompat.consumeSystemWindowInsets();
        }
    }

    protected void onInsetsChanged(WindowInsetsCompat windowInsetsCompat) {
    }

    public ScrimInsetsFrameLayout(Context context) {
        this(context, null);
    }

    public ScrimInsetsFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScrimInsetsFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.tempRect = new Rect();
        context = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, C0026R.styleable.ScrimInsetsFrameLayout, i, C0026R.style.Widget_Design_ScrimInsetsFrameLayout, new int[0]);
        this.insetForeground = context.getDrawable(C0026R.styleable.ScrimInsetsFrameLayout_insetForeground);
        context.recycle();
        setWillNotDraw(true);
        ViewCompat.setOnApplyWindowInsetsListener(this, new C03391());
    }

    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (this.insets != null && this.insetForeground != null) {
            int save = canvas.save();
            canvas.translate((float) getScrollX(), (float) getScrollY());
            this.tempRect.set(0, 0, width, this.insets.top);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            this.tempRect.set(0, height - this.insets.bottom, width, height);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            this.tempRect.set(0, this.insets.top, this.insets.left, height - this.insets.bottom);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            this.tempRect.set(width - this.insets.right, this.insets.top, width, height - this.insets.bottom);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(canvas);
            canvas.restoreToCount(save);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.insetForeground != null) {
            this.insetForeground.setCallback(this);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.insetForeground != null) {
            this.insetForeground.setCallback(null);
        }
    }
}
