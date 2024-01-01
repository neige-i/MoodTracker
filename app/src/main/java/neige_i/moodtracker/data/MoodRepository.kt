package neige_i.moodtracker.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import neige_i.moodtracker.data.room.Mood
import neige_i.moodtracker.data.room.MoodDao
import java.time.LocalDate
import javax.inject.Inject

class MoodRepository @Inject constructor(
    private val moodDao: MoodDao,
) {

    fun getMoodByDate(date: LocalDate): Flow<Mood?> = moodDao.getByDate(date).distinctUntilChanged()

    /**
     * Tries to update the specified [mood] if it already exists in the database.
     * Otherwise, it is inserted as a new one.
     */
    suspend fun updateOrInsert(mood: Mood) {
        val updateCount = moodDao.update(mood)
        if (updateCount == 0) {
            moodDao.add(mood)
        }
    }
}