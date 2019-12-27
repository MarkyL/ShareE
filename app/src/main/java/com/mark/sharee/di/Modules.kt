package com.mark.sharee.di

import android.content.Context
import android.content.SharedPreferences
import com.example.sharee.R
import com.mark.sharee.fragments.main.MainViewModel
import com.mark.sharee.fragments.movie.MovieViewModel
import com.mark.sharee.navigation.ActivityNavigator
import com.mark.sharee.navigation.FragmentNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module(override = true) {
    single<SharedPreferences> { androidContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE) }
}

val navigatorModule = module {
    single {  ActivityNavigator(R.id.fragmentContainer) }
    single {  FragmentNavigator(R.id.fragmentContainer) }
}

val viewModelsModule = module {
    viewModel { MovieViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
}

// Gather all app modules
val shareeApp = listOf(dataModule, navigatorModule, viewModelsModule, retrofitModule)
