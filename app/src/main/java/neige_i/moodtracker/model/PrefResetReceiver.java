package neige_i.moodtracker.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by neige-i on 24/10/2017.
 */

public class PrefResetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.getSharedPreferences("controller.MainActivity", Context.MODE_PRIVATE)
                .edit().remove("mood").apply();
    }
}
