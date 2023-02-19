package neige_i.moodtracker.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import neige_i.moodtracker.R
import neige_i.moodtracker.ui.history.HistoryActivity
import neige_i.moodtracker.model.Mood
import neige_i.moodtracker.service.PrefUpdateReceiver
import neige_i.moodtracker.ui.Smiley
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mMoodPager: ViewPager2
    private var mCommentaryInput: EditText? = null

    private lateinit var mPreferences: SharedPreferences
    private lateinit var mCurrentMood: Mood

    /**
     *
     * Control variable to determinate if the commentary is correct.<br></br>
     * As the current mood is not saved at each ViewPager swipe, the commentary must be checked at two moments:
     * hen displaying it in the EditText,
     * when saving it into preferences.
     *
     * The situation is as follows: the user enters a commentary for a specified mood, and then swipes to another one.
     *
     * First case: the user opens the Dialog.
     * It would be inappropriate if he can still see his commentary in the EditText.
     * Therefore the EditText must not contain the commentary.
     *
     * Second case: the user exits the app without putting a new commentary.
     * The current one is still in memory and must not be taken into consideration.
     * Therefore the value to put into preferences is an empty String instead of the current commentary.
     */
    private var isCommentaryCorrect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMoodFromPrefs()
        initMoodPager()

        findViewById<View>(R.id.new_note_ic).setOnClickListener(this)
        findViewById<View>(R.id.history_ic).setOnClickListener(this)
        findViewById<View>(R.id.share_ic).setOnClickListener(this)

        schedulePrefUpdate()
    }

    private fun initMoodFromPrefs() {
        mPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE)

        // Initialize the current mood with the preferences
        // If no preferences is found, then initialize with an empty mood
        mCurrentMood = Mood.fromString(mPreferences.getString(PREF_KEY_MOOD + 0, Mood().toString()))

        // If the current mood is empty, then set it to default
        if (mCurrentMood.smiley == Mood.MOOD_EMPTY) mCurrentMood.smiley = Mood.MOOD_DEFAULT
    }

    private fun initMoodPager() {
        isCommentaryCorrect = true
        mMoodPager = findViewById(R.id.mood_pager)
        mMoodPager.adapter = SmileyPagerAdapter(this)
        mMoodPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Update the background and the control variable at each page swipe
                mMoodPager.setBackgroundResource(Smiley.values()[position].color)
                isCommentaryCorrect = mMoodPager.currentItem == mCurrentMood.smiley
            }
        })
        mMoodPager.currentItem = mCurrentMood.smiley // Initialize the ViewPager's current item
    }

    private fun schedulePrefUpdate() {
        // The alarm is correctly triggered at midnight, every day
        // But it is also triggered at each app launch, which is not desired
        // To prevent this, the actions in this method are only performed at the first app launch
        if (mPreferences.getString(PREF_KEY_MOOD + 0, null) == null) {
            // The preferences must be updated at midnight
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0

            // Set the class that will handle the actions to perform
            val broadcast = PendingIntent.getBroadcast(
                this,
                0,
                Intent(this, PrefUpdateReceiver::class.java),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
            )

            // Set the alarm to perform the tasks at midnight and repeat it every day
            // To remove the warning at the next instruction
            val alarm = (getSystemService(ALARM_SERVICE) as AlarmManager)
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, broadcast)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.new_note_ic -> openCommentaryDialog()
            R.id.history_ic -> startActivity(Intent(this, HistoryActivity::class.java))
            R.id.share_ic -> shareCurrentMood()
        }
    }

    private fun openCommentaryDialog() {
        val commentaryDialog = AlertDialog.Builder(this)
            .setView(R.layout.dialog_commentary)
            .setPositiveButton(R.string.dialog_positive_btn) { _, _ ->
                mCurrentMood.commentary = mCommentaryInput!!.text.toString()
                mCurrentMood.smiley = mMoodPager.currentItem
                isCommentaryCorrect = true
            }
            .setNegativeButton(R.string.dialog_negative_btn, null)
            .create()
        assert(
            commentaryDialog.window != null // To remove the warning at the next instruction
        )
        commentaryDialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        commentaryDialog.show()

        // Initialize the EditText
        mCommentaryInput = commentaryDialog.findViewById(R.id.commentary_input)
        if (isCommentaryCorrect) {
            mCommentaryInput!!.setText(mCurrentMood.commentary)
            mCommentaryInput!!.setSelection(mCurrentMood.commentary.length)
        }
    }

    private fun shareCurrentMood() {
        saveMoodToPrefs()
        val smileyTab = arrayOf(": (", ": /", ": |", ": )", ": D")
        val textToSend = """
             ${smileyTab[mCurrentMood.smiley]}
             ${mCurrentMood.commentary}
             ------------------------------
             ${getString(R.string.share_text)}
             """.trimIndent()
        val shareIntent = Intent(Intent.ACTION_SEND) // Implicit intent
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToSend)
        shareIntent.type = "text/plain"
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
    }

    private fun saveMoodToPrefs() {
        mCurrentMood.smiley = mMoodPager.currentItem
        if (!isCommentaryCorrect) mCurrentMood.commentary = "" // Reset the commentary if incorrect
        mPreferences.edit().putString(PREF_KEY_MOOD + 0, mCurrentMood.toString()).apply()
    }

    override fun onStop() {
        super.onStop()
        saveMoodToPrefs()
    }

    companion object {
        const val PREF_KEY_MOOD = "PREF_KEY_MOOD_"
        const val PREF_FILE_NAME = "mood"
    }
}