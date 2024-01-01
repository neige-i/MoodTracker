package neige_i.moodtracker.controller

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
class MainViewModel @Inject constructor(
    getCurrentMoodUseCase: GetCurrentMoodUseCase,
    private val setCurrentMoodUseCase: SetCurrentMoodUseCase,
) : ViewModel() {

    val pagerUiLiveData: LiveData<MoodPagerUi> = getCurrentMoodUseCase.invoke()
        .map {
            MoodPagerUi(
                position = it.smiley.ordinal,
                backgroundColor = it.smiley.color
            )
        }
        .asLiveData()

    fun onPagerPositionChanged(position: Int) {
        viewModelScope.launch {
            setCurrentMoodUseCase.updateSmileyAndResetComment(newSmileyIndex = position)
        }
    }
}