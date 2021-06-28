package com.dicoding.githubuser.data

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteUserDao {

    //insert user to fav
    @Insert
    suspend fun addUser(favoriteUser: FavoriteUser)

    //get user from list
    @Query("SELECT * FROM userTable")
    fun getUser(): LiveData<List<FavoriteUser>>

    //check user if already exist
    @Query("SELECT COUNT(*) FROM userTable WHERE userTable.id = :id ")
    suspend fun checkUser(id: Int): Int

    @Query("DELETE FROM userTable WHERE userTable.id =:id")
    suspend fun deleteUser(id: Int): Int

    // for content provider
    @Query("SELECT * FROM userTable")
    fun findAll(): Cursor
}
