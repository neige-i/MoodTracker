package neige_i.moodtracker.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import neige_i.moodtracker.controller.MainActivity;
import neige_i.moodtracker.controller.SmileyFragment;

/**
 * Created by neige-i on 20/10/2017.
 */

public class MoodPagerAdapter extends FragmentPagerAdapter {

    public MoodPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return SmileyFragment.newInstance(MainActivity.MOOD_DRAWABLES[position]);
    }

    @Override
    public int getCount() {
        return MainActivity.MOOD_COUNT;
    }
}