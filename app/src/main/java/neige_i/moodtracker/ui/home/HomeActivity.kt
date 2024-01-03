package neige_i.moodtracker.ui.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import dagger.hilt.android.AndroidEntryPoint
import neige_i.moodtracker.R
import neige_i.moodtracker.databinding.ActivityHomeBinding
import neige_i.moodtracker.model.PrefUpdateReceiver
import neige_i.moodtracker.ui.comment.MoodCommentDialogFragment
import neige_i.moodtracker.ui.emoji.EmojiPagerAdapter
import neige_i.moodtracker.ui.history.HistoryActivity
import neige_i.moodtracker.ui.utils.viewBinding
import java.util.Calendar

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private val binding by viewBinding(ActivityHomeBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initMoodPager()
        handleClickEvents()
        schedulePrefUpdate()

        viewModel.pagerUiLiveData.observe(this, ::updateMoodPager)
        viewModel.moodToShareLiveData.observe(this, ::shareCurrentMood)
    }

    private fun initMoodPager() {
        binding.homeEmojiPager.apply {
            adapter = EmojiPagerAdapter(this@HomeActivity)
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.onPagerPositionChanged(position)
                }
            })
        }
    }

    private fun handleClickEvents() {
        binding.homeNewNoteIc.setOnClickListener {
            MoodCommentDialogFragment().show(supportFragmentManager, null)
        }
        binding.homeShareIc.setOnClickListener { viewModel.onMoodShareRequested() }
        binding.homeHistoryIc.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun updateMoodPager(moodPagerUi: MoodPagerUi) {
        binding.homeEmojiPager.apply {
            currentItem = moodPagerUi.position
            setBackgroundResource(moodPagerUi.backgroundColor)
        }
    }

    private fun shareCurrentMood(moodToShareEvent: MoodToShareEvent) {
        val textToSend = """
            ${moodToShareEvent.emoticon}
            ${moodToShareEvent.comment}
            ----------
            ${getString(R.string.share_text)}
            """.trimIndent()

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, textToSend)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
    }

    private fun schedulePrefUpdate() {
        val mPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE)

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
                PendingIntent.FLAG_IMMUTABLE
            )

            // Set the alarm to perform the tasks at midnight and repeat it every day
            // To remove the warning at the next instruction
            val alarm = (getSystemService(ALARM_SERVICE) as AlarmManager)
            alarm.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                broadcast
            )
        }
    }

    companion object {
        const val PREF_KEY_MOOD = "PREF_KEY_MOOD_"
        const val PREF_FILE_NAME = "mood"
    }
}
