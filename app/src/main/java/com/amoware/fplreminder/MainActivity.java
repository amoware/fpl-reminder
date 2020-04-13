package com.amoware.fplreminder;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.amoware.fplreminder.common.FplReminder;
import com.amoware.fplreminder.common.Time;
import com.amoware.fplreminder.common.TypefaceUtil;
import com.amoware.fplreminder.dialog.FplReminderDialog;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.gameweek.GameweeksTask;
import com.amoware.fplreminder.gameweek.GameweeksTaskInterface;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amoware.fplreminder.common.Constants.tagger;

/**
 * Created by amoware on 2019-12-29.
 */
public class MainActivity extends AppCompatActivity implements GameweeksTaskInterface {

    private LinearLayout linearLayout;
    private FplReminder fplReminder;
    private FplReminderDialog dialog;

    private TextView hoursTextView;
    private TextView minutesTextView;

    private CheckBox soundCheckbox;
    private CheckBox vibrationCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showProgress(true);
        configureContentView();

        GameweeksTask task = new GameweeksTask(this);
        task.execute();

    }

    public void showSnackbar(String info){
        Snackbar.make(findViewById(R.id.linearLayout), "Settings saved: \n" + info,
                Snackbar.LENGTH_LONG)
                .show();
    }

    private void showProgress(boolean showProgress) {
        findViewById(R.id.main_progress_layout).setVisibility(showProgress ? VISIBLE : GONE);
        findViewById(R.id.main_notification_layout).setVisibility(showProgress ? GONE : VISIBLE);
    }

    private void configureContentView() {
        fplReminder = new FplReminder(this);

        Typeface boldTypeface = TypefaceUtil.getBoldTypeface(this);
        hoursTextView = findViewById(R.id.main_hours_value_textview);
        minutesTextView = findViewById(R.id.main_minutes_value_textview);

        hoursTextView.setTypeface(boldTypeface);
        minutesTextView.setTypeface(boldTypeface);

        ((TextView) findViewById(R.id.main_title_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_upcomingDeadline_textview)).setTypeface(boldTypeface);

        ((TextView) findViewById(R.id.main_timer_label_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_hours_label_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_minutes_label_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_colon_label_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_suffixtimer_label_textview)).setTypeface(boldTypeface);

        ((TextView) findViewById(R.id.main_preferences_label_textview)).setTypeface(boldTypeface);

        ((TextView) findViewById(R.id.progress_textview)).setTypeface(boldTypeface);

        soundCheckbox = findViewById(R.id.main_sound_checkbox);
        soundCheckbox.setTypeface(boldTypeface);
        if (fplReminder.isNotificationSound() == true){
            soundCheckbox.setChecked(true);
            showSnackbar("Sound is ON");
        }
        else
            soundCheckbox.setChecked(false);
            showSnackbar("Sound is OFF");

        vibrationCheckbox = findViewById(R.id.main_vibration_checkbox);
        vibrationCheckbox.setTypeface(boldTypeface);
        if (fplReminder.isNotificationVibration() == true){
            vibrationCheckbox.setChecked(true);
            showSnackbar("Vibration is ON");
        }
        else
            vibrationCheckbox.setChecked(false);
            showSnackbar("Vibration is OFF");


        displayNotificationTimer(fplReminder.getNotificationTimer());
    }

    private void displayNotificationTimer(Time time) {
        if (time != null && hoursTextView != null && minutesTextView != null) {
            hoursTextView.setText(String.valueOf(time.getHours()));
            minutesTextView.setText(String.valueOf(time.getMinutes()));
        }
    }

    /** Called from the view when the user clicks on the layout including the notification timer. */
    public void showReminderDialog(View view) {
        if (dialog == null || !dialog.isShowing()) {
            (dialog = new FplReminderDialog(fplReminder)).show();
            dialog.setOnTimeSelected(this::displayNotificationTimer);
        }
    }

    @Override
    public void onGameweeksDownloaded(List<Gameweek> gameweeks) {
        Log.d(tagger(getClass()), "Gameweeks from FPL: " + gameweeks);
        fplReminder.onGameweeksDownloaded(gameweeks);
        showProgress(false);
    }

    /** Called from the view when the user clicks on the checkbox concerning the sound. */
    public void changeSoundSettings(View view) {
        if (soundCheckbox != null) {
            fplReminder.setNotificationSound(soundCheckbox.isChecked());
        }
    }

    /** Called from the view when the user clicks on the checkbox concerning the vibration. */
    public void changeVibrationSettings(View view) {
        if (vibrationCheckbox != null) {
            fplReminder.setNotificationVibration(vibrationCheckbox.isChecked());
        }
    }
}
