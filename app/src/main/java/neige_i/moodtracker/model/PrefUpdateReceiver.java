package neige_i.moodtracker.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

import static neige_i.moodtracker.controller.HistoryActivity.DAY_COUNT;
import static neige_i.moodtracker.controller.MainActivity.PREF_FILE_NAME;
import static neige_i.moodtracker.controller.MainActivity.PREF_KEY_MOOD;

/**
 * This Receiver does only one thing: update the preferences.
 */
public class PrefUpdateReceiver extends BroadcastReceiver {
    // ---------------------------------------     CLASS VARIABLES     --------------------------------------

    /**
     * Constant for storing the current day in the preferences.
     */
    private static final String PREF_KEY_CURRENT_DAY = "PREF_KEY_CURRENT_DAY";

    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        int currentDay = Calendar.getInstance().get(Calendar.DATE);

        Log.i("Update_Receiver", "Called for the day " + currentDay); // Log to check if this class is called

        // If no current day is saved in preferences
        if (preferences.getInt(PREF_KEY_CURRENT_DAY, 0) == 0)
            preferences.edit().putInt(PREF_KEY_CURRENT_DAY, currentDay).apply();
        // As updates occur at midnight, need to check if the day has changed to perform updates
        // This verification is normally not necessary, but this Receiver is called at each app launch
        // If this Receiver or the Alarm is correctly set, just remove the present conditions and the associated variables
        else if (preferences.getInt(PREF_KEY_CURRENT_DAY, currentDay) != currentDay) {
            updatePrefs(preferences, currentDay);
        }
    }

    // ---------------------------------------     PRIVATE METHODS     --------------------------------------

    /**
     * Updates the specified preferences. Resets the mood of the current day after adding it to the history.
     * @param preferences the preferences to update.
     * @param currentDay the value of the current day to put in preferences.
     */
    private void updatePrefs(SharedPreferences preferences, int currentDay) {
        int i = 0;
        String mood;
        SharedPreferences.Editor editor = preferences.edit();

        // Get all the available moods from the preferences
        while ((mood = preferences.getString(PREF_KEY_MOOD + (i++), null)) != null && i <= DAY_COUNT)
            editor.putString(PREF_KEY_MOOD + i, mood); // Copy each mood into the next one

        editor.putString(PREF_KEY_MOOD + 0, new Mood().toString()); // Reset the first mood with the empty value

        editor.putInt(PREF_KEY_CURRENT_DAY, currentDay); // Update the currentDay preferences

        editor.apply(); // Apply all modifications

        Log.i("Update_Receiver", "Prefs updated"); // Log to check if this method is called
    }
}
