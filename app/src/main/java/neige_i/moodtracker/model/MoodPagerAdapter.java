package neige_i.moodtracker.model;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import neige_i.moodtracker.controller.SmileyFragment;

import static neige_i.moodtracker.controller.MainActivity.MOOD_DRAWABLES;
import static neige_i.moodtracker.model.Mood.MOOD_COUNT;

/**
 * This PagerAdapter allows the user to swipe between the different Fragments that show a unique mood.
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