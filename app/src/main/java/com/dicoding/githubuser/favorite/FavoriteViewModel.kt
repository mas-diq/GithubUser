package com.dicoding.githubuser.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.data.FavoriteUser
import com.dicoding.githubuser.data.FavoriteUserDao
import com.dicoding.githubuser.data.UserDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var favDao: FavoriteUserDao? = null
    private var db: UserDatabase? = null

    init {
        db = UserDatabase.getDatabase(application)
        favDao = db?.favoriteUserDao()
    }

    fun getFav(): LiveData<List<FavoriteUser>>? = favDao?.getUser()
}
