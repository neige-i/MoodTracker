package neige_i.moodtracker.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import neige_i.moodtracker.ui.main.smiley.SmileyFragment
import neige_i.moodtracker.ui.Smiley

class SmileyPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val allEmojis = Smiley.values().map { it.emoji }

    override fun createFragment(position: Int): Fragment = SmileyFragment.newInstance(allEmojis[position])

    override fun getItemCount(): Int = allEmojis.size
}