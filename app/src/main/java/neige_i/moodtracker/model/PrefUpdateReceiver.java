package neige_i.moodtracker.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This Receiver does only one thing: update the preferences.
 */
public class PrefUpdateReceiver extends BroadcastReceiver {
    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    @Override
    public void onReceive(Context context, Intent intent) {
        context.getSharedPreferences("controller.MainActivity", Context.MODE_PRIVATE)
                .edit().remove("mood").apply();
    }
}
