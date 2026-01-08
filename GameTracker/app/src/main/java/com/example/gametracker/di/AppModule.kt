package com.example.gametracker.di

import com.example.gametracker.data.repository.FakeGameRepository
import com.example.gametracker.ui.LibraryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FakeGameRepository() }
    viewModel { LibraryViewModel(get()) }
}