package com.tt.githubbrowser.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tt.githubbrowser.repository.RepoRepository
import com.tt.githubbrowser.repository.UserRepository

class RepoListViewModel(
    savedStateHandle: SavedStateHandle,
    userRepository: UserRepository,
    reposRepository: RepoRepository
) : ViewModel() {
    val repos = reposRepository.getRepos()
}
