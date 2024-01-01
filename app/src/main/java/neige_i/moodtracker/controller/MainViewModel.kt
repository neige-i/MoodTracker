package neige_i.moodtracker.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import neige_i.moodtracker.data.room.Mood
import neige_i.moodtracker.domain.GetCurrentMoodUseCase
import neige_i.moodtracker.domain.SetCurrentMoodUseCase
import neige_i.moodtracker.ui.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getCurrentMoodUseCase: GetCurrentMoodUseCase,
    private val setCurrentMoodUseCase: SetCurrentMoodUseCase,
) : ViewModel() {

    private val currentMoodFlow: Flow<Mood> = getCurrentMoodUseCase.invoke()

    val pagerUiLiveData: LiveData<MoodPagerUi> = currentMoodFlow
        .map {
            MoodPagerUi(
                position = it.smiley.ordinal,
                backgroundColor = it.smiley.color
            )
        }
        .asLiveData()

    private val moodToShareSingleLiveEvent = SingleLiveEvent<MoodToShareEvent>()
    val moodToShareLiveData: LiveData<MoodToShareEvent> = moodToShareSingleLiveEvent

    fun onPagerPositionChanged(position: Int) {
        viewModelScope.launch {
            setCurrentMoodUseCase.updateSmileyAndResetComment(newSmileyIndex = position)
        }
    }

    fun onMoodShareRequested() {
        viewModelScope.launch {
            val currentMood= currentMoodFlow.first()

            withContext(Dispatchers.Main) {
                moodToShareSingleLiveEvent.value = MoodToShareEvent(
                    emoticon = currentMood.smiley.emoticon,
                    comment = currentMood.comment.orEmpty(),
                )
            }
        }
    }
}