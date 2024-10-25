package app.pastel.data

import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {

    val soundEnabled: Flow<Boolean>

    suspend fun setSoundEnabled(value: Boolean)
}