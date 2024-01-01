package neige_i.moodtracker.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import neige_i.moodtracker.R

enum class Smiley(@DrawableRes val emoji: Int, @ColorRes val color: Int) {
    SAD(emoji = R.drawable.emoji_sad, color = R.color.faded_red),
    DISAPPOINTED(emoji = R.drawable.emoji_disppaointed, color = R.color.warm_grey),
    NORMAL(emoji = R.drawable.emoji_normal, color = R.color.cornflower_blue_65),
    HAPPY(emoji = R.drawable.emoji_happy, color = R.color.light_sage),
    SUPER_HAPPY(emoji = R.drawable.emoji_super_happy, color = R.color.banana_yellow),
}