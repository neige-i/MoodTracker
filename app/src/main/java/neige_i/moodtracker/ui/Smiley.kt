package neige_i.moodtracker.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import neige_i.moodtracker.R

enum class Smiley(@DrawableRes val emoji: Int, @ColorRes val color: Int) {
    SAD(R.drawable.smiley_sad, R.color.faded_red),
    DISAPPOINTED(R.drawable.smiley_disappointed, R.color.warm_grey),
    NORMAL(R.drawable.smiley_normal, R.color.cornflower_blue_65),
    HAPPY(R.drawable.smiley_happy, R.color.light_sage),
    GLAD(R.drawable.smiley_super_happy, R.color.banana_yellow),
}