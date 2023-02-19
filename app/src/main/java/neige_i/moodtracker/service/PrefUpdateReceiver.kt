package neige_i.moodtracker.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import neige_i.moodtracker.model.Mood
import neige_i.moodtracker.ui.history.HistoryActivity
import neige_i.moodtracker.ui.main.MainActivity

class PrefUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        updatePrefs(context.getSharedPreferences(MainActivity.PREF_FILE_NAME, Context.MODE_PRIVATE))
    }

    private fun updatePrefs(preferences: SharedPreferences) {
        var i = 0
        var mood: String?
        val editor = preferences.edit()

        // Get all the available moods from the preferences
        while (preferences.getString(MainActivity.PREF_KEY_MOOD + i++, null)
                .also { mood = it } != null && i <= HistoryActivity.DAY_COUNT
        ) editor.putString(
            MainActivity.PREF_KEY_MOOD + i, mood
        ) // Copy each mood into the next one
        editor.putString(MainActivity.PREF_KEY_MOOD + 0, Mood().toString()) // Reset the first mood with the empty value
        editor.apply() // Apply all modifications
    }
}