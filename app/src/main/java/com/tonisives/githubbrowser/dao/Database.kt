package com.tonisives.githubbrowser.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tonisives.githubbrowser.model.Converters
import com.tonisives.githubbrowser.model.Repo
import com.tonisives.githubbrowser.model.User

@Database(entities = [User::class, Repo::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repoDao(): RepoDao
}