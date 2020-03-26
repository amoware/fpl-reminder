package com.amoware.fplreminder;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amoware.fplreminder.alarm.AlarmsManager;
import com.amoware.fplreminder.common.Time;
import com.amoware.fplreminder.common.TypefaceUtil;
import com.amoware.fplreminder.dialog.ReminderDialog;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.gameweek.GameweeksTask;
import com.amoware.fplreminder.gameweek.GameweeksTaskInterface;

import java.util.Date;
import java.util.List;

import static com.amoware.fplreminder.common.Constants.tagger;

/**
 * Created by amoware on 2019-12-29.
 */
public class MainActivity extends AppCompatActivity implements GameweeksTaskInterface {

    private ReminderDialog dialog;

    private FPLReminder fplReminder;
    private Gameweek currentGameweek;

    private TextView hoursTextView;
    private TextView minutesTextView;

    public void displayCountdownTimer(){
    }

    public void displayCurrentGameweek(){
    }

    public void displayNotificationPreferences() {
    }

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

        hoursTextView.setTypeface(boldTypeface);
        minutesTextView.setTypeface(boldTypeface);

        ((TextView) findViewById(R.id.main_title_textview)).setTypeface(boldTypeface);

        ((TextView) findViewById(R.id.main_timer_label_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_hours_label_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_minutes_label_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_colon_label_textview)).setTypeface(boldTypeface);
        ((TextView) findViewById(R.id.main_suffixtimer_label_textview)).setTypeface(boldTypeface);

        ((TextView) findViewById(R.id.main_preferences_label_textview)).setTypeface(boldTypeface);
        ((CheckBox) findViewById(R.id.main_sound_checkbox)).setTypeface(boldTypeface);
        ((CheckBox) findViewById(R.id.main_vibration_checkbox)).setTypeface(boldTypeface);

        displayNotificationTimer(fplReminder.getNotificationTimer());
    }

    public void showReminderDialog(View view) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new ReminderDialog(this);
            dialog.show();

            dialog.setGameweek(currentGameweek);
            dialog.setTime(fplReminder.getNotificationTimer());
            dialog.setOnTimeSelected((time) -> {
                setAlarmBasedOnSelectedTime(time);
                displayNotificationTimer(time);
            });
        }
    }

    private void setAlarmBasedOnSelectedTime(Time time) {
        if (currentGameweek != null) {
            // Only set reminder when there's a current gameweek
            fplReminder.setNotificationTimer(currentGameweek.getDeadlineTime(), time);
        }
    }

    private void displayNotificationTimer(Time time) {
        if (time != null && hoursTextView != null && minutesTextView != null) {
            hoursTextView.setText(String.valueOf(time.getHours()));
            minutesTextView.setText(String.valueOf(time.getMinutes()));
        }
    }

    @Override
    public void onGameweeksDownloaded(List<Gameweek> gameweeks) {
        Log.d(tagger(getClass()), "Gameweeks from FPL: " + gameweeks);

        Date todaysDate = new Date();
        AlarmsManager alarmsManager = new AlarmsManager(this);

        for (Gameweek gameweek : gameweeks) {
            if (todaysDate.compareTo(gameweek.getDeadlineTime()) < 0) {
                currentGameweek = new Gameweek(gameweek);
                alarmsManager.setAlarmForGameweekDeadline(currentGameweek.getDeadlineTime());
                break;
            }
        }
    }
}
