package app.pastel.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : java.io.Serializable {

    @Serializable
    data object MenuScreen : Screen()

    @Serializable
    data object GameScreen : Screen()

    @Serializable
    data object StatsScreen : Screen()

    @Serializable
    data object AboutScreen : Screen()
}