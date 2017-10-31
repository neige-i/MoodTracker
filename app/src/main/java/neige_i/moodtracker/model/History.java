package neige_i.moodtracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the mood history.
 * Everything is done through the List variable.
 */
public class History {
    // -------------------------------------     INSTANCE VARIABLES     -------------------------------------

    /**
     * List of moods that are saved in the history.
     * The moods are sorted from the newest to the oldest.
     * @see #initHistory(List)
     * @see #addMoodToHistory(Mood)
     */
    private List<Mood> mMoodList;

    // ---------------------------------------     CLASS VARIABLES     --------------------------------------

    /**
     * Number of moods that will be saved in history.
     */
    public static final int DAY_COUNT = 7;

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
     * @throws IllegalArgumentException if the size of the specified list is greater than {@link #DAY_COUNT}.
     */
    public void initHistory(List<String> moodHistory) {
        if (moodHistory.size() > DAY_COUNT)
            throw new IllegalArgumentException("The specified list has too many elements");

        mMoodList = new ArrayList<>();
        for (String oneMood : moodHistory)
            mMoodList.add(Mood.fromString(oneMood));
    }

    /**
     * Adds the specified mood to the history.
     * @param moodToAdd the mood to add.
     */
    public void addMoodToHistory(Mood moodToAdd) {
        mMoodList.add(0, moodToAdd); // Add the specified mood at the beginning of the list

        // If the history is full, remove the oldest mood (i.e. the last element)
        if (mMoodList.size() > DAY_COUNT)
            mMoodList.remove(DAY_COUNT);
    }
}
