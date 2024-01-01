package neige_i.moodtracker.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MoodDao {

    @Query("SELECT * FROM mood WHERE date = :moodDate")
    fun getByDate(moodDate: LocalDate): Flow<Mood?>

    @Insert
    suspend fun add(mood: Mood)

    @Update
    suspend fun update(mood: Mood): Int
}