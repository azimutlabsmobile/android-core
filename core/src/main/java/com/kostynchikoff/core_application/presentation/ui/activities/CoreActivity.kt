package com.kostynchikoff.core_application.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kostynchikoff.KDispatcher.IKDispatcher
import com.kostynchikoff.core_application.data.constants.CoreConstant
import com.kostynchikoff.core_application.data.constants.CoreVariables.ACTIVITIES_SCREEN_ORIENTATION
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.presentation.model.UIValidation
import com.kostynchikoff.core_application.utils.callback.PermissionHandler
import com.kostynchikoff.core_application.utils.callback.ResultLiveDataHandler
import com.kostynchikoff.core_application.utils.delegates.*
import com.kostynchikoff.core_application.utils.extensions.toast
import com.kostynchikoff.core_application.utils.wrappers.EventObserver
import java.net.HttpURLConnection


abstract class CoreActivity(lay: Int) : AppCompatActivity(lay), ResultLiveDataHandler,
    DarkTheme by DarkThemeDelegate(),
    TransitionAnimation by TransitionAnimationActivityDelegate(), PermissionHandler, IKDispatcher {


    protected val errorMessageObserver = EventObserver<String> { toast(it) }

    protected val errorMessageByTypeObserver = EventObserver<UIValidation> {
        errorByType(type = it.type, msg = it.message)
    }

    protected val successMessageObserver = EventObserver<String> {
        successMessage(it)
    }


    protected val loaderByTypeObserver = EventObserver<Pair<String, Boolean>> {
        val (type, isLoading) = it
        if (isLoading) {
            showLoaderByType(type)
        } else {
            hideLoaderByType(type)
        }
    }

    /**
     * Используем чтобы отследить конкретную ошибку
     */
    protected val errorMessageByCodeObserver = EventObserver<Pair<Int, String>> {
        val (code , error) = it
        when(code){
            HttpURLConnection.HTTP_INTERNAL_ERROR -> on500Error(error)
            HttpURLConnection.HTTP_NOT_FOUND ->  on404Error(error)
        }
    }



    open fun redirectLogin() {
        // реализовать в случае базовой функциональности
    }

    /**
     * Для того чтобы отслеживать статусы необходимо подписаться в Activity
     */
    protected val statusObserver = EventObserver<Status> {
        it.let {
            when (it) {
                Status.SHOW_LOADING -> showLoader()
                Status.HIDE_LOADING -> hideLoader()
                Status.REDIRECT_LOGIN -> redirectLogin()
                Status.SHOW_PULL_TO_REFRESH_LOADING -> showPullToRefreshLoader()
                Status.HIDE_PULL_TO_REFRESH_LOADING -> hidePullToRefreshLoader()
                Status.SHOW_PAGGING_LOADING -> showPagingLoader()
                Status.HIDE_PAGGING_LOADING -> hidePagingLoader()
                Status.SUCCESS -> success()
                else -> return@let
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme(window, Theme.DARK)
        initTransition(this)
        /*
        * Если задан то задаем ориентацию для всех активити
        */
        ACTIVITIES_SCREEN_ORIENTATION?.let {
            requestedOrientation = it
        }
        super.onCreate(savedInstanceState)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            when {
                it != CoreConstant.PERMISSION_DENIED -> {
                    confirmPermission()
                    confirmWithRequestCode(requestCode)
                    return
                }
                else -> {
                    ignorePermission()
                    return
                }
            }
        }
    }

}

