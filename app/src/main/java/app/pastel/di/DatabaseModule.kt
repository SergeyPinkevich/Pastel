package app.pastel.di

import android.content.Context
import androidx.room.Room
import app.pastel.data.AppDatabase
import app.pastel.data.GameRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideGameRecordDao(database: AppDatabase): GameRecordDao {
        return database.gameRecordDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
            .build()
    }
}