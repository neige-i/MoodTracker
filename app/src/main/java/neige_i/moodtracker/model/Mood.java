package neige_i.moodtracker.model;

import static neige_i.moodtracker.controller.MainActivity.DEFAULT_MOOD;
import static neige_i.moodtracker.controller.MainActivity.MOOD_COUNT;
import static neige_i.moodtracker.controller.MainActivity.NO_MOOD;

/**
 * Created by neige-i on 25/10/2017.
 */

public class Mood {

    private int mSmiley;
    private String mCommentary;

    public Mood() {
        mSmiley = NO_MOOD;
        mCommentary = null;
    }

    public Mood(int smiley, String commentary) {
        try { setSmiley(smiley); } catch (IndexOutOfBoundsException ignored) {}
        mCommentary = commentary;
    }

    public int getSmiley() {
        return mSmiley;
    }

    public void setSmiley(int smiley) {
        mSmiley = DEFAULT_MOOD;
        if ((smiley < 0 || smiley >= MOOD_COUNT) && smiley != NO_MOOD)
            throw new IndexOutOfBoundsException("The value must be in the following range [0;" + MOOD_COUNT + "[.");
        mSmiley = smiley;
    }

    public String getCommentary() {
        return mCommentary;
    }

    public void setCommentary(String commentary) {
        mCommentary = commentary;
    }

    @Override
    public String toString() {
        return mSmiley + (mCommentary != null ? mCommentary : "");
    }
}
