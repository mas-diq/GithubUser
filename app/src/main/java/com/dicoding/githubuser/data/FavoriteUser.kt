package com.dicoding.githubuser.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "userTable")
data class FavoriteUser(
    @ColumnInfo(name = "avatar") val avatar: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "repository") val repository: String,
    @ColumnInfo(name = "company") val company: String,
    @ColumnInfo(name = "follower") val follower: String,
    @ColumnInfo(name = "following") val following: String,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "type") val type: String,
) : Serializable