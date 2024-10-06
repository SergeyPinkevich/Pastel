package app.pastel.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : java.io.Serializable {

    @Serializable
    data object MenuScreen : Screen()

    @Serializable
    data object GameScreen : Screen()
}