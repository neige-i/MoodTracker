package neige_i.moodtracker.ui.comment

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import neige_i.moodtracker.R
import neige_i.moodtracker.databinding.DialogCommentBinding
import neige_i.moodtracker.ui.utils.viewBinding

@AndroidEntryPoint
class MoodCommentDialogFragment : DialogFragment() {

    private val binding by viewBinding(DialogCommentBinding::inflate)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val viewModel: MoodCommentViewModel by viewModels()

        viewModel.moodCommentLiveData.observe(this) { moodComment ->
            binding.commentInput.setText(moodComment)
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title)
            .setView(binding.root)
            .setPositiveButton(R.string.dialog_positive_btn) { _, _ ->
                viewModel.onPositiveButtonClicked(binding.commentInput.text?.toString())
            }
            .setNegativeButton(R.string.dialog_negative_btn, null)
            .create()

        automaticallyShowSoftKeyboard(alertDialog.window)

        return alertDialog
    }

    private fun automaticallyShowSoftKeyboard(dialogWindow: Window?) {
        binding.commentInput.requestFocus()
        dialogWindow?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }
}