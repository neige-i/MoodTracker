package neige_i.moodtracker.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import neige_i.moodtracker.controller.SmileyFragment;

import static neige_i.moodtracker.controller.MainActivity.MOOD_DRAWABLES;
import static neige_i.moodtracker.model.Mood.MOOD_COUNT;

/**
 * This PagerAdapter allows user to swipe between the different moods.
 */
public class MoodPagerAdapter extends FragmentPagerAdapter {
    // ----------------------------------------     CONSTRUCTORS     ----------------------------------------

    public MoodPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    @Override
    public Fragment getItem(int position) {
        return SmileyFragment.newInstance(MOOD_DRAWABLES[position]);
    }

    @Override
    public int getCount() {
        return MOOD_COUNT;
    }
}