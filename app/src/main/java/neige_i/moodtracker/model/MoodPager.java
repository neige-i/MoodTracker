package neige_i.moodtracker.model;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import neige_i.moodtracker.R;

/**
 * Created by neige-i on 20/10/2017.
 */

public class MoodPager extends PagerAdapter {

    private Context mContext;

    public MoodPager(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(R.drawable.smiley_happy);
        imageView.setAdjustViewBounds(true);
//            ImageView imageView = (ImageView) findViewById(R.id.main_activity_mood_img);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}