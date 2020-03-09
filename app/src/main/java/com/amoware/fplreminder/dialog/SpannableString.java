package com.amoware.fplreminder.dialog;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TypefaceSpan;

import androidx.annotation.NonNull;

/**
 * Created by Jonas Eiselt on 2020-03-09.
 */
public class SpannableString {

    private final SpannableTypefaceSpan spannableTypefaceSpan;

    public SpannableString(Typeface typeface) {
        spannableTypefaceSpan = new SpannableTypefaceSpan(typeface);
    }

    public SpannableStringBuilder getType(CharSequence value) {
        SpannableStringBuilder ss = new SpannableStringBuilder(value);
        ss.setSpan(spannableTypefaceSpan, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return ss;
    }

    public SpannableStringBuilder getType(CharSequence value, int textSize) {
        SpannableStringBuilder ss = new SpannableStringBuilder(value);
        ss.setSpan(new AbsoluteSizeSpan(textSize, true), 0, ss.length(),
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(spannableTypefaceSpan, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return ss;
    }

    private static class SpannableTypefaceSpan extends TypefaceSpan {

        private final Typeface typeface;

        SpannableTypefaceSpan(Typeface type) {
            super("");
            typeface = type;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            if (typeface != null) {
                applyCustomTypeFace(ds, typeface);
            }
        }

        @Override
        public void updateMeasureState(@NonNull TextPaint paint) {
            if (typeface != null) {
                applyCustomTypeFace(paint, typeface);
            }
        }

        private void applyCustomTypeFace(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();

            oldStyle = old == null ? 0 : old.getStyle();

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTypeface(tf);
        }
    }
}
