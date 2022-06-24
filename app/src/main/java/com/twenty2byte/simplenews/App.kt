package com.twenty2byte.simplenews

import android.app.Application
import com.twenty2byte.simplenews.di.networkModule
import com.twenty2byte.simplenews.di.userDb
import com.twenty2byte.simplenews.di.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(userDb, networkModule, viewModel)
        }
    }
}