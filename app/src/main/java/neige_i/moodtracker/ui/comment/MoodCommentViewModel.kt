package neige_i.moodtracker.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import neige_i.moodtracker.domain.GetCurrentMoodUseCase
import neige_i.moodtracker.domain.SetCurrentMoodUseCase
import javax.inject.Inject

@HiltViewModel
class MoodCommentViewModel @Inject constructor(
    getCurrentMoodUseCase: GetCurrentMoodUseCase,
    private val setCurrentMoodUseCase: SetCurrentMoodUseCase,
) : ViewModel() {

    val moodCommentLiveData: LiveData<String> = getCurrentMoodUseCase.invoke()
        .map { it.comment.orEmpty() }
        .asLiveData()

    fun onPositiveButtonClicked(comment: String?) {
        viewModelScope.launch {
            comment?.let {
                setCurrentMoodUseCase.updateComment(newComment = it)
            }
        }
    }
}