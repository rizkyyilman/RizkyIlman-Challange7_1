package com.catnip.rizkyilmann_challange4

import android.app.Application
import com.catnip.rizkyilmann_challange4.dependencyinjection.AppInjection
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@App)
            modules(AppInjection.modules)
        }
    }
}
