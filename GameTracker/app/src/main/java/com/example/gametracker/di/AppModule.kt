package com.example.gametracker.di

import androidx.room.Room
import com.example.gametracker.data.api.RawgApi
import com.example.gametracker.data.local.GameDatabase
import com.example.gametracker.data.repository.ConcreteGameRepository
import com.example.gametracker.domain.repository.GameRepository
import com.example.gametracker.ui.LibraryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    // ROOM
    single {
        Room.databaseBuilder(
            androidContext(),
            GameDatabase::class.java,
            "game_tracker.db"
        ).build()
    }

    // DAO
    single { get<GameDatabase>().gameDao() }

    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // RawgAPI
    single { get<Retrofit>().create(RawgApi::class.java) }

    // Repository
    single<GameRepository> {
        ConcreteGameRepository(
            api = get(),
            dao = get(),
            apiKey = "b9e45507eea746a9a1f6291707df5c8c" // BuildConfig dps
        )
    }

    // LibraryViewModel
    viewModel {
        LibraryViewModel(repository = get())
    }
}