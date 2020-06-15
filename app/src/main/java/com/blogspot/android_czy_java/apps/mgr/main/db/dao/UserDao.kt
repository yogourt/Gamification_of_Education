package com.blogspot.android_czy_java.apps.mgr.main.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blogspot.android_czy_java.apps.mgr.main.db.model.UserModel

@Dao
interface UserDao {

    @Query("SELECT COUNT(id) FROM users WHERE id=:userId")
    fun containsUserId(userId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserModel)

    @Query("SELECT * FROM users WHERE id=:userId")
    fun getCurrentUser(userId: String?): LiveData<UserModel>

    @Query("UPDATE users SET photo=:avatar WHERE id=:userId")
    fun updateAvatar(avatar: String, userId: String)

    @Query("UPDATE users SET nickname=:nickname WHERE id=:userId")
    fun updateNickname(userId: String, nickname: String)

    @Query("UPDATE users SET points=:points WHERE id=:userId")
    fun updatePoints(userId: String, points: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: MutableList<UserModel>)
}
