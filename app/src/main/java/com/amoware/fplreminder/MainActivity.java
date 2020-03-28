package com.amoware.fplreminder;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amoware.fplreminder.common.FPLReminder;
import com.amoware.fplreminder.common.Time;
import com.amoware.fplreminder.common.TypefaceUtil;
import com.amoware.fplreminder.dialog.ReminderDialog;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.gameweek.GameweeksTask;
import com.amoware.fplreminder.gameweek.GameweeksTaskInterface;

import java.util.List;

import static com.amoware.fplreminder.common.Constants.tagger;

/**
 * Created by amoware on 2019-12-29.
 */
public class MainActivity extends AppCompatActivity implements GameweeksTaskInterface {

    private ReminderDialog dialog;

    private FPLReminder fplReminder;

    private TextView hoursTextView;
    private TextView minutesTextView;
    private TextView suffixTimerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureContentView();

        GameweeksTask task = new GameweeksTask(this);
        task.execute();
    }

    private void configureContentView() {
        fplReminder = new FPLReminder(this);

        Typeface boldTypeface = TypefaceUtil.getBoldTypeface(this);
        hoursTextView = findViewById(R.id.main_hours_value_textview);
        minutesTextView = findViewById(R.id.main_minutes_value_textview);
        suffixTimerTextView = findViewById(R.id.main_suffixtimer_label_textview);

        hoursTextView.setTypeface(boldTypeface);
        minutesTextView.setTypeface(boldTypeface);
        suffixTimerTextView.setTypeface(boldTypeface);

        ((TextView) findViewById(R.id.main_title_textview)).setTypeface(boldTypeface);

        ((TextView) findViewById(R.id.main_timer_label_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_hours_label_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_minutes_label_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_colon_label_textview)).setTypeface(boldTypeface);

        ((TextView) findViewById(R.id.main_preferences_label_textview)).setTypeface(boldTypeface);
        ((CheckBox) findViewById(R.id.main_sound_checkbox)).setTypeface(boldTypeface);
        ((CheckBox) findViewById(R.id.main_vibration_checkbox)).setTypeface(boldTypeface);

        displayNotificationTimer(fplReminder.getNotificationTimer());
    }

    private void displayNotificationTimer(Time time) {
        if (time != null && hoursTextView != null && minutesTextView != null) {
            hoursTextView.setText(String.valueOf(time.getHours()));
            minutesTextView.setText(String.valueOf(time.getMinutes()));
        }
        displayNotificationTimerText();
    }

    private void displayNotificationTimerText() {
        String value = getString(R.string.common_word_gameweek);
        if (fplReminder.getCurrentGameweek() != null) {
            value = fplReminder.getCurrentGameweek().getName() + "'s";
        }
        suffixTimerTextView.setText(getString(R.string.main_text_beforedeadline, value));
    }

    public void showReminderDialog(View view) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new ReminderDialog(this);
            dialog.show();

            dialog.setGameweek(fplReminder.getCurrentGameweek());
            dialog.setTime(fplReminder.getNotificationTimer());
            dialog.setOnTimeSelected((time) -> {
                fplReminder.setNotificationTimer(time);
                displayNotificationTimer(time);
            });
        }
    }

    @Override
    public void onGameweeksDownloaded(List<Gameweek> gameweeks) {
        Log.d(tagger(getClass()), "Gameweeks from FPL: " + gameweeks);
        fplReminder.onGameweeksDownloaded(gameweeks);
        displayNotificationTimerText();
    }
}
