package neige_i.moodtracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import neige_i.moodtracker.data.room.MoodDao
import neige_i.moodtracker.data.room.MoodDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MoodDatabase {
        return Room
            .databaseBuilder(
                context = context,
                klass = MoodDatabase::class.java,
                name = "mood",
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideMoodDao(moodDatabase: MoodDatabase): MoodDao {
        return moodDatabase.moodDao()
    }
}