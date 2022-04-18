package com.example.mycarinventory

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.core.context.GlobalContext

class MyCarInventoryApplication : Application() {
    override fun onCreate(){
        super.onCreate()

        GlobalContext.startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@MyCarInventoryApplication)
            modules(appModule)
        }
    }
}