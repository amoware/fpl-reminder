package com.amoware.fplreminder;

import java.util.Date;

/**
 * Created by amoware on 2019-12-12.
 */
public class NotificationSettings {

    private Date notificationTime;
    private Date deadlineTime;
    private Time scrollTime;
    private boolean notificationSound;
    private boolean notificationVibration;

    public Date getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    public Date getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(Date deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public Time getScrollTime() {
        return scrollTime;
    }

    public void setScrollTime(Time scrollTime) {
        this.scrollTime = scrollTime;
    }

    public boolean isNotificationSound() {
        return notificationSound;
    }

    public void setNotificationSound(boolean notificationSound) {
        this.notificationSound = notificationSound;
    }

    public boolean isNotificationVibration() {
        return notificationVibration;
    }

    public void setNotificationVibration(boolean notificationVibration) {
        this.notificationVibration = notificationVibration;
    }

    /**
     * Calculates the date for the notification to be set. The date depends on the deadline of a
     * particular gameweek and a desired time before the deadline.
     */
    public Date calculateNotificationTime(Date deadlineTime, Time scrollTime) {
        return null;
    }
}
