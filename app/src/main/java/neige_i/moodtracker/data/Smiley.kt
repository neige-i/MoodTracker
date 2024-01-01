package neige_i.moodtracker.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import neige_i.moodtracker.R

enum class Smiley(
    @DrawableRes val emoji: Int,
    @ColorRes val color: Int,
    val emoticon: String,
) {
    SAD(
        emoji = R.drawable.emoji_sad,
        color = R.color.faded_red,
        emoticon = ": (",
    ),
    DISAPPOINTED(
        emoji = R.drawable.emoji_disppaointed,
        color = R.color.warm_grey,
        emoticon = ": /",
    ),
    NORMAL(
        emoji = R.drawable.emoji_normal,
        color = R.color.cornflower_blue_65,
        emoticon = ": |",
    ),
    HAPPY(
        emoji = R.drawable.emoji_happy,
        color = R.color.light_sage,
        emoticon = ": )",
    ),
    SUPER_HAPPY(
        emoji = R.drawable.emoji_super_happy,
        color = R.color.banana_yellow,
        emoticon = ": D",
    ),
}