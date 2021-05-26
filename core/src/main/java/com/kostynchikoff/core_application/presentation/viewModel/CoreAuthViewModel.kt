package com.kostynchikoff.core_application.presentation.viewModel

import com.kostynchikoff.core_application.domein.auth.useCase.CoreCheckAuthUserUseCase

/**
 * Всею общую информация для авторизации нужно переность сюда
 */
open class CoreAuthViewModel(
    private val isAuthUserUseCase: CoreCheckAuthUserUseCase
) : CoreLaunchViewModel() {

    val isAuthUser: Boolean
        get() = isAuthUserUseCase.execute().isAuthUser
}