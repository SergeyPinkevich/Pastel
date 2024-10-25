package app.pastel.di

import app.pastel.data.PreferenceStorage
import app.pastel.data.PreferenceStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatastoreModule {

    @Binds
    @Singleton
    abstract fun provideDataStore(preferenceStorage: PreferenceStorageImpl): PreferenceStorage
}