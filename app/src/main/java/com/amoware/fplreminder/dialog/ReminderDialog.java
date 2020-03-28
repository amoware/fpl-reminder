package com.amoware.fplreminder.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amoware.fplreminder.R;
import com.amoware.fplreminder.common.Time;
import com.amoware.fplreminder.common.TypefaceUtil;
import com.amoware.fplreminder.gameweek.Gameweek;

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

    private Gameweek gameweek;

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
        Typeface boldTypeface = TypefaceUtil.getBoldTypeface(context);
        SpannableString boldSS = new SpannableString(boldTypeface);

        // Setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);

        String dialogTitle = context.getString(R.string.dialog_title);
        builder.setTitle(boldSS.getType(dialogTitle, TEXT_SIZE + 1));
        builder.setView(getContentView());

        String setReminder = context.getString(R.string.dialog_button_setreminder);
        builder.setPositiveButton(boldSS.getType(setReminder), (dialogInterface, i) -> {
            if (onTimeSelectedListener != null) {
                onTimeSelectedListener.onTimeSelected(
                        new Time(hoursNumberPicker.getValue(), minutesNumberPicker.getValue())
                );
            }
        });

        String cancel = context.getString(R.string.dialog_button_cancel);
        builder.setNegativeButton(boldSS.getType(cancel), null);

        return builder.create();
    }

    @SuppressLint("InflateParams")
    private View getContentView() {
        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = null;
        if (inflater != null) {
            contentView = inflater.inflate(R.layout.layout_reminderdialog, null, false);

            Typeface semiboldTypeface = TypefaceUtil.getSemiboldTypeface(context);

            messageTextView = contentView.findViewById(R.id.dialog_message_textview);

            setTextViewTypeface(messageTextView, semiboldTypeface);
            setTextViewTypeface(contentView.findViewById(R.id.dialog_hours_textview), semiboldTypeface);
            setTextViewTypeface(contentView.findViewById(R.id.dialog_minutes_textview), semiboldTypeface);

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
        if (textView != null) {
            textView.setTypeface(typeface);
        }
    }

    private void setDialogButton(int buttonType) {
        Button button = dialog.getButton(buttonType);
        if (button != null) {
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) TEXT_SIZE);
            button.setAllCaps(false);
            button.setTextColor(context.getResources().getColor(R.color.dialog_button_foreground));
        }
    }

    public boolean isShowing() {
        return dialog != null && isShowing;
    }

    public void setGameweek(Gameweek gameweek) {
        this.gameweek = gameweek;
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
            String name = context.getString(R.string.dialog_message_agameweek);
            if (gameweek != null && gameweek.getName() != null) {
                name = gameweek.getName().toLowerCase();
            }

            messageTextView.setText(context.getString(R.string.dialog_message,
                    Integer.toString(hoursNumberPicker.getValue()),
                    Integer.toString(minutesNumberPicker.getValue()),
                    name));
        }
    }

    public void setOnTimeSelected(OnTimeSelectedListener onTimeSelectedListener) {
        this.onTimeSelectedListener = onTimeSelectedListener;
    }
}

