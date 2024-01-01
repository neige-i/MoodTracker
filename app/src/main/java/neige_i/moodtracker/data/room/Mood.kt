package neige_i.moodtracker.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import neige_i.moodtracker.data.Smiley
import java.time.LocalDate

@Entity
data class Mood(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val smiley: Smiley,
    val comment: String?,
    val date: LocalDate,
)
