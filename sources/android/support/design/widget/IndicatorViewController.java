package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.C0026R;
import android.support.design.animation.AnimationUtils;
import android.support.design.animation.AnimatorSetCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.Space;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

final class IndicatorViewController {
    private static final int CAPTION_OPACITY_FADE_ANIMATION_DURATION = 167;
    private static final int CAPTION_STATE_ERROR = 1;
    private static final int CAPTION_STATE_HELPER_TEXT = 2;
    private static final int CAPTION_STATE_NONE = 0;
    private static final int CAPTION_TRANSLATE_Y_ANIMATION_DURATION = 217;
    static final int COUNTER_INDEX = 2;
    static final int ERROR_INDEX = 0;
    static final int HELPER_INDEX = 1;
    @Nullable
    private Animator captionAnimator;
    private FrameLayout captionArea;
    private int captionDisplayed;
    private int captionToShow;
    private final float captionTranslationYPx = ((float) this.context.getResources().getDimensionPixelSize(C0026R.dimen.design_textinput_caption_translate_y));
    private int captionViewsAdded;
    private final Context context;
    private boolean errorEnabled;
    private CharSequence errorText;
    private int errorTextAppearance;
    private TextView errorView;
    private CharSequence helperText;
    private boolean helperTextEnabled;
    private int helperTextTextAppearance;
    private TextView helperTextView;
    private LinearLayout indicatorArea;
    private int indicatorsAdded;
    private final TextInputLayout textInputView;
    private Typeface typeface;

    boolean isCaptionView(int i) {
        return i != 0 ? i == 1 : true;
    }

    public IndicatorViewController(TextInputLayout textInputLayout) {
        this.context = textInputLayout.getContext();
        this.textInputView = textInputLayout;
    }

    void showHelper(CharSequence charSequence) {
        cancelCaptionAnimator();
        this.helperText = charSequence;
        this.helperTextView.setText(charSequence);
        if (this.captionDisplayed != 2) {
            this.captionToShow = 2;
        }
        updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, shouldAnimateCaptionView(this.helperTextView, charSequence));
    }

    void hideHelperText() {
        cancelCaptionAnimator();
        if (this.captionDisplayed == 2) {
            this.captionToShow = 0;
        }
        updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, shouldAnimateCaptionView(this.helperTextView, null));
    }

    void showError(CharSequence charSequence) {
        cancelCaptionAnimator();
        this.errorText = charSequence;
        this.errorView.setText(charSequence);
        if (this.captionDisplayed != 1) {
            this.captionToShow = 1;
        }
        updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, shouldAnimateCaptionView(this.errorView, charSequence));
    }

    void hideError() {
        this.errorText = null;
        cancelCaptionAnimator();
        if (this.captionDisplayed == 1) {
            if (!this.helperTextEnabled || TextUtils.isEmpty(this.helperText)) {
                this.captionToShow = 0;
            } else {
                this.captionToShow = 2;
            }
        }
        updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, shouldAnimateCaptionView(this.errorView, null));
    }

    private boolean shouldAnimateCaptionView(TextView textView, @Nullable CharSequence charSequence) {
        return (ViewCompat.isLaidOut(this.textInputView) && this.textInputView.isEnabled() && (this.captionToShow != this.captionDisplayed || textView == null || TextUtils.equals(textView.getText(), charSequence) == null)) ? true : null;
    }

    private void updateCaptionViewsVisibility(int i, int i2, boolean z) {
        if (z) {
            Animator animatorSet = new AnimatorSet();
            this.captionAnimator = animatorSet;
            List arrayList = new ArrayList();
            List list = arrayList;
            int i3 = i;
            int i4 = i2;
            createCaptionAnimators(list, this.helperTextEnabled, this.helperTextView, 2, i3, i4);
            createCaptionAnimators(list, this.errorEnabled, this.errorView, 1, i3, i4);
            AnimatorSetCompat.playTogether(animatorSet, arrayList);
            final TextView captionViewFromDisplayState = getCaptionViewFromDisplayState(i);
            final TextView captionViewFromDisplayState2 = getCaptionViewFromDisplayState(i2);
            final int i5 = i2;
            final int i6 = i;
            animatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    IndicatorViewController.this.captionDisplayed = i5;
                    IndicatorViewController.this.captionAnimator = null;
                    if (captionViewFromDisplayState != null) {
                        captionViewFromDisplayState.setVisibility(4);
                        if (i6 == 1 && IndicatorViewController.this.errorView != null) {
                            IndicatorViewController.this.errorView.setText(null);
                        }
                    }
                }

                public void onAnimationStart(Animator animator) {
                    if (captionViewFromDisplayState2 != null) {
                        captionViewFromDisplayState2.setVisibility(0);
                    }
                }
            });
            animatorSet.start();
        } else {
            setCaptionViewVisibilities(i, i2);
        }
        this.textInputView.updateEditTextBackground();
        this.textInputView.updateLabelState(z);
        this.textInputView.updateTextInputBoxState();
    }

    private void setCaptionViewVisibilities(int i, int i2) {
        if (i != i2) {
            TextView captionViewFromDisplayState;
            if (i2 != 0) {
                captionViewFromDisplayState = getCaptionViewFromDisplayState(i2);
                if (captionViewFromDisplayState != null) {
                    captionViewFromDisplayState.setVisibility(0);
                    captionViewFromDisplayState.setAlpha(1.0f);
                }
            }
            if (i != 0) {
                captionViewFromDisplayState = getCaptionViewFromDisplayState(i);
                if (captionViewFromDisplayState != null) {
                    captionViewFromDisplayState.setVisibility(4);
                    if (i == 1) {
                        captionViewFromDisplayState.setText(0);
                    }
                }
            }
            this.captionDisplayed = i2;
        }
    }

    private void createCaptionAnimators(List<Animator> list, boolean z, TextView textView, int i, int i2, int i3) {
        if (textView != null) {
            if (z) {
                if (i == i3 || i == i2) {
                    list.add(createCaptionOpacityAnimator(textView, i3 == i));
                    if (i3 == i) {
                        list.add(createCaptionTranslationYAnimator(textView));
                    }
                }
            }
        }
    }

    private ObjectAnimator createCaptionOpacityAnimator(TextView textView, boolean z) {
        z = z ? true : false;
        textView = ObjectAnimator.ofFloat(textView, View.ALPHA, new float[]{z});
        textView.setDuration(167);
        textView.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        return textView;
    }

    private ObjectAnimator createCaptionTranslationYAnimator(TextView textView) {
        textView = ObjectAnimator.ofFloat(textView, View.TRANSLATION_Y, new float[]{-this.captionTranslationYPx, 0.0f});
        textView.setDuration(217);
        textView.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        return textView;
    }

    void cancelCaptionAnimator() {
        if (this.captionAnimator != null) {
            this.captionAnimator.cancel();
        }
    }

    @Nullable
    private TextView getCaptionViewFromDisplayState(int i) {
        switch (i) {
            case 1:
                return this.errorView;
            case 2:
                return this.helperTextView;
            default:
                return 0;
        }
    }

    void adjustIndicatorPadding() {
        if (canAdjustIndicatorPadding()) {
            ViewCompat.setPaddingRelative(this.indicatorArea, ViewCompat.getPaddingStart(this.textInputView.getEditText()), 0, ViewCompat.getPaddingEnd(this.textInputView.getEditText()), 0);
        }
    }

    private boolean canAdjustIndicatorPadding() {
        return (this.indicatorArea == null || this.textInputView.getEditText() == null) ? false : true;
    }

    void addIndicator(TextView textView, int i) {
        if (this.indicatorArea == null && this.captionArea == null) {
            this.indicatorArea = new LinearLayout(this.context);
            this.indicatorArea.setOrientation(0);
            this.textInputView.addView(this.indicatorArea, -1, -2);
            this.captionArea = new FrameLayout(this.context);
            this.indicatorArea.addView(this.captionArea, -1, new LayoutParams(-2, -2));
            this.indicatorArea.addView(new Space(this.context), new LinearLayout.LayoutParams(0, 0, 1.0f));
            if (this.textInputView.getEditText() != null) {
                adjustIndicatorPadding();
            }
        }
        if (isCaptionView(i)) {
            this.captionArea.setVisibility(0);
            this.captionArea.addView(textView);
            this.captionViewsAdded++;
        } else {
            this.indicatorArea.addView(textView, i);
        }
        this.indicatorArea.setVisibility(0);
        this.indicatorsAdded++;
    }

    void removeIndicator(TextView textView, int i) {
        if (this.indicatorArea != null) {
            if (isCaptionView(i) == 0 || this.captionArea == 0) {
                this.indicatorArea.removeView(textView);
            } else {
                this.captionViewsAdded--;
                setViewGroupGoneIfEmpty(this.captionArea, this.captionViewsAdded);
                this.captionArea.removeView(textView);
            }
            this.indicatorsAdded--;
            setViewGroupGoneIfEmpty(this.indicatorArea, this.indicatorsAdded);
        }
    }

    private void setViewGroupGoneIfEmpty(ViewGroup viewGroup, int i) {
        if (i == 0) {
            viewGroup.setVisibility(8);
        }
    }

    void setErrorEnabled(boolean z) {
        if (this.errorEnabled != z) {
            cancelCaptionAnimator();
            if (z) {
                this.errorView = new AppCompatTextView(this.context);
                this.errorView.setId(C0026R.id.textinput_error);
                if (this.typeface != null) {
                    this.errorView.setTypeface(this.typeface);
                }
                setErrorTextAppearance(this.errorTextAppearance);
                this.errorView.setVisibility(4);
                ViewCompat.setAccessibilityLiveRegion(this.errorView, 1);
                addIndicator(this.errorView, 0);
            } else {
                hideError();
                removeIndicator(this.errorView, 0);
                this.errorView = null;
                this.textInputView.updateEditTextBackground();
                this.textInputView.updateTextInputBoxState();
            }
            this.errorEnabled = z;
        }
    }

    boolean isErrorEnabled() {
        return this.errorEnabled;
    }

    boolean isHelperTextEnabled() {
        return this.helperTextEnabled;
    }

    void setHelperTextEnabled(boolean z) {
        if (this.helperTextEnabled != z) {
            cancelCaptionAnimator();
            if (z) {
                this.helperTextView = new AppCompatTextView(this.context);
                this.helperTextView.setId(C0026R.id.textinput_helper_text);
                if (this.typeface != null) {
                    this.helperTextView.setTypeface(this.typeface);
                }
                this.helperTextView.setVisibility(4);
                ViewCompat.setAccessibilityLiveRegion(this.helperTextView, 1);
                setHelperTextAppearance(this.helperTextTextAppearance);
                addIndicator(this.helperTextView, 1);
            } else {
                hideHelperText();
                removeIndicator(this.helperTextView, 1);
                this.helperTextView = null;
                this.textInputView.updateEditTextBackground();
                this.textInputView.updateTextInputBoxState();
            }
            this.helperTextEnabled = z;
        }
    }

    boolean errorIsDisplayed() {
        return isCaptionStateError(this.captionDisplayed);
    }

    boolean errorShouldBeShown() {
        return isCaptionStateError(this.captionToShow);
    }

    private boolean isCaptionStateError(int i) {
        if (i == 1 && this.errorView != 0 && TextUtils.isEmpty(this.errorText) == 0) {
            return true;
        }
        return false;
    }

    boolean helperTextIsDisplayed() {
        return isCaptionStateHelperText(this.captionDisplayed);
    }

    boolean helperTextShouldBeShown() {
        return isCaptionStateHelperText(this.captionToShow);
    }

    private boolean isCaptionStateHelperText(int i) {
        return i == 2 && this.helperTextView != 0 && TextUtils.isEmpty(this.helperText) == 0;
    }

    CharSequence getErrorText() {
        return this.errorText;
    }

    CharSequence getHelperText() {
        return this.helperText;
    }

    void setTypefaces(Typeface typeface) {
        if (typeface != this.typeface) {
            this.typeface = typeface;
            setTextViewTypeface(this.errorView, typeface);
            setTextViewTypeface(this.helperTextView, typeface);
        }
    }

    private void setTextViewTypeface(@Nullable TextView textView, Typeface typeface) {
        if (textView != null) {
            textView.setTypeface(typeface);
        }
    }

    @ColorInt
    int getErrorViewCurrentTextColor() {
        return this.errorView != null ? this.errorView.getCurrentTextColor() : -1;
    }

    @Nullable
    ColorStateList getErrorViewTextColors() {
        return this.errorView != null ? this.errorView.getTextColors() : null;
    }

    void setErrorViewTextColor(@Nullable ColorStateList colorStateList) {
        if (this.errorView != null) {
            this.errorView.setTextColor(colorStateList);
        }
    }

    void setErrorTextAppearance(@StyleRes int i) {
        this.errorTextAppearance = i;
        if (this.errorView != null) {
            this.textInputView.setTextAppearanceCompatWithErrorFallback(this.errorView, i);
        }
    }

    @ColorInt
    int getHelperTextViewCurrentTextColor() {
        return this.helperTextView != null ? this.helperTextView.getCurrentTextColor() : -1;
    }

    @Nullable
    ColorStateList getHelperTextViewColors() {
        return this.helperTextView != null ? this.helperTextView.getTextColors() : null;
    }

    void setHelperTextViewTextColor(@Nullable ColorStateList colorStateList) {
        if (this.helperTextView != null) {
            this.helperTextView.setTextColor(colorStateList);
        }
    }

    void setHelperTextAppearance(@StyleRes int i) {
        this.helperTextTextAppearance = i;
        if (this.helperTextView != null) {
            TextViewCompat.setTextAppearance(this.helperTextView, i);
        }
    }
}
