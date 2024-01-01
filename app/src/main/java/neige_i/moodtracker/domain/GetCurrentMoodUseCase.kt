package neige_i.moodtracker.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import neige_i.moodtracker.data.MoodRepository
import neige_i.moodtracker.data.Smiley
import neige_i.moodtracker.data.room.Mood
import java.time.LocalDate
import javax.inject.Inject

class GetCurrentMoodUseCase @Inject constructor(
    private val moodRepository: MoodRepository,
) {

    operator fun invoke(): Flow<Mood> {
        val today = LocalDate.now()
        return moodRepository.getMoodByDate(today)
            .map { moodOfTheDay ->
                moodOfTheDay ?: Mood(smiley = Smiley.HAPPY, comment = null, date = today)
            }
    }
}