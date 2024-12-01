package com.truongdc.movie_tmdb.ui

import androidx.lifecycle.viewModelScope
import com.truongdc.movie_tmdb.core.data.repository.MainRepository
import com.truongdc.movie_tmdb.core.navigation.AppDestination
import com.truongdc.movie_tmdb.core.state.UiStateDelegateImpl
import com.truongdc.movie_tmdb.core.viewmodel.UiStateViewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieTMDBAppViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) :
    UiStateViewModel<MovieTMDBAppViewModel.UiState, MovieTMDBAppViewModel.Event>(
        UiStateDelegateImpl(UiState())
    ) {

    data class UiState(
        val isShowSettingDialog: Boolean = false,
        val isLogin: Boolean? = null,
    )

    sealed interface Event

    init {
        viewModelScope.launch {
            val isLogin = mainRepository.isLogin.first()
            updateUiState { state -> state.copy(isLogin = isLogin) }
        }
    }

    fun showSettingDialog(isShow: Boolean) {
        asyncUpdateUiState(viewModelScope) { state -> state.copy(isShowSettingDialog = isShow) }
    }

    fun logoutAccount() {
        viewModelScope.launch {
            try {
                showLoading()
                setLogout()
                appNavigator.showToast("Logout success!")
                popToLogin()
                hideLoading()
            } catch (e: Exception) {
                e.printStackTrace()
                appNavigator.showToast("Logout failed!")
                hideLoading()
            }
        }
    }

    private suspend fun setLogout() = mainRepository.setIsLogin(false)

    private fun popToLogin() {
        appNavigator.navigateTo(
            route = AppDestination.Login.route,
            popUpToRoute = appNavigator.currentRoute(),
            isInclusive = true
        )
    }
}