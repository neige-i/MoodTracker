package neige_i.moodtracker.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

import static neige_i.moodtracker.controller.MainActivity.PREF_KEY_MOOD;
import static neige_i.moodtracker.model.History.DAY_COUNT;

/**
 * This Receiver does only one thing: update the preferences.
 */
public class PrefUpdateReceiver extends BroadcastReceiver {
    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("controller.MainActivity", Context.MODE_PRIVATE);

        int currentDay = Calendar.getInstance().get(Calendar.DATE);
        Log.i("Receiver", "Day " + currentDay);
        if (preferences.getInt("Current day", 0) == 0)
            preferences.edit().putInt("Current day", currentDay).apply();
        else if (preferences.getInt("Current day", currentDay) != currentDay) {
            int i = 0;
            SharedPreferences.Editor editor = preferences.edit();
            String mood;

            while ((mood = preferences.getString(PREF_KEY_MOOD + (i++), null)) != null)
                if (i <= DAY_COUNT)
                    editor.putString(PREF_KEY_MOOD + i, mood);
            editor.putString(PREF_KEY_MOOD + 0, new Mood().toString());

            editor.putInt("Current day", currentDay);

            editor.apply();
            Log.i("Receiver", "update prefs");
        }
    }
}
