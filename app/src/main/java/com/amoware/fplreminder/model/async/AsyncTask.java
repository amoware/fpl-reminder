package com.amoware.fplreminder.model.async;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by amoware on 2022-09-11.
 */
public abstract class AsyncTask<Params, Result> {
    private final ExecutorService executor;
    private Handler handler;

    protected AsyncTask() {
        executor = Executors.newSingleThreadExecutor((r) -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
    }

    public Handler getHandler() {
        if (handler == null) {
            synchronized (AsyncTask.class) {
                handler = new Handler(Looper.getMainLooper());
            }
        }
        return handler;
    }

    protected abstract Result doInBackground(Params params);

    protected abstract void onPostExecute(Result result);

    public void execute() {
        execute(null);
    }

    public void execute(Params params) {
        getHandler().post(() -> executor.execute(() -> {
            Result result = doInBackground(params);
            getHandler().post(() -> onPostExecute(result));
        }));
    }
}
