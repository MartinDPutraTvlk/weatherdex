package com.julo.weatherdex.data.favorites.di.module

import android.content.SharedPreferences
import com.google.gson.Gson
import com.julo.weatherdex.core.di.IoDispatcher
import com.julo.weatherdex.data.favorites.api.repository.FavoritesRepository
import com.julo.weatherdex.data.favorites.implementation.repository.FavoritesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoritesModule {

    @Provides
    @Singleton
    fun provideConsignmentCandidateRepository(
        sharedPreferences: SharedPreferences,
        gson: Gson,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): FavoritesRepository {
        return FavoritesRepositoryImpl(
            sharedPreferences = sharedPreferences,
            gson = gson,
            ioDispatcher = ioDispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}
