package com.tonisives.githubbrowser.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tonisives.githubbrowser.repository.RepoRepository
import com.tonisives.githubbrowser.repository.UserRepository

class RepoListViewModel(
    userRepository: UserRepository,
    reposRepository: RepoRepository
) : ViewModel() {
    val repos = reposRepository.getRepos()
}
