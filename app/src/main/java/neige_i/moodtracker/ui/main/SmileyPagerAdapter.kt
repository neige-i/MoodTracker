package neige_i.moodtracker.model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import neige_i.moodtracker.controller.SmileyFragment;
import neige_i.moodtracker.ui.Smiley;

/**
 * This PagerAdapter allows the user to swipe between the different Fragments that show a unique mood.
 */
public class MoodPagerAdapter extends FragmentStateAdapter {

    private final Smiley[] allSmileys = Smiley.values();

    // ----------------------------------------     CONSTRUCTORS     ----------------------------------------

    public MoodPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return SmileyFragment.newInstance(allSmileys[position].getEmoji());
    }

    @Override
    public int getItemCount() {
        return allSmileys.length;
    }
}