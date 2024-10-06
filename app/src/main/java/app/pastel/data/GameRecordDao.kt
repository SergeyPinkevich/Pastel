package app.pastel.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameRecordDao {

    @Query("SELECT * FROM game_records ORDER BY timestamp DESC")
    suspend fun getAll(): List<GameRecordSchema>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameRecord: GameRecordSchema)
}