package tech.danielwaiguru.dscmuranga

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import tech.danielwaiguru.dscmuranga.di.appModules

class DscApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
    private fun initKoin() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@DscApp)
            modules(appModules)
        }
    }
}