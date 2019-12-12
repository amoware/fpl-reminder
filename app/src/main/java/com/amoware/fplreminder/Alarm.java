package com.amoware.fplreminder;

import java.util.Date;

public class Alarm {

    private int id;
    private Date time;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
