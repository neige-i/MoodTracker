package neige_i.moodtracker.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoodTest {
    // -------------------------------------     INSTANCE VARIABLES     -------------------------------------

    private Mood mActualMood;
    private Mood mExpectedMood;
    private boolean mIsValid;

    // ----------------------------------------     TEST METHODS     ----------------------------------------

    @Test
    public void setSmiley_valid() throws Exception {
        assertTrue(isSmileyValid(4));
    }

    @Test
    public void setSmiley_outOfRange_invalid() throws Exception {
        assertFalse(isSmileyValid(10));
    }

    @Test
    public void setCommentary_valid() throws Exception {
        assertTrue(isCommentaryValid("a commentary"));
    }

    @Test
    public void setCommentary_null_invalid() throws Exception {
        assertFalse(isCommentaryValid(null));
    }

    @Test
    public void fromString_fullMood() throws Exception {
        mActualMood = Mood.fromString("4Feel great");
        mExpectedMood = new Mood(4, "Feel great");
        assertEquals(mExpectedMood, mActualMood);
    }

    @Test
    public void fromString_moodWithoutCommentary() throws Exception {
        mActualMood = Mood.fromString("2");
        mExpectedMood = new Mood(2, "");
        assertEquals(mExpectedMood, mActualMood);
    }

    @Test
    public void fromString_emptyMood() throws Exception {
        mActualMood = Mood.fromString("9");
        mExpectedMood = new Mood();
        assertEquals(mExpectedMood, mActualMood);
    }

    // ---------------------------------------     PRIVATE METHODS     --------------------------------------

    private boolean isSmileyValid(int smiley) {
        try {
            new Mood().setSmiley(smiley);
            mIsValid = true;
        } catch (IndexOutOfBoundsException e) {
            mIsValid = false;
        }
        return mIsValid;
    }

    private boolean isCommentaryValid(String commentary) {
        try {
            new Mood().setCommentary(commentary);
            mIsValid = true;
        } catch (IllegalArgumentException e) {
            mIsValid = false;
        }
        return mIsValid;
    }
}