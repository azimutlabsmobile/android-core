package com.kostynchikoff.core_application.presentation.ui.activities

import android.os.Bundle
import android.view.MotionEvent
import androidx.core.os.bundleOf
import com.kostynchikoff.core_application.data.constants.CoreConstant.ARG_IS_AUTH_FROM_INACTION
import com.kostynchikoff.core_application.data.constants.CoreVariables
import com.kostynchikoff.core_application.presentation.controllers.TrackUseApplication
import com.kostynchikoff.core_application.presentation.controllers.TrackUseApplicationController
import com.kostynchikoff.core_application.presentation.viewModel.CoreAuthViewModel
import com.kostynchikoff.core_application.utils.delegates.Theme
import com.kostynchikoff.core_application.utils.extensions.goPendingActivity
import com.kostynchikoff.core_application.utils.extensions.showActivityAndClearBackStack
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Использовать в авторизованой зоне
 * @param lay layout
 * @param isUseLocalSession если нужно использовать локальную сессию
 */
abstract class CoreAuthorizedActivity(
    lay: Int,
    isUseLocalSession: Boolean = true,
    private val isGoToPendingFragment: Boolean = true
) :
    CoreActivity(lay),
    TrackUseApplication by TrackUseApplicationController(isUseLocalSession) {

    private val viewModel by viewModel<CoreAuthViewModel>()

    override fun redirectLogin() = showActivityAndClearBackStack(CoreVariables.LOGIN_ACTIVITY)


    override fun onCreate(savedInstanceState: Bundle?) {
        isAuthUser = viewModel.isAuthUser
        onStartTrack(this)
        super.onCreate(savedInstanceState)
        if (viewModel.isAuthUser) {
            onAuthorizedCreate(savedInstanceState)
        } else {
            onUnAuthorizedCreate(savedInstanceState)
        }
    }

    /**
     * Выволнения кода для авторизованных пользователей
     */
    open fun onAuthorizedCreate(savedInstanceState: Bundle?) {
        // выполняться код для авторизованных пользователей
    }

    /**
     * Выволнения кода для не авторизованных пользователей
     */
    open fun onUnAuthorizedCreate(savedInstanceState: Bundle?) {
        // выполняться код для авторизованных пользователей
    }

    /**
     * Выволнения кода при срабатывании бездействия
     */
    open fun redirectLoginInCaseOfInaction() {
        showActivityAndClearBackStack(CoreVariables.LOGIN_ACTIVITY, bundleOf(ARG_IS_AUTH_FROM_INACTION to true))
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        onTouchEvent()
        return super.dispatchTouchEvent(ev)
    }


    override fun onResume() {
        super.onResume()
        onResumeTrack()
        if (isGoToPendingFragment)
            goPendingActivity()
    }

    override fun onPause() {
        super.onPause()
        onPauseTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyTrack()
    }
}