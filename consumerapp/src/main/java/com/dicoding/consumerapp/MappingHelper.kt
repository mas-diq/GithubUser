package com.dicoding.consumerapp

import android.database.Cursor

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val list = ArrayList<User>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.ID))
                val avatar =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.AVATAR_URL))
                val username =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.USERNAME))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.NAME))
                val location =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.LOCATION))
                val repository =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.REPOSITORY))
                val company =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.COMPANY))
                val follower =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.FOLLOWER))
                val following =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.FOLLOWING))
                val type =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.TYPE))
                list.add(
                    User(
                        avatar,
                        username,
                        name,
                        location,
                        repository,
                        company,
                        follower,
                        following,
                        id,
                        type
                    )
                )
            }
        }
        return list
    }
}