package app.pastel.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val SOUND_ENABLED = "sound_enabled"

class PreferenceStorageImpl @Inject constructor(
    @ApplicationContext val context: Context
) : PreferenceStorage {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override val soundEnabled: Flow<Boolean>
        get() = context.dataStore.getValue(booleanPreferencesKey(SOUND_ENABLED), false)

    override suspend fun setSoundEnabled(value: Boolean) {
        setValue(booleanPreferencesKey(SOUND_ENABLED), value)
    }

    private suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    private fun <T> DataStore<Preferences>.getValue(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return this.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }
}