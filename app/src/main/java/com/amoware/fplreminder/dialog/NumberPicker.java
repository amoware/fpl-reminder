package com.amoware.fplreminder.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.amoware.fplreminder.R;

import static com.amoware.fplreminder.common.Constants.TAG;

/**
 * Created by amoware on 2020-03-09.
 */
public class NumberPicker extends android.widget.NumberPicker {

    public NumberPicker(Context context) {
        super(context);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    private void updateView(View view) {
        if (view instanceof EditText) {
            Typeface typeface = getTypeface();

            if (typeface != null) {
                ((EditText) view).setTypeface(typeface);
                ((EditText) view).setTextSize(17);
                ((EditText) view).setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    private Typeface getTypeface() {
        Typeface typeface = null;
        try {
            typeface = Typeface.createFromAsset(getContext().getAssets(), "nunito-semibold.ttf");
        } catch (Exception e) {
            Log.e(TAG, "Unable to load typeface: " + e.getMessage());
        }
        return typeface;
    }
}
