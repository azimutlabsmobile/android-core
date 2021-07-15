package com.kostyanchikoff.movenpublich

import android.content.pm.ActivityInfo
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
            allActivitiesOrientation {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }
}