package com.kostyanchikoff.movenpublich

import androidx.multidex.MultiDexApplication
import com.kostynchikoff.core_application.di.getCoreDIModule
import com.kostynchikoff.core_application.utils.extensions.coreBuilder

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        coreBuilder {
            koinModule {
                getCoreDIModule()
            }
        }
    }
}