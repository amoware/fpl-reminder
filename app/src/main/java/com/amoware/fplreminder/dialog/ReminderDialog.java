package com.amoware.fplreminder.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amoware.fplreminder.R;
import com.amoware.fplreminder.common.Time;

import static com.amoware.fplreminder.common.Constants.NUNITO_REGULAR;
import static com.amoware.fplreminder.common.Constants.NUNITO_SEMIBOLD;
import static com.amoware.fplreminder.common.Constants.TAG;

/**
 * Created by amoware on 2020-03-09.
 */
public class ReminderDialog {

    private OnTimeSelectedListener onTimeSelectedListener;

    public interface OnTimeSelectedListener {
        void onTimeSelected(Time time);
    }

    private Context context;
    private AlertDialog dialog;

    private boolean isShowing;

    private final int TEXT_SIZE = 18;

    private NumberPicker hoursNumberPicker;
    private NumberPicker minutesNumberPicker;

    private TextView messageTextView;

    public ReminderDialog(Context context) {
        this.context = context;
    }

    public void show() {
        isShowing = true;
        dialog = createAlertDialog();
        dialog.show();

        setDialogButton(DialogInterface.BUTTON_POSITIVE);
        setDialogButton(DialogInterface.BUTTON_NEGATIVE);

        dialog.setOnDismissListener(dialogInterface -> isShowing = false);
        dialog.setOnCancelListener(dialogInterface -> isShowing = false);
    }

    private AlertDialog createAlertDialog() {
        Typeface semiboldTypeface = getTypeface(NUNITO_SEMIBOLD);
        SpannableString semiboldSS = new SpannableString(semiboldTypeface);

        // Setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);

        builder.setTitle(semiboldSS.getType("Reminder", TEXT_SIZE + 1));
        builder.setView(getContentView());

        builder.setPositiveButton(semiboldSS.getType("Cancel"), null);

        builder.setNegativeButton(semiboldSS.getType("Set reminder"), (dialogInterface, i) -> {
            if (onTimeSelectedListener != null) {
                onTimeSelectedListener.onTimeSelected(
                        new Time(hoursNumberPicker.getValue(), minutesNumberPicker.getValue())
                );
            }
        });

        return builder.create();
    }

    @SuppressLint("InflateParams")
    private View getContentView() {
        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = null;
        if (inflater != null) {
            contentView = inflater.inflate(R.layout.layout_reminderdialog, null, false);

            Typeface regularTypeface = getTypeface(NUNITO_REGULAR);

            messageTextView = contentView.findViewById(R.id.dialog_message_textview);

            setTextViewTypeface(messageTextView, regularTypeface);
            setTextViewTypeface(contentView.findViewById(R.id.dialog_hours_textview), regularTypeface);
            setTextViewTypeface(contentView.findViewById(R.id.dialog_minutes_textview), regularTypeface);

            hoursNumberPicker = contentView.findViewById(R.id.dialog_hours_numberpicker);
            hoursNumberPicker.setMinValue(0);
            hoursNumberPicker.setMaxValue(47);

            minutesNumberPicker = contentView.findViewById(R.id.dialog_minutes_numberpicker);
            minutesNumberPicker.setMinValue(0);
            minutesNumberPicker.setMaxValue(59);

            hoursNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> updateDialogMessage());
            minutesNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> updateDialogMessage());
        }
        return contentView;
    }

    private void setTextViewTypeface(TextView textView, Typeface typeface) {
        if (textView != null && typeface != null) {
            textView.setTypeface(typeface);
        }
    }

    private Typeface getTypeface(String path) {
        Typeface typeface = null;
        try {
            typeface = Typeface.createFromAsset(context.getAssets(), path);
        } catch (Exception e) {
            Log.e(TAG, "Unable to load typeface: " + e.getMessage());
        }
        return typeface;
    }

    private void setDialogButton(int buttonType) {
        Button button = dialog.getButton(buttonType);
        if (button != null) {
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) TEXT_SIZE);
            button.setAllCaps(false);
            button.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
    }

    public boolean isShowing() {
        return dialog != null && isShowing;
    }

    public void setTime(Time time) {
        if (time != null && hoursNumberPicker != null) {
            hoursNumberPicker.setValue(time.getHours());
        }

        if (time != null && minutesNumberPicker != null) {
            minutesNumberPicker.setValue(time.getMinutes());
        }

        updateDialogMessage();
    }

    private void updateDialogMessage() {
        if (hoursNumberPicker != null && minutesNumberPicker != null) {
            messageTextView.setText(context.getString(R.string.dialog_message,
                    Integer.toString(hoursNumberPicker.getValue()),
                    Integer.toString(minutesNumberPicker.getValue())));
        }
    }

    public void setOnTimeSelected(OnTimeSelectedListener onTimeSelectedListener) {
        this.onTimeSelectedListener = onTimeSelectedListener;
    }
}

