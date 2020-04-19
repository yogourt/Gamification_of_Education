package com.blogspot.android_czy_java.apps.mgr.main.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.dao.UserDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.CourseModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.UserModel


@Database(
    entities = [
        CourseModel::class,
        TaskModel::class,
        MessageModel::class,
        UserModel::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val coursesDao: CoursesDao
    abstract val userDao: UserDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "Classroom_Gamification.db"
            ).fallbackToDestructiveMigration().build()
        }
    }

}