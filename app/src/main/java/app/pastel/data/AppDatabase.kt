package app.pastel.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameRecordSchema::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameRecordDao(): GameRecordDao
}