package neige_i.moodtracker.model;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by neige-i on 20/10/2017.
 */

public class MoodPager extends PagerAdapter { // FragmentPagerAdapter

    private ImageView mSmileyImage;

    public MoodPager(ImageView smileyImage) {
        mSmileyImage = smileyImage;
    }

    @Override
    public int getCount() {
        return Mood.MOOD_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mSmileyImage, 0);
        return mSmileyImage;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}