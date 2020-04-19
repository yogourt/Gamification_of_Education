package com.blogspot.android_czy_java.apps.mgr.main.db.dao

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
}
