package com.amoware.fplreminder;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.amoware.fplreminder.alarm.AlarmsManager;
import com.amoware.fplreminder.common.DateUtil;
import com.amoware.fplreminder.common.PreferenceManager;
import com.amoware.fplreminder.common.Time;
import com.amoware.fplreminder.dialog.ReminderDialog;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.gameweek.GameweekManager;
import com.amoware.fplreminder.gameweek.GameweeksTask;
import com.amoware.fplreminder.gameweek.GameweeksTaskInterface;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.amoware.fplreminder.App.CHANNEL_1_ID;
import static com.amoware.fplreminder.common.Constants.REMINDER_PREFERENCE;
import static com.amoware.fplreminder.common.Constants.tagger;

/**
 * Created by amoware on 2019-12-29.
 */
public class MainActivity extends AppCompatActivity implements GameweeksTaskInterface {

    private ReminderDialog dialog;
    private GameweekManager gameweekManager;
    private NotificationSettings notificationSettings;

    private Gameweek currentGameweek;
    private Date currentDeadlineTime;
    private Time timeBeforeDeadlineTime;

    public void displayNotificationTimer(){
    }

    public void displayCountdownTimer(){
    }

    public void displayCurrentGameweek(){
    }

    public void displayNotificationPreferences(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameweeksTask task = new GameweeksTask(this);
        task.execute();
    }


    public void sendOnChannel1(View v){
        String title = null;
        EditText editTextTitle = findViewById(R.id.edit_text_title);
        if (editTextTitle != null) {
            title = editTextTitle.getText().toString();
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_1)
                .setContentTitle(title)
                .setContentText(title)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notification);

        VibratorService service = new VibratorService(this);
        service.vibrate();
    }

    private Date generateDate(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,seconds);
        return calendar.getTime();
    }

    public void showReminderDialog(View view) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new ReminderDialog(this);
            dialog.show();

            PreferenceManager preferenceManager = new PreferenceManager(this);
            String jsonString = preferenceManager.getString(REMINDER_PREFERENCE, null);

            timeBeforeDeadlineTime = Time.parseTime(jsonString);

            dialog.setGameweek(currentGameweek);
            dialog.setTime(timeBeforeDeadlineTime);
            dialog.setOnTimeSelected((time) -> {
                setAlarmBasedOnSelectedTime(time);
                updateGraphicalUserInterface();
            });
        }
    }

    private void setAlarmBasedOnSelectedTime(Time time) {
        this.timeBeforeDeadlineTime = time;

        Log.d(tagger(getClass()), time.toJsonString());
        PreferenceManager preferenceManager = new PreferenceManager(this);
        preferenceManager.putString(REMINDER_PREFERENCE, time.toJsonString());

        Log.d(tagger(getClass()), "Gameweek deadline: " + currentDeadlineTime
                + ", time selected: " + time);

        Date notificationDate = DateUtil.subtractTime(currentDeadlineTime, timeBeforeDeadlineTime);
        AlarmsManager alarmsManager = new AlarmsManager(this);
        alarmsManager.setAlarmForNotificationToBeShown(notificationDate);
    }

    private void updateGraphicalUserInterface() {

    }

    @Override
    public void onGameweeksDownloaded(List<Gameweek> gameweeks) {
        Log.d(tagger(MainActivity.class), "Gameweeks from FPL: " + gameweeks);

        Date todaysDate = new Date();
        AlarmsManager alarmsManager = new AlarmsManager(MainActivity.this);

        for (Gameweek gameweek : gameweeks) {
            if (todaysDate.compareTo(gameweek.getDeadlineTime()) < 0) {
                currentGameweek = new Gameweek(gameweek);
                currentDeadlineTime = new Date(gameweek.getDeadlineTime().getTime());
                alarmsManager.setAlarmForGameweekDeadline(currentDeadlineTime);
                break;
            }
        }
    }
}
