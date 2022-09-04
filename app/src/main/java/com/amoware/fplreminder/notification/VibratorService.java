package com.amoware.fplreminder.notification;

/**
 * Created by amoware on 2020-02-17.
 */
public class VibratorService {
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
