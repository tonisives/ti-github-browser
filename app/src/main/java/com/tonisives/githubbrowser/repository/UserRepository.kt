package com.tonisives.githubbrowser.repository

import com.tonisives.githubbrowser.dao.SharedPreferencesDao
import com.tonisives.githubbrowser.dao.UserDao
import com.tonisives.githubbrowser.util.AppExecutors
import org.koin.core.component.KoinComponent

class UserRepository(
    private val executor: AppExecutors,
    private val userDao: UserDao,
    private val sharedPreferencesDao: SharedPreferencesDao
) : KoinComponent {

    fun getUser() = userDao.getFirst()

    // this could be later used to refresh user data from backend
    /*fun getUser(email: String): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>(executor) {
            override fun saveCallResult(item: User) = userDao.save(item)

            override fun shouldFetch(data: User?) = false // never fetch, there is no user refresh call

            override fun loadFromDb() = userDao.getByEmail(email)

            override fun createCall() = MutableLiveData<ApiResponse>() // there is no user refresh call.

            override fun processResponse(json: String): User = json.toObject()

        }.asLiveData()
    }*/
}