package ru.plumsoftware.photoeditor.utilities

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.plumsoftware.photoeditor.dependencyinjection.repositoryModule
import ru.plumsoftware.photoeditor.dependencyinjection.viewModelModule

@Suppress("unused")
class AppConfig : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}