package com.kostynchikoff.core_application.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.kostynchikoff.KDispatcher.IKDispatcher
import com.kostynchikoff.KDispatcher.KDispatcherEventConstant
import com.kostynchikoff.KDispatcher.subscribe
import com.kostynchikoff.KDispatcher.unsubscribe
import com.kostynchikoff.core_application.R
import com.kostynchikoff.core_application.data.constants.CoreConstant
import com.kostynchikoff.core_application.data.constants.CoreVariables
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.presentation.model.UIValidation
import com.kostynchikoff.core_application.presentation.viewModel.CoreAuthViewModel
import com.kostynchikoff.core_application.utils.callback.PermissionHandler
import com.kostynchikoff.core_application.utils.callback.ResultLiveDataHandler
import com.kostynchikoff.core_application.utils.extensions.showActivityAndClearBackStack
import com.kostynchikoff.core_application.utils.wrappers.EventObserver
import org.koin.android.viewmodel.ext.android.viewModel
import java.net.HttpURLConnection

open class CoreBottomSheetFragment(
    private val lay: Int = -1,
    style: Int = R.style.BottomSheetDialogTheme,
    private val isAuthCallBack: Boolean = false
) : RoundedBottomSheetDialogFragment(style), ResultLiveDataHandler,
    PermissionHandler, IKDispatcher {

    private val viewModel by viewModel<CoreAuthViewModel>()

    /**
     * Для того чтобы отслеживать статусы необходимо подписаться во Fragment-е
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

    protected val errorMessageObserver = EventObserver<String> {
        error(it)
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
     * Подписка на ошибки
     * Возврашает строку и тип ошибки, удобно когда нужно вывести ошибку для конкретного случая
     */
    protected val errorMessageByTypeObserver = EventObserver<UIValidation> {
        errorByType(type = it.type, msg = it.message)
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

    open fun redirectLogin() =
        activity?.showActivityAndClearBackStack(CoreVariables.LOGIN_ACTIVITY)


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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (lay == -1) {
            return super.onCreateView(inflater, container, savedInstanceState)
        }
        return inflater.inflate(lay, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeAuthCallBack(view, savedInstanceState)

        if (viewModel.isPendingAuthorizationPassed) {
            onAuthorizedViewCreated(view, savedInstanceState)
        }else{
            onUnAuthorizedViewCreated(view, savedInstanceState)
        }
    }

    /**
     * Вызываеться после успешной авторизации
     */
    open fun onUpdateStateSuccessAuth() {
        // do nothing
    }

    /**
     * выполняться код для авторизованных пользователей
     */
    open fun onAuthorizedViewCreated(view: View, savedInstanceState: Bundle?) {
        // do nothing
    }

    /**
     * выполняться код для авторизованных пользователей
     */
    open fun onUnAuthorizedViewCreated(view: View, savedInstanceState: Bundle?) {
      // do nothing
    }

    /**
     * При успешной авторизации пересоздаем состоние
     */
    private fun subscribeAuthCallBack(view: View, savedInstanceState: Bundle?) {
        if (isAuthCallBack) {
            subscribe<Unit>(KDispatcherEventConstant.SUCCESS_AUTH_CORE_EVENT, 1) {
                onAuthorizedViewCreated(view, savedInstanceState)
                onUpdateStateSuccessAuth()
            }
        }
    }

    /**
     * Удаляем callback успешной авторизации
     */
    private fun unsubscribeAuthCallBack() {
        if (isAuthCallBack) {
            unsubscribe(KDispatcherEventConstant.SUCCESS_AUTH_CORE_EVENT)
        }
    }
}