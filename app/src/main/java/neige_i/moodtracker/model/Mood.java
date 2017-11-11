package neige_i.moodtracker.model;

/**
 * This class represents the main object of the app.
 * A mood is defined by 2 elements: a smiley and a commentary.
 */
public class Mood {
    // -------------------------------------     INSTANCE VARIABLES     -------------------------------------

    /**
     * Smiley value. The higher the value, the happier the smiley is.
     * Its value is between 0 inclusive and {@link #MOOD_COUNT} exclusive.
     * It can also equal to {@link #MOOD_EMPTY} if no mood has been selected yet by the user.
     * @see #getSmiley()
     * @see #setSmiley(int)
     * @see #MOOD_COUNT
     * @see #MOOD_EMPTY
     */
    private int mSmiley;
    /**
     * Commentary value. If empty, then no commentary is provided.
     * @see #getCommentary()
     * @see #setCommentary(String)
     */
    private String mCommentary;

    // ---------------------------------------     CLASS VARIABLES     --------------------------------------

    /**
     * Number of available moods.
     */
    public static final int MOOD_COUNT = 5;
    /**
     * Default mood value. This is the mood that is displayed the first time the app is started in the day.
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
        mCommentary = "";
    }

    /**
     * Initializes a mood with the specified arguments.
     * @param smiley        the smiley value.
     * @param commentary    the commentary value.
     */
    Mood(int smiley, String commentary) {
        setSmiley(smiley);
        setCommentary(commentary);
    }

    // --------------------------------------     GETTERS & SETTERS     -------------------------------------

    public int getSmiley() {
        return mSmiley;
    }

    /**
     * Sets the smiley value.
     * @param smiley the smiley value.
     * @throws IndexOutOfBoundsException    if the specified smiley is not between 0 and {@link #MOOD_COUNT}
     *                                      or is not equal to {@link #MOOD_EMPTY}.
     */
    public void setSmiley(int smiley) {
        if ((smiley < 0 || smiley >= MOOD_COUNT) && smiley != MOOD_EMPTY)
            throw new IndexOutOfBoundsException("The smiley value must be in the following range [0;" + MOOD_COUNT + '[');
        mSmiley = smiley;
    }

    public String getCommentary() {
        return mCommentary;
    }

    /**
     * Sets the commentary value.
     * @param commentary the commentary value.
     * @throws IllegalArgumentException if the specified commentary is null.
     */
    public void setCommentary(String commentary) {
        if (commentary == null)
            throw new IllegalArgumentException("A null String is not allowed as commentary");
        mCommentary = commentary;
    }

    // -------------------------------------     OVERRIDDEN METHODS     -------------------------------------

    /**
     * Returns the String representation of this object. This String will be used during loading and saving process.<br />
     * The first character represents the smiley. The rest of the String represents the commentary (if not empty).<br />
     * Here are some examples of returned values for given Mood objects:
     * <ul>
     *     <li>new Mood(1, "foo") --> "1foo"</li>
     *     <li>new Mood(3, "")  -------> "3" </li>
     *     <li>new Mood()         -------------> "{@value #MOOD_EMPTY }"</li>
     * </ul>
     * This method is the opposite of {@link #fromString(String)}.
     * @return the String representation of this Mood object.
     * @see #fromString(String)
     */
    @Override
    public String toString() {
        return mSmiley + mCommentary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mood mood = (Mood) o;

        return mSmiley == mood.mSmiley && mCommentary.equals(mood.mCommentary);
    }

    // ---------------------------------------     STATIC METHODS     ---------------------------------------

    /**
     * Converts a String into a Mood object. This method is the opposite of {@link #toString()}.
     * @param moodString the String to convert.
     * @return the Mood object that is obtained by converting the specified String.
     * @see #toString()
     * @throws IllegalArgumentException if the specified moodString is null or empty.
     */
    public static Mood fromString(String moodString) {
        if (moodString == null)
            throw new IllegalArgumentException("A null String cannot be converted into a Mood object");
        else if (moodString.isEmpty())
            throw new IllegalArgumentException("An empty String cannot be converted into a Mood object");
        return new Mood(Character.getNumericValue(moodString.charAt(0)), moodString.substring(1));
    }
}
