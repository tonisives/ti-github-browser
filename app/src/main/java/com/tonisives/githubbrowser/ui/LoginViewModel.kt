package com.tonisives.githubbrowser.ui

import androidx.lifecycle.ViewModel
import com.tonisives.githubbrowser.repository.AuthRepository

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    // login activity can observe if there is a logged in user.
    // if there is none, this user will be set after login call.
    val user = authRepository.getLoggedInUser()

    fun login(userName: String, password: String) {
        authRepository.login(userName, password)
    }
}
