package com.dicoding.consumerapp

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTORITY = "com.dicoding.githubuser"
    const val SCHEME = "content"

    internal class FavoriteUserColumns : BaseColumns {
        companion object {
            private const val TABLE_NAME = "userTable"
            const val AVATAR_URL = "avatar"
            const val USERNAME = "username"
            const val NAME = "name"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val COMPANY = "company"
            const val FOLLOWER = "follower"
            const val FOLLOWING = "following"
            const val ID = "id"
            const val TYPE = "type"

            val
                    CONTEN_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}