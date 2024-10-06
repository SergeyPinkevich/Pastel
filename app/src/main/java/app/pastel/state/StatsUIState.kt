package app.pastel.state

data class StatsUIState(
    val highestScore: Int = 0,
    val gamesPlayed: Int = 0,
    val previousGames: List<PreviousGame> = emptyList(),
)

data class PreviousGame(
    val date: String,
    val score: Int
)