package com.example.mapassesmenttask.di

import com.example.mapassesmenttask.core.data.repository.HomeRepository
import com.example.mapassesmenttask.ui.viewmodel.HomeViewModel
import com.example.mapassesmenttask.core.data.network.NetworkHelper
import com.example.mapassesmenttask.core.data.network.RestHelper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { HomeRepository(get()) }  // Ensure dependencies are correctly injected
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
}

val commonModule = module {
    single { NetworkHelper(androidContext()) }
    single { RestHelper.client }
}

val appModules = listOf(repositoryModule, viewModelModule, commonModule)