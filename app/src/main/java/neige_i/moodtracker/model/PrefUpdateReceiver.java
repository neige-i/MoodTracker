package neige_i.moodtracker.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static neige_i.moodtracker.controller.HistoryActivity.DAY_COUNT;
import static neige_i.moodtracker.controller.MainActivity.PREF_FILE_NAME;
import static neige_i.moodtracker.controller.MainActivity.PREF_KEY_MOOD;

/**
 * This Receiver does only one thing: update the preferences.
 */
public class PrefUpdateReceiver extends BroadcastReceiver {

    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    @Override
    public void onReceive(Context context, Intent intent) {
        updatePrefs(context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE));
    }

    // ---------------------------------------     PRIVATE METHODS     --------------------------------------

    /**
     * Updates the specified preferences. Resets the mood of the current day after adding it to the history.
     * @param preferences the preferences to update.
     */
    private void updatePrefs(SharedPreferences preferences) {
        int i = 0;
        String mood;
        SharedPreferences.Editor editor = preferences.edit();

        // Get all the available moods from the preferences
        while ((mood = preferences.getString(PREF_KEY_MOOD + (i++), null)) != null && i <= DAY_COUNT)
            editor.putString(PREF_KEY_MOOD + i, mood); // Copy each mood into the next one

        editor.putString(PREF_KEY_MOOD + 0, new Mood().toString()); // Reset the first mood with the empty value

        editor.apply(); // Apply all modifications
    }
}
