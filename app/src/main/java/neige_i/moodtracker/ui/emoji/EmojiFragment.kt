package neige_i.moodtracker.ui.emoji

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import neige_i.moodtracker.R
import neige_i.moodtracker.databinding.FragmentEmojiBinding
import neige_i.moodtracker.ui.utils.viewBinding

class EmojiFragment : Fragment(R.layout.fragment_emoji) {

    private val binding by viewBinding(FragmentEmojiBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.emojiImage.setImageResource(requireArguments().getInt(EMOJI_ID_ARG))
    }

    companion object {
        private const val EMOJI_ID_ARG = "EMOJI_ID_ARG"

        fun newInstance(@DrawableRes emojiResId: Int): EmojiFragment {
            return EmojiFragment().apply {
                arguments = bundleOf(
                    EMOJI_ID_ARG to emojiResId,
                )
            }
        }
    }
}
