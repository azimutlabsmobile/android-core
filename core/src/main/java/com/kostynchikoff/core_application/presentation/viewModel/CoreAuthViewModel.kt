package com.kostynchikoff.core_application.presentation.viewModel

import android.renderscript.RenderScript
import com.kostynchikoff.core_application.domein.auth.useCase.CoreCheckAuthUserUseCase
import com.kostynchikoff.core_application.domein.auth.useCase.CoreIsPendingAuthorizationPassedUseCase

/**
 * Всею общую информация для авторизации нужно переность сюда
 */
open class CoreAuthViewModel(
    private val isAuthUserUseCase: CoreCheckAuthUserUseCase,
    private val isPendingAuthorizationPassedUseCase: CoreIsPendingAuthorizationPassedUseCase
) : CoreLaunchViewModel() {

    val isPendingAuthorizationPassed: Boolean
        get() = isAuthUserUseCase.execute().isAuthUser && isPendingAuthorizationPassedUseCase.execute()

}