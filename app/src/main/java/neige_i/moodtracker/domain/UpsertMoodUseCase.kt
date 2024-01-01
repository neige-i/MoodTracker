package neige_i.moodtracker.domain

import neige_i.moodtracker.data.MoodRepository
import neige_i.moodtracker.data.room.Mood
import javax.inject.Inject

class UpsertMoodUseCase @Inject constructor(
    private val moodRepository: MoodRepository,
) {

    suspend operator fun invoke(mood: Mood) {
        moodRepository.updateOrInsert(mood)
    }
}