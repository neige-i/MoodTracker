package neige_i.moodtracker.data.room

import androidx.room.TypeConverter
import java.time.DateTimeException
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDateToEpochDay(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @TypeConverter
    fun fromEpochDayToLocalDate(day: Long): LocalDate {
        return try {
            LocalDate.ofEpochDay(day)
        } catch (e: DateTimeException) {
            LocalDate.EPOCH
        }
    }
}