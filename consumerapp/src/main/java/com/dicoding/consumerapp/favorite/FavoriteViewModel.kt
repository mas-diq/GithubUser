package com.dicoding.consumerapp.favorite

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.consumerapp.DatabaseContract
import com.dicoding.consumerapp.MappingHelper
import com.dicoding.consumerapp.User

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var list = MutableLiveData<ArrayList<User>>()

    fun setFavoriteUser(context: Context) {
        val cursor = context.contentResolver.query(
            DatabaseContract.FavoriteUserColumns.CONTEN_URI,
            null, null, null, null
        )
        val listConverted = MappingHelper.mapCursorToArrayList(cursor)
        list.postValue(listConverted)
    }

    fun getFav(): LiveData<ArrayList<User>> = list
}
