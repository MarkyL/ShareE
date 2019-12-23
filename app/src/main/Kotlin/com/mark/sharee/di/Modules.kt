package com.mark.sharee.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module(override = true) {
    single<SharedPreferences> { androidContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE) }
}

//val activityModule = module()

// Gather all app modules
val shareeApp = listOf(dataModule)