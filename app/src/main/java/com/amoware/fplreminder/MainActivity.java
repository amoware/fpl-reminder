package com.amoware.fplreminder;

import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.amoware.fplreminder.alarm.AlarmsManager;

import java.util.Calendar;
import java.util.Date;

import static com.amoware.fplreminder.App.CHANNEL_1_ID;

/**
 * Created by amoware on 2019-12-29.
 */
public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    //private EditText editTextMessage;

    private NotificationSettings notificationSettings;
    private GameweekManager gameweekManager;

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

        notificationManager = NotificationManagerCompat.from(this);

        editTextTitle = findViewById(R.id.edit_text_title);
       // editTextMessage = findViewById(R.id.edit);

        /*
        GameweeksTask task = new GameweeksTask(new GameweeksTaskInterface() {
            @Override
            public void onGameweeksDownloaded(List<Gameweek> gameweeks) {
                Log.d("GameweekClient", "Gameweeks from FPL: " + gameweeks);
            }
        });
        task.execute();
         */



        /*try {
            // https://stackoverflow.com/questions/22395417/error-strictmodeandroidblockguardpolicy-onnetwork
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            GameweekClient gameweekClient = new GameweekClient();

            String apiString = "{\"events\":[{\"id\":1,\"name\":\"Gameweek 1\",\"deadline_time\":\"2019-08-09T18:00:00Z\",\"average_entry_score\":65,\"finished\":true,\"data_checked\":false,\"highest_scoring_entry\":3493085,\"deadline_time_epoch\":1565373600,\"deadline_time_game_offset\":0,\"highest_score\":142,\"is_previous\":false,\"is_current\":false,\"is_next\":false,\"chip_plays\":[{\"chip_name\":\"bboost\",\"num_played\":128770},{\"chip_name\":\"3xc\",\"num_played\":271367}],\"most_selected\":183,\"most_transferred_in\":1,\"top_element\":214,\"top_element_info\":{\"id\":214,\"points\":20},\"transfers_made\":0,\"most_captained\":191,\"most_vice_captained\":189},{\"id\":2,\"name\":\"Gameweek 2\",\"deadline_time\":\"2019-08-17T10:30:00Z\",\"average_entry_score\":41,\"finished\":true,\"data_checked\":false,\"highest_scoring_entry\":6106693,\"deadline_time_epoch\":1566037800,\"deadline_time_game_offset\":0,\"highest_score\":119,\"is_previous\":false,\"is_current\":false,\"is_next\":false,\"chip_plays\":[{\"chip_name\":\"bboost\",\"num_played\":114585},{\"chip_name\":\"freehit\",\"num_played\":89437},{\"chip_name\":\"wildcard\",\"num_played\":263066},{\"chip_name\":\"3xc\",\"num_played\":216711}],\"most_selected\":183,\"most_transferred_in\":235,\"top_element\":278,\"top_element_info\":{\"id\":278,\"points\":17},\"transfers_made\":9998839,\"most_captained\":191,\"most_vice_captained\":214},{\"id\":3,\"name\":\"Gameweek 3\",\"deadline_time\":\"2019-08-23T18:00:00Z\",\"average_entry_score\":44,\"finished\":true,\"data_checked\":false,\"highest_scoring_entry\":5087809,\"deadline_time_epoch\":1566583200,\"deadline_time_game_offset\":0,\"highest_score\":107,\"is_previous\":false,\"is_current\":false,\"is_next\":false,\"chip_plays\":[{\"chip_name\":\"bboost\",\"num_played\":79958},{\"chip_name\":\"freehit\",\"num_played\":104304},{\"chip_name\":\"wildcard\",\"num_played\":414347},{\"chip_name\":\"3xc\",\"num_played\":166101}],\"most_selected\":183,\"most_transferred_in\":278,\"top_element\":191,\"top_element_info\":{\"id\":191,\"points\":15},\"transfers_made\":13387638,\"most_captained\":214,\"most_vice_captained\":191}]}";
            List<Gameweek> gameweeks = gameweekClient.stringToListConverter(apiString);
            Log.d("GameweekClient", "Gameweeks from string: " + gameweeks);


            gameweeks = gameweekClient.getGameweeksFromFPL();
            Log.d("GameweekClient", "Gameweeks from FPL: " + gameweeks);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Todo remove temporary code
        AlarmsManager alarmsManager = new AlarmsManager(this);
        alarmsManager.setAlarmForGameweekDeadline(generateDate(30));
        alarmsManager.setAlarmForGameweekDeadline(generateDate(15)); // Overwrites alarm above
        alarmsManager.setAlarmForNotificationToBeShown(generateDate(30));*/

        AlarmsManager alarmsManager = new AlarmsManager(this);
        alarmsManager.setAlarmForNotificationToBeShown(generateDate(30));
    }


    public void sendOnChannel1(View v){
        String title = null;
        if (editTextTitle != null) {
            title = editTextTitle.getText().toString();
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_1)
                .setContentTitle(title)
                .setContentText(title)
                .build();

        notificationManager.notify(1, notification);

        VibratorService service = new VibratorService(this);
        service.vibrate();
    }

    private Date generateDate(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,seconds);
        return calendar.getTime();
    }
}
