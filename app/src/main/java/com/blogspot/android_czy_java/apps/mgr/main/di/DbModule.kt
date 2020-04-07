package com.blogspot.android_czy_java.apps.mgr.main.di

import android.content.Context
import com.blogspot.android_czy_java.apps.mgr.main.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context) = AppDatabase.getInstance(context)

    @Provides
    fun provideCourseDao(db: AppDatabase) = db.coursesDao

    @Provides
    fun providePreferences(context: Context) =
        context.getSharedPreferences("Gamification_Of_Education_Prefs", Context.MODE_PRIVATE)


}