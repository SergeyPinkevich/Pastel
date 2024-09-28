package app.pastel.state

data class MainMenuUIState(
    val soundSettings: SoundSettings = SoundSettings.ON
)

enum class SoundSettings {
    ON,
    OFF
}