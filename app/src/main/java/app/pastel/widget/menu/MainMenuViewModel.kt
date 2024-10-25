package app.pastel.widget.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.pastel.data.PreferenceStorage
import app.pastel.state.MainMenuUIState
import app.pastel.state.SoundSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainMenuUIState())
    val uiState: StateFlow<MainMenuUIState> = _uiState

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferenceStorage.soundEnabled.collectLatest { isEnabled ->
                    val soundSettings = if (isEnabled) SoundSettings.ON else SoundSettings.OFF
                    _uiState.value = _uiState.value.copy(soundSettings = soundSettings)
                }
            }
        }
    }

    fun onSoundClick() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentSoundSettings = _uiState.value.soundSettings
                if (currentSoundSettings == SoundSettings.ON) {
                    preferenceStorage.setSoundEnabled(false)
                } else {
                    preferenceStorage.setSoundEnabled(true)
                }
            }
        }
    }
}