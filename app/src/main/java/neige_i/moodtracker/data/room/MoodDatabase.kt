package neige_i.moodtracker.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Mood::class], version = 1)
@TypeConverters(Converters::class)
abstract class MoodDatabase : RoomDatabase() {

    abstract fun moodDao(): MoodDao
}