package neige_i.moodtracker.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import neige_i.moodtracker.R
import neige_i.moodtracker.model.Mood
import neige_i.moodtracker.ui.Smiley
import neige_i.moodtracker.ui.main.MainActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var mMoodHistory: List<Mood>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        initHistoryFromPrefs()
        initHistoryLayout()
    }

    private fun initHistoryFromPrefs() {
        val preferences = getSharedPreferences(MainActivity.PREF_FILE_NAME, MODE_PRIVATE)
        mMoodHistory = buildList {
            var i = 1 // Caution: the PREF_KEY_MOOD_0 corresponds to the current day mood. This variable must begin at 1
            var moodString: String?
            while (preferences.getString(MainActivity.PREF_KEY_MOOD + i++, null).also { moodString = it } != null) {
                if (i > DAY_COUNT + 1) throw IndexOutOfBoundsException("There are too many moods that are stored in preferences")
                add(Mood.fromString(moodString))
            }
        }
    }

    private fun initHistoryLayout() {
        val historyLayout = findViewById<LinearLayout>(R.id.history_layout)
        if (mMoodHistory.isNotEmpty()) {
            // The history can contain a maximum of DAY_COUNT moods
            // So, setting the historyLayout's weights sum to DAY_COUNT and its children to 1
            // will assure that each mood of the history has the appropriate height
            historyLayout.weightSum = DAY_COUNT.toFloat()
            for (i in mMoodHistory.indices.reversed()) historyLayout.addView(getSingleMoodLayout(historyLayout, mMoodHistory[i], i))
        } else {
            // If the history is empty, display the TextView message
            historyLayout.visibility = View.GONE
            findViewById<View>(R.id.empty_history_txt).visibility = View.VISIBLE
        }
    }

    private fun getSingleMoodLayout(parent: ViewGroup, mood: Mood, whichDay: Int): View {
        // Set the main layout
        val mainLayout = LayoutInflater.from(this).inflate(R.layout.single_mood_history, parent, false) as LinearLayout

        // mainLayout has only one direct child, which width is proportional to the smiley value
        // So, setting the mainLayout's weights sum to MOOD_COUNT and its child to (smiley value + 1)
        // will ensure that each mood of the history has the appropriate width
        // (REMEMBER the smiley value is between 0 inclusive and MOOD_COUNT exclusive, hence the '+1')
        mainLayout.weightSum = Mood.MOOD_COUNT.toFloat()

        // Set the mood layout and the "no mood" TextView
        val moodLayout = mainLayout.findViewById<View>(R.id.single_mood_lyt)
        val weight: Int
        if (mood.smiley != Mood.MOOD_EMPTY) {
            // If the mood is not empty, set the correct background and a proportional width
            moodLayout.setBackgroundResource(Smiley.values()[mood.smiley].color)
            weight = mood.smiley + 1
        } else {
            // If the mood is empty, make the "no mood" TextView visible and set a maximal width
            mainLayout.findViewById<View>(R.id.no_mood_txt).visibility = View.VISIBLE
            weight = Mood.MOOD_COUNT
        }
        moodLayout.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, weight.toFloat())

        // Set the TextView showing the day
        (mainLayout.findViewById<View>(R.id.day_txt) as TextView).text = resources.getStringArray(R.array.mood_text_array)[whichDay]

        // Set the ImageView
        val commentaryIcon = mainLayout.findViewById<ImageView>(R.id.commentary_ic)
        if (mood.commentary.isNotEmpty()) { // If there is a commentary to display
            commentaryIcon.visibility = View.VISIBLE
            commentaryIcon.setOnClickListener {
                val textView = layoutInflater.inflate(R.layout.toast_message, parent, false) as TextView
                textView.text = mood.commentary
                val toast = Toast(this@HistoryActivity)
                toast.view = textView
                toast.show()
            }
        }
        return mainLayout
    }

    companion object {
        const val DAY_COUNT = 7
    }
}