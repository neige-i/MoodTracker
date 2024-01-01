package neige_i.moodtracker.domain

import kotlinx.coroutines.flow.first
import neige_i.moodtracker.data.Smiley
import javax.inject.Inject

class SetCurrentMoodUseCase @Inject constructor(
    private val getCurrentMoodUseCase: GetCurrentMoodUseCase,
    private val upsertMoodUseCase: UpsertMoodUseCase,
) {

    suspend fun updateSmileyAndResetComment(newSmileyIndex: Int) {
        if (newSmileyIndex in Smiley.entries.indices) {
            val newMood = getCurrentMoodUseCase.invoke()
                .first()
                .copy(
                    smiley = Smiley.entries[newSmileyIndex],
                    comment = null,
                )
            upsertMoodUseCase.invoke(newMood)
        }
    }
}