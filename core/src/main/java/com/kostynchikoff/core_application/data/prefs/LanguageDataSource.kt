package com.kostynchikoff.core_application.data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.kostynchikoff.core_application.data.constants.CoreConstant.PREF_CURRENT_LOCALE

@Deprecated("будет удален в 135 версии")
class LanguageDataSource(private val pref: SharedPreferences) {

    /**
     * Получить current lang
     */
    fun getCurrentLocale() = pref.getString(PREF_CURRENT_LOCALE, null)

    /**
     * Обновляем current locale
     */
    fun setCurrentLocale(lang: String) = pref.edit {
        putString(PREF_CURRENT_LOCALE, lang)
    }

}