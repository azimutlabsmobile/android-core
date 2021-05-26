package com.kostynchikoff.core_application.utils.extensions

fun CharSequence?.empty() : String = this?.toString().orEmpty()