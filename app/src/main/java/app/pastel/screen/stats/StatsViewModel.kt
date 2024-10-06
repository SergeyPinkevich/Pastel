package app.pastel.screen.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.pastel.data.GameRecordDao
import app.pastel.state.PreviousGame
import app.pastel.state.StatsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(private val local: GameRecordDao) : ViewModel() {

    private val _state = MutableStateFlow(StatsUIState())
    val state: StateFlow<StatsUIState> = _state

    init {
        viewModelScope.launch {
            val state = withContext(Dispatchers.IO) {
                val previousGames = local.getAll()
                val highestScore = if (previousGames.isEmpty()) 0 else previousGames.maxOf { it.score }
                StatsUIState(
                    highestScore = highestScore,
                    gamesPlayed = previousGames.size,
                    previousGames = previousGames.map {
                        PreviousGame(
                            date = convertTimestampToDate(it.timestamp),
                            score = it.score
                        )
                    }
                )
            }
            withContext(Dispatchers.Main) {
                _state.value = state
            }
        }
    }

    private fun convertTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(date)
    }
}