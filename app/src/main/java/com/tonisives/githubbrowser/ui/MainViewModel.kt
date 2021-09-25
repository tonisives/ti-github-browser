package com.tonisives.githubbrowser.ui;

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tonisives.githubbrowser.repository.AuthRepository
import com.tonisives.githubbrowser.repository.UserRepository

class MainViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val user = userRepository.getUser()
    fun logout() = authRepository.logout()
}
