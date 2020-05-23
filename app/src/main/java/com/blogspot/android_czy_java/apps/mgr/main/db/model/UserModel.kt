package com.blogspot.android_czy_java.apps.mgr.main.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserModel(
    @PrimaryKey
    val id: String,
    val nickname: String,
    val photo: String?,
    val points: Long
)
