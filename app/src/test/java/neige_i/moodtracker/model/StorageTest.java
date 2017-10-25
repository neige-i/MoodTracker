package neige_i.moodtracker.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static neige_i.moodtracker.controller.MainActivity.DEFAULT_MOOD;
import static org.junit.Assert.assertEquals;

/**
 * Created by neige-i on 25/10/2017.
 */
public class StorageTest {
    @Test
    public void initMoodList() throws Exception {
        String[] strings = new String[] {   "2Mmm...",
                                            "4Great!",
                                            "3Yeah",
                                            "0WHAT!!!",
                                            "Better",               // Mood incorrect
                                            null,                   // No mood
                                            "1",
                                            "0Angry",
                                            "3Not recordable" };    // Mood out of range
        List<Mood> moodList = Arrays.asList(new Mood(2, "Mmm..."),
                                            new Mood(4, "Great!"),
                                            new Mood(3, "Yeah"),
                                            new Mood(0, "WHAT!!!"),
                                            new Mood(DEFAULT_MOOD, "etter"),
                                            new Mood(),
                                            new Mood(1, null),
                                            new Mood(0, "Angry"));
        Storage storage = new Storage();
        storage.initMoodList(strings);
        assertEquals(moodList.toString(), storage.getMoodList().toString());
    }

}