package neige_i.moodtracker.ui.emoji

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import neige_i.moodtracker.data.Smiley

class EmojiPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = Smiley.entries.size

    override fun createFragment(position: Int): Fragment {
        return EmojiFragment.newInstance(emojiResId = Smiley.entries[position].emoji)
    }
}