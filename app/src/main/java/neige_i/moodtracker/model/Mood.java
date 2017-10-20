package neige_i.moodtracker.model;

import neige_i.moodtracker.R;

/**
 * Created by neige-i on 20/10/2017.
 */

public class Mood {

    public static final int MOOD_COUNT = 5;
    public static final int[] MOOD_DRAWABLES = new int[MOOD_COUNT];
    public static final int[] MOOD_COLOURS = new int[MOOD_COUNT];
    public static final int DEFAULT_MOOD = 3;

    public static void initDrawables() {
        MOOD_DRAWABLES[0] = R.drawable.smiley_sad;
        MOOD_DRAWABLES[1] = R.drawable.smiley_disappointed;
        MOOD_DRAWABLES[2] = R.drawable.smiley_normal;
        MOOD_DRAWABLES[3] = R.drawable.smiley_happy;
        MOOD_DRAWABLES[4] = R.drawable.smiley_super_happy;
    }

    public static void initColours() {
        MOOD_COLOURS[0] = R.color.faded_red;
        MOOD_COLOURS[1] = R.color.warm_grey;
        MOOD_COLOURS[2] = R.color.cornflower_blue_65;
        MOOD_COLOURS[3] = R.color.light_sage;
        MOOD_COLOURS[4] = R.color.banana_yellow;
    }
}
