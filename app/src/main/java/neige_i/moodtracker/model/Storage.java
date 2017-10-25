package neige_i.moodtracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neige-i on 25/10/2017.
 */

public class Storage {

    private List<Mood> mMoodList;

    public static final int DAY_COUNT = 7 + 1;

    public List<Mood> getMoodList() {
        return mMoodList;
    }

    public void initMoodList(String[] moods) {
        mMoodList = new ArrayList<>();

        for (String oneMood : moods) {
            mMoodList.add(oneMood == null ? new Mood() :
                    new Mood(Character.getNumericValue(oneMood.charAt(0)), oneMood.length() > 1 ? oneMood.substring(1) : null));

            if (mMoodList.size() == DAY_COUNT)
                break;
        }
    }

    public void updateMoodList() {
        mMoodList.add(0,new Mood());
        mMoodList.remove(DAY_COUNT);
    }


}
