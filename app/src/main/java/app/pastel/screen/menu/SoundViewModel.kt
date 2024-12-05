package app.pastel.screen.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.pastel.data.PreferenceStorage
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
class SoundViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : ViewModel() {

    private val _soundState = MutableStateFlow(SoundSettings.OFF)
    val soundState: StateFlow<SoundSettings> = _soundState

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferenceStorage.soundEnabled.collectLatest { isEnabled ->
                    _soundState.value = if (isEnabled) SoundSettings.ON else SoundSettings.OFF
                }
            }
        }
    }

    fun onSoundClick() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentSoundSettings = _soundState.value
                if (currentSoundSettings == SoundSettings.ON) {
                    preferenceStorage.setSoundEnabled(false)
                } else {
                    preferenceStorage.setSoundEnabled(true)
                }
            }
        }
    }
}