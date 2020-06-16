package com.amoware.fplreminder;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amoware.fplreminder.common.ConnectionHandler;
import com.amoware.fplreminder.common.FplReminder;
import com.amoware.fplreminder.common.Time;
import com.amoware.fplreminder.common.TypefaceUtil;
import com.amoware.fplreminder.dialog.FplReminderDialog;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.gameweek.GameweeksTask;
import com.amoware.fplreminder.gameweek.GameweeksTaskInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.amoware.fplreminder.common.Constants.tagger;
import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;
import static com.google.android.material.snackbar.Snackbar.make;

/**
 * Created by amoware on 2019-12-29.
 */
public class MainActivity extends AppCompatActivity implements GameweeksTaskInterface {

    private FplReminder fplReminder;
    private FplReminderDialog dialog;

    private TextView hoursTextView;
    private TextView minutesTextView;
    private TextView upcomingDeadlineTextView;

    private CheckBox soundCheckbox;
    private CheckBox vibrationCheckbox;

    private boolean gameweeksDownloading;
    private boolean connectedToInternet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureContentView();

        downloadGameweeks(null);
    }

    public void downloadGameweeks(View view) {
        if (!gameweeksDownloading) {
            ConnectionHandler connectionHandler = new ConnectionHandler(this);
            if (!(connectedToInternet = connectionHandler.isNetworkAvailable())) {
                connectionSnackbar();
                showCurrentGameweek();
                showProgress(false);
            } else {
                gameweeksDownloading = true;
                showProgress(true);

                GameweeksTask task = new GameweeksTask(this);
                task.execute();
            }
        }
    }

    private void showProgress(boolean showProgress) {
        int visibleGone = showProgress ? VISIBLE : GONE;
        int goneVisible = showProgress ? GONE : VISIBLE;
        int invisibleVisible = showProgress ? INVISIBLE : VISIBLE;

        setVisibility(findViewById(R.id.main_progress_layout), visibleGone);
        setVisibility(findViewById(R.id.main_upcomingDeadline_textview), invisibleVisible);
        setVisibility(findViewById(R.id.main_refresh_button), goneVisible);

        // If there is no upcoming gameweek, the user shouldn't be able to set a reminder
        int statusVisibility = GONE;
        int notificationVisibility = goneVisible;
        int timerLabelVisibility = invisibleVisible;

        if (goneVisible == VISIBLE) {
            if (fplReminder.getCurrentGameweek() == null) {
                statusVisibility = VISIBLE;
                notificationVisibility = GONE;
                timerLabelVisibility = INVISIBLE;
            }
        }

        setVisibility(findViewById(R.id.main_status_layout), statusVisibility);
        setVisibility(findViewById(R.id.main_notification_layout), notificationVisibility);
        setVisibility(findViewById(R.id.main_timer_label_textview), timerLabelVisibility);
    }

    private void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    private void configureContentView() {
        fplReminder = new FplReminder(this);

        applyBoldTypefaceToTextViews(
                hoursTextView = findViewById(R.id.main_hours_value_textview),
                minutesTextView = findViewById(R.id.main_minutes_value_textview),
                findViewById(R.id.main_title_textview),
                upcomingDeadlineTextView = findViewById(R.id.main_upcomingDeadline_textview),
                findViewById(R.id.main_timer_label_textview),
                findViewById(R.id.main_hours_label_textview),
                findViewById(R.id.main_minutes_label_textview),
                findViewById(R.id.main_colon_label_textview),
                findViewById(R.id.main_suffixtimer_label_textview),
                findViewById(R.id.main_preferences_label_textview),
                findViewById(R.id.progress_textview),
                findViewById(R.id.main_notification_status)
        );

        Typeface boldTypeface = TypefaceUtil.getBoldTypeface(this);
        soundCheckbox = findViewById(R.id.main_sound_checkbox);
        soundCheckbox.setTypeface(boldTypeface);
        if (fplReminder.isNotificationSound()) {
            soundCheckbox.setChecked(true);
        }
        else {
            soundCheckbox.setChecked(false);
        }

        vibrationCheckbox = findViewById(R.id.main_vibration_checkbox);
        vibrationCheckbox.setTypeface(boldTypeface);
        if (fplReminder.isNotificationVibration()) {
            vibrationCheckbox.setChecked(true);
        }
        else {
            vibrationCheckbox.setChecked(false);
        }

        displayNotificationTimer(fplReminder.getNotificationTimer());
    }

    private void applyBoldTypefaceToTextViews(View... views) {
        Typeface boldTypeface = TypefaceUtil.getBoldTypeface(this);
        for (View view : views) {
            if (view != null) {
                ((TextView) view).setTypeface(boldTypeface);
            }
        }
    }

    public void showSnackbar(String info) {
        make(findViewById(R.id.main_linearlayout), info, LENGTH_LONG)
                // .setActionTextColor(getResources().getColor(R.color.design_default_color_error))
                .setTextColor(getResources().getColor(R.color.white))
                .show();
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
        showCurrentGameweek();
        showProgress(false);
        gameweeksDownloading = false;

        // No gameweeks to download when app is started.
        if (gameweeks == null || gameweeks.isEmpty()) {
            showSnackbar("Gameweeks can't be downloaded. Please try to refresh!");
        }
    }

    private void showCurrentGameweek() {
        Gameweek gameweek = fplReminder.getCurrentGameweek();
        String text = "No upcoming gameweek";
        if (gameweek != null) {
            Date deadline = gameweek.getDeadlineTime();
            text = gameweek.getName() != null ? (gameweek.getName() + ": No deadline"): text;
            if (deadline != null) {
                DateFormat dateFormat = new SimpleDateFormat("EEE d MMM HH:mm", new Locale("en"));
                text = gameweek.getName() + " deadline: " + dateFormat.format(deadline);
            }
        }
        upcomingDeadlineTextView.setText(text);
    }

    /** Called from the view when the user clicks on the checkbox concerning the sound. */
    public void changeSoundSettings(View view) {
        fplReminder.setNotificationSound(soundCheckbox.isChecked());
        if (soundCheckbox.isChecked()) {
            showSnackbar("Sound notification enabled");
        }
        else {
            showSnackbar("Sound notification disabled");
        }
    }

    /** Called from the view when the user clicks on the checkbox concerning the vibration. */
    public void changeVibrationSettings(View view) {
        fplReminder.setNotificationVibration(vibrationCheckbox.isChecked());
        if (vibrationCheckbox.isChecked()) {
            showSnackbar("Vibration notification enabled");
        }
        else {
            showSnackbar("Vibration notification disabled");
        }
    }

    public void connectionSnackbar() {
        if (!connectedToInternet) {
            showSnackbar("No connection to internet. Please check your internet connection or to refresh");
        }
    }
}
