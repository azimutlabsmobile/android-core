package com.kostynchikoff.core_application.utils.extensions

import android.app.Application
import com.kostynchikoff.core_application.CoreBuilder
import com.kostynchikoff.lingver.Lingver
import java.util.*

/**
 * Иницальзация core в Application классе
 */
fun Application.coreBuilder(block: CoreBuilder.() -> Unit) = CoreBuilder(this).apply(block).build()

/**
 * Дает возможность использовать язык во всем приложении
 */
fun Application.initLaunguage() {
    Lingver.init(this, Locale.getDefault())
}