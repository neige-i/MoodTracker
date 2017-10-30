package neige_i.moodtracker.model;

import java.util.ArrayList;
import java.util.List;

import static neige_i.moodtracker.model.Mood.MOOD_EMPTY;

/**
 * This class manages the mood of the current day and the mood history.
 * Everything is done through the List variable.
 */
public class Storage {
    // -------------------------------------     INSTANCE VARIABLES     -------------------------------------

    /**
     * List of Mood objects.
     * Its first element corresponds to the mood of the current day.
     * The rest of the list corresponds to the mood history.
     * @see #initMoodList(List)
     * @see #updateMoodList()
     */
    private List<Mood> mMoodList;

    // ---------------------------------------     CLASS VARIABLES     --------------------------------------

    /**
     * Number of moods that will be saved in history, plus the one of the current day.
     * For example, if the application should store the last 10 moods, then DAY_COUNT equals to 10 + 1.
     */
    public static final int DAY_COUNT = 7 + 1;

    // --------------------------------------     GETTERS & SETTERS     -------------------------------------

    public List<Mood> getMoodList() {
        return mMoodList;
    }

    // ----------------------------------------     OTHER METHODS     ---------------------------------------

    /**
     * Initializes the {@link #mMoodList} with the specified String list.
     * In a normal configuration, the String objects of the specified list are obtained with the
     * {@link Mood#toString()} method.
     * @param moodHistory the String list containing the mood history.
     */
    public void initMoodList(List<String> moodHistory) {
        mMoodList = new ArrayList<>();

        for (String oneMood : moodHistory) {
            // For each mood of the history, see if it corresponds to an empty one or to a normal one
            // Then, add it to the list.
            int smiley = Character.getNumericValue(oneMood.charAt(0));
            mMoodList.add(new Mood(smiley, oneMood.substring(1)));

            // If the maximum size of the list is reached, exit the loop and ignore the rest of the history.
            // This instruction is just to prevent getting a greater number of moods than expected.
            // But this kind of situation should never happen.
            if (mMoodList.size() == DAY_COUNT)
                break;
        }

        // Add the empty mood if there is no saved preferences
        if (mMoodList.size() == 0)
            mMoodList.add(new Mood());
    }

    /**
     * Updates the {@link #mMoodList} by adding an empty mood at its first element
     * and removing the last one if its size is greater than {@link #DAY_COUNT}.
     */
    public void updateMoodList() {
        mMoodList.add(0, new Mood());
        if (mMoodList.size() > DAY_COUNT)
            mMoodList.remove(DAY_COUNT);
    }


}
