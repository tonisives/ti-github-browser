package com.tonisives.githubbrowser.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tonisives.githubbrowser.dao.SharedPreferencesDao

import com.tonisives.githubbrowser.dao.UserDao
import com.tonisives.githubbrowser.model.User

import com.tonisives.githubbrowser.network.client.AuthClient
import com.tonisives.githubbrowser.network.route.UserCredentials
import com.tonisives.githubbrowser.util.AppExecutors

class AuthRepository constructor(
    private val authClient: AuthClient,
    private val userDao: UserDao,
    private val sharedPrefs: SharedPreferencesDao,
    private val executor: AppExecutors
) {
    var result = MediatorLiveData<Resource<User?>>()

    fun getLoggedInUser(): MediatorLiveData<Resource<User?>> {
        result.value = Resource.loading(null)

        executor.diskIO().execute {
            val dbSource = userDao.getFirst()
            executor.mainThread().execute {
                result.value = Resource.success(dbSource.value)
            }
        }

        return result
    }

    fun login(email: String, token: String): LiveData<Resource<User?>> {
        // call the login, store new user in db, set current user to sharedPrefs
        result.value = Resource.loading(null)

        val networkRequest = authClient.loginUser(email, token)

        result.addSource(networkRequest) { data ->
            result.removeSource(networkRequest)

            if (data.value != null) {
                data.value.token = token

                executor.diskIO().execute {
                    userDao.save(data.value) // now next requests can be made with this token
                    // the login request is not actually required to show the repo list, but we save it here because
                    // mostly API-s return access token after login.
                    sharedPrefs.setCurrentUser(UserCredentials(email, token))

                    executor.mainThread().execute {
                        result.addSource(userDao.getFirst()) {
                            result.value = Resource.success(it)
                        }
                    }
                }
            } else {
                result.value = Resource.error(data.errorMessage!!, null)
            }
        }

        return result
    }

    fun logout(): LiveData<Resource<Unit>> {
        val status = mutableLoadingLiveDataOf<Unit>()

        executor.diskIO().execute {
            userDao.deleteAll()
            status.postValue(Resource.success(null))
        }

        return status
    }
}