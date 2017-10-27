package neige_i.moodtracker.model;

import java.util.List;

/**
 * This class represents the main object of the app.
 * A mood is defined by 2 elements: a smiley and a commentary.
 */
public class Mood {
    // -------------------------------------     INSTANCE VARIABLES     -------------------------------------

    /**
     * Smiley value. The higher the value, the happier the smiley is.
     * Its value is between 0 inclusive and {@value #MOOD_COUNT} exclusive.
     * It can also equal to {@link #MOOD_EMPTY} if no mood has been selected yet by the user.
     * @see #getSmiley()
     * @see #setSmiley(int)
     * @see #MOOD_COUNT
     * @see #MOOD_EMPTY
     */
    private int mSmiley;
    /**
     * Commentary value. If null, then no commentary has been entered by the user.
     * @see #getCommentary()
     * @see #setCommentary(String)
     */
    private String mCommentary;

    // ---------------------------------------     CLASS VARIABLES     --------------------------------------

    /**
     * Number of available moods in the application.
     */
    public static final int MOOD_COUNT = 5;
    /**
     * Default mood value. This is the mood that is displayed at first application start in the day.
     */
    public static final int MOOD_DEFAULT = 3;
    /**
     * Empty mood value. This variable is useful when the user does not open the app for an entire day.
     * Indeed, in this situation, saving the mood of that day (whatever its value) would not be appropriate
     * as the user did not choose one.<br />
     * <strong>NOTE:</strong> the value of this constant must be greater than {@link #MOOD_COUNT}.
     */
    public static final int MOOD_EMPTY = 9;

    // ----------------------------------------     CONSTRUCTORS     ----------------------------------------

    /**
     * Initializes an empty mood.
     */
    public Mood() {
        mSmiley = MOOD_EMPTY;
        mCommentary = null;
    }

    /**
     * Initializes a mood with the specified arguments.
     * @param smiley        the smiley value.
     * @param commentary    the commentary value.
     */
    public Mood(int smiley, String commentary) {
        setSmiley(smiley);
        mCommentary = commentary;
    }

    // --------------------------------------     GETTERS & SETTERS     -------------------------------------

    public int getSmiley() {
        return mSmiley;
    }

    /**
     * Sets the smiley value.
     * @throws IndexOutOfBoundsException    if the specified smiley is not between 0 and {@link #MOOD_COUNT}
     *                                      or is not equal to {@link #MOOD_EMPTY}.
     * @param smiley the smiley value.
     */
    public void setSmiley(int smiley) {
        mSmiley = MOOD_DEFAULT;
        if ((smiley < 0 || smiley >= MOOD_COUNT) && smiley != MOOD_EMPTY)
            throw new IndexOutOfBoundsException("The value must be in the following range [0;" + MOOD_COUNT + "[.");
        mSmiley = smiley;
    }

    public String getCommentary() {
        return mCommentary;
    }

    public void setCommentary(String commentary) {
        mCommentary = commentary;
    }

    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    /**
     * Returns the String representation of this object. This String will be used during loading and saving process.<br />
     * The first character represents the smiley. The rest of the String represents the commentary (if not null).<br />
     * Here are some examples of returned values for given Mood objects:
     * <ul>
     *     <li>new Mood(1, "foo") --> "1foo"</li>
     *     <li>new Mood(3, null)  ----> "3" </li>
     *     <li>new Mood()         -------------> "{@value #MOOD_EMPTY }"</li>
     * </ul>
     * @return the representation of this Mood object.
     * @see Storage#initMoodList(List)  initMoodList(List)
     */
    @Override
    public String toString() {
        return mSmiley + (mCommentary != null ? mCommentary : "");
    }
}
