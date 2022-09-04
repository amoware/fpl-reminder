package com.amoware.fplreminder.notification;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 * Created by amoware on 2020-02-17.
 */
public class VibratorService {

    private Vibrator vibrator;

    public VibratorService(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public VibratorService() {}

    public void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(500);
        }
    }

    /**
     * Returns a pattern used for vibration:
     * [0]: Start without a delay
     * [1]: Vibrate for 150 milliseconds
     * [2]: Pause for 100 milliseconds
     * [3]: Vibrate for 150 milliseconds
     */
    public long[] getDefaultVibratePattern() {
        return new long[]{ 0, 150, 100, 150 };
    }
}
