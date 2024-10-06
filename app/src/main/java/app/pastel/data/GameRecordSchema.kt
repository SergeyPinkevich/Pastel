package app.pastel.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_records")
data class GameRecordSchema(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val score: Int,
    val timestamp: Long
)