package neige_i.moodtracker.model;

import neige_i.moodtracker.R;

/**
 * Created by neige-i on 25/10/2017.
 */

public class Mood {

    private int mSmiley;
    private String mCommentary;

    public Mood(int smiley, String commentary) {
        mSmiley = smiley;
        mCommentary = commentary;
    }

    public int getSmiley() {
        return mSmiley;
    }

    public void setSmiley(int smiley) {
        if (smiley < 0 || smiley > 4)
            throw new IndexOutOfBoundsException("The value must be in the following range [0;5[.");
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
        return mSmiley + mCommentary;
    }
}
