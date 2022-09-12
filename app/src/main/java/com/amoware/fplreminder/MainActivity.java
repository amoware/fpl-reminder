package com.amoware.fplreminder;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amoware.fplreminder.alarm.RestoreAlarmsReceiver;
import com.amoware.fplreminder.common.ConnectionHandler;
import com.amoware.fplreminder.common.FplReminder;
import com.amoware.fplreminder.common.Time;
import com.amoware.fplreminder.common.TypefaceUtil;
import com.amoware.fplreminder.dialog.FplReminderDialog;
import com.amoware.fplreminder.dialog.SpannableString;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.model.gameweek.FetchGameweeksTask;
import com.amoware.fplreminder.model.gameweek.GameweeksTaskInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private FplReminder mFplReminder;
    private FplReminderDialog mDialog;

    private TextView hoursTextView;
    private TextView minutesTextView;
    private TextView upcomingDeadlineTextView;

    private CheckBox soundCheckbox;
    private CheckBox vibrationCheckbox;

    private boolean gameweeksDownloading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFplReminder = new FplReminder(this);

        configureContentView();
        downloadGameweeks(null);

        // Activate trigger-after-boot receiver
        ComponentName receiver = new ComponentName(this, RestoreAlarmsReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        );

        // Disable trigger-after-boot receiver
        /*ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);*/
    }

    public void downloadGameweeks(View view) {
        if (gameweeksDownloading) {
            return;
        }

        ConnectionHandler connectionHandler = new ConnectionHandler(this);
        if (!connectionHandler.isNetworkAvailable()) {
            showProgress(false);
            showNetworkUnavailableSnackbar();
            showCurrentGameweek();
            return;
        }

        gameweeksDownloading = true;
        showProgress(true);

        new FetchGameweeksTask(mFplReminder, this)
                .execute();
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
            if (mFplReminder.getCurrentGameweek() == null) {
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

        findViewById(R.id.main_notification_layout)
                .setOnClickListener(this::showReminderDialog);

        Typeface boldTypeface = TypefaceUtil.getBoldTypeface(this);
        soundCheckbox = findViewById(R.id.main_sound_checkbox);
        soundCheckbox.setTypeface(boldTypeface);
        soundCheckbox.setChecked(mFplReminder.isNotificationSound());
        soundCheckbox.setOnClickListener(this::changeSoundSettings);

        vibrationCheckbox = findViewById(R.id.main_vibration_checkbox);
        vibrationCheckbox.setTypeface(boldTypeface);
        vibrationCheckbox.setChecked(mFplReminder.isNotificationVibration());
        vibrationCheckbox.setOnClickListener(this::changeVibrationSettings);

        displayNotificationTimer(mFplReminder.getNotificationTimer());

        findViewById(R.id.main_refresh_button)
                .setOnClickListener(this::downloadGameweeks);
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
        SpannableString ss = new SpannableString(TypefaceUtil.getRegularTypeface(this));
        make(findViewById(R.id.main_linearlayout), ss.getType(info), LENGTH_LONG)
                .setTextColor(getResources().getColor(R.color.white))
                .show();
    }

    private void displayNotificationTimer(Time time) {
        if (time != null && hoursTextView != null && minutesTextView != null) {
            hoursTextView.setText(String.valueOf(time.getHours()));
            minutesTextView.setText(String.valueOf(time.getMinutes()));
        }
    }

    /**
     * Called from the view when the user clicks on the layout including the notification timer.
     */
    public void showReminderDialog(View view) {
        if (mDialog == null || !mDialog.isShowing()) {
            (mDialog = new FplReminderDialog(mFplReminder)).show();
            mDialog.setOnTimeSelected(this::displayNotificationTimer);
        }
    }

    @Override
    public void onGameweeksDownloaded(List<Gameweek> gameweeks) {
        Log.d(tagger(getClass()), "Gameweeks from FPL: " + gameweeks);
        mFplReminder.onGameweeksDownloaded(gameweeks);
        showCurrentGameweek();
        showProgress(false);
        gameweeksDownloading = false;

        // No gameweeks to download when app is started.
        if (gameweeks == null || gameweeks.isEmpty()) {
            showSnackbar(getString(R.string.snackbar_text_nogameweeks));
        }
    }

    private void showCurrentGameweek() {
        Gameweek gameweek = mFplReminder.getCurrentGameweekFromStorage();
        String text = getString(R.string.overline_text_status_nogameweek);
        if (gameweek != null) {
            Date deadline = gameweek.getDeadlineTime();
            text = gameweek.getName() != null ?
                    (gameweek.getName() + " " + getString(R.string.overline_text_status_nodeadline)) : text;
            if (deadline != null) {
                DateFormat dateFormat = new SimpleDateFormat("EEE d MMM yyyy HH:mm", new Locale("en"));
                text = gameweek.getName() + " deadline:\n" + dateFormat.format(deadline);
            }
        }
        upcomingDeadlineTextView.setText(text);
    }

    /**
     * Called from the view when the user clicks on the checkbox concerning the sound.
     */
    public void changeSoundSettings(View view) {
        mFplReminder.setNotificationSound(soundCheckbox.isChecked());
        if (soundCheckbox.isChecked()) {
            showSnackbar(getString(R.string.snackbar_text_soundon));
        } else {
            showSnackbar(getString(R.string.snackbar_text_soundoff));
        }
    }

    /**
     * Called from the view when the user clicks on the checkbox concerning the vibration.
     */
    public void changeVibrationSettings(View view) {
        mFplReminder.setNotificationVibration(vibrationCheckbox.isChecked());
        if (vibrationCheckbox.isChecked()) {
            showSnackbar(getString(R.string.snackbar_text_vibrationon));
        } else {
            showSnackbar(getString(R.string.snackbar_text_vibrationoff));
        }
    }

    public void showNetworkUnavailableSnackbar() {
        showSnackbar(getString(R.string.snackbar_text_nointernet));
    }
}
