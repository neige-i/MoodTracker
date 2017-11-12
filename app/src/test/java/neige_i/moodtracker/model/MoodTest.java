package neige_i.moodtracker.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoodTest {
    // ----------------------------------------     TEST METHODS     ----------------------------------------

    // To test the setSmiley and setCommentary methods, we just assign them a valid and non valid value.

    /**
     * Tests the {@link Mood#setSmiley(int)  setSmiley} method with a valid argument.
     * @throws Exception
     */
    @Test
    public void setSmiley_valid() throws Exception {
        assertTrue(isSmileyValid(4));
    }

    /**
     * Tests the {@link Mood#setSmiley(int)  setSmiley} method with a non valid argument.
     * @throws Exception
     */
    @Test
    public void setSmiley_outOfRange_invalid() throws Exception {
        assertFalse(isSmileyValid(10));
    }

    /**
     * Tests the {@link Mood#setCommentary(String)  setCommentary} method with a valid argument.
     * @throws Exception
     */
    @Test
    public void setCommentary_valid() throws Exception {
        assertTrue(isCommentaryValid("a commentary"));
    }

    /**
     * Tests the {@link Mood#setCommentary(String)  setCommentary} method with a non valid argument.
     * @throws Exception
     */
    @Test
    public void setCommentary_null_invalid() throws Exception {
        assertFalse(isCommentaryValid(null));
    }

    // To test the fromString method, we compare its returned value with
    // a Mood object which is instantiated with its constructor.
    // This constructor takes the expected values for the smiley and the commentary.

    /**
     * Tests the {@link Mood#fromString(String) fromString} method with a String argument that represents
     * a mood with a commentary.
     * @throws Exception
     */
    @Test
    public void fromString_moodWithCommentary() throws Exception {
        assertEquals(new Mood(4, "Feel great"), Mood.fromString("4Feel great"));
    }

    /**
     * Tests the {@link Mood#fromString(String) fromString} method with a String argument that represents
     * a mood without commentary.
     * @throws Exception
     */
    @Test
    public void fromString_moodWithoutCommentary() throws Exception {
        assertEquals(new Mood(2, ""), Mood.fromString("2"));
    }

    /**
     * Tests the {@link Mood#fromString(String) fromString} method with a String argument that represents
     * an empty mood.
     * @throws Exception
     */
    @Test
    public void fromString_emptyMood() throws Exception {
        assertEquals(new Mood(), Mood.fromString("9"));
    }

    // ---------------------------------------     PRIVATE METHODS     --------------------------------------

    /**
     * Returns true if the specified smiley is valid, false otherwise.
     * @param smiley The smiley to test.
     * @return true if smiley is valid, false otherwise.
     */
    private boolean isSmileyValid(int smiley) {
        try {
            new Mood().setSmiley(smiley);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Returns true if the specified commentary is valid, false otherwise.
     * @param commentary The commentary.
     * @return true if commentary is valid, false otherwise.
     */
    private boolean isCommentaryValid(String commentary) {
        try {
            new Mood().setCommentary(commentary);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}