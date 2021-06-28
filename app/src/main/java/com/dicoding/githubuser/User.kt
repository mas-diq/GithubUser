package com.dicoding.githubuser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var avatar: String = "",
    var username: String = "",
    var name: String = "",
    var location: String = "",
    var repository: String = "",
    var company: String = "",
    var follower: String = "",
    var following: String = "",
    var id: Int = 0,
    var type: String = ""
) : Parcelable