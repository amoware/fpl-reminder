package com.amoware.fplreminder.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.amoware.fplreminder.MainActivity;

public class ConnectionHandler{
    Context context;

    public ConnectionHandler(Context context){
        this.context=context;
    }

    // This method check to see if there is a connection to the internet
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        else
            return false;
    }
}

