package com.amoware.fplreminder.notification;

import android.net.Uri;

import java.util.Arrays;

/**
 * Created by amoware on 2020-03-30.
 */
public class Notification {

    private Uri soundUri;

    private String contentTitle;
    private String contentText;

    private long[] vibrationPattern;

    public Uri getSoundUri() {
        return soundUri;
    }

    public void setSound(Uri soundUri) {
        this.soundUri = soundUri;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public long[] getVibrationPattern() {
        return vibrationPattern;
    }

    public void setVibrationPattern(long[] vibrationPattern) {
        if (vibrationPattern != null) {
            this.vibrationPattern = Arrays.copyOf(vibrationPattern, vibrationPattern.length);
        }
    }
}
