package com.amoware.fplreminder;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    private CheckBox soundCheckbox;
    private CheckBox vibrationCheckbox;

    private boolean gameweeksDownloading;

    private boolean connectionTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureContentView();

        downloadGameweeks(null);

        connectionTest = isNetworkAvailable();
        Log.v("connected", "" + connectionTest);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        else
            return false;
    }

    public void downloadGameweeks(View view) {
        if (!gameweeksDownloading) {
            gameweeksDownloading = true;
            showProgress(true);

            GameweeksTask task = new GameweeksTask(this);
            task.execute();
        }
    }

    private void showProgress(boolean showProgress) {
        findViewById(R.id.main_progress_layout).setVisibility(showProgress ? VISIBLE : GONE);
        findViewById(R.id.main_refresh_button).setVisibility(showProgress ? GONE : VISIBLE);
        findViewById(R.id.main_timer_label_textview).setVisibility(showProgress ?  INVISIBLE : VISIBLE);
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
        if (fplReminder.isNotificationSound() == true) {
            soundCheckbox.setChecked(true);
        }
        else
            soundCheckbox.setChecked(false);

        vibrationCheckbox = findViewById(R.id.main_vibration_checkbox);
        vibrationCheckbox.setTypeface(boldTypeface);
        if (fplReminder.isNotificationVibration() == true) {
            vibrationCheckbox.setChecked(true);
        }
        else
            vibrationCheckbox.setChecked(false);

        displayNotificationTimer(fplReminder.getNotificationTimer());
    }


    public void showSnackbar(String info) {

        make(findViewById(R.id.main_linearlayout),  info, LENGTH_LONG)
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
        showProgress(false);
        gameweeksDownloading = false;
    }

    /** Called from the view when the user clicks on the checkbox concerning the sound. */
    public void changeSoundSettings(View view) {
        if (soundCheckbox.isChecked() == true) {
            fplReminder.setNotificationSound(soundCheckbox.isChecked());
            showSnackbar("Sound is ON");
        }
        else
            showSnackbar("Sound is OFF");
    }

    /** Called from the view when the user clicks on the checkbox concerning the vibration. */
    public void changeVibrationSettings(View view) {
        if (vibrationCheckbox.isChecked() == true) {
            fplReminder.setNotificationVibration(vibrationCheckbox.isChecked());
            showSnackbar("Vibration is ON");
        }
        else
            showSnackbar("Vibration is OFF");
    }
}
