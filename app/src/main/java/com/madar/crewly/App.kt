package com.madar.crewly

import android.app.Application
import com.madar.crewly.di.commonModule
import com.madar.crewly.di.dataModule
import com.madar.crewly.di.displayModule
import com.madar.crewly.di.domainModule
import com.madar.crewly.di.inputModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger(Level.ERROR)
            modules(
                commonModule,
                dataModule,
                domainModule,
                inputModule,
                displayModule
            )
        }
    }
}
