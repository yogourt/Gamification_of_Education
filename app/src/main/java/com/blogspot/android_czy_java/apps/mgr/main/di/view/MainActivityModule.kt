package com.blogspot.android_czy_java.apps.mgr.main.di.view

import com.blogspot.android_czy_java.apps.mgr.main.MainActivity
import com.blogspot.android_czy_java.apps.mgr.main.course.CourseFragment
import com.blogspot.android_czy_java.apps.mgr.main.courses.CoursesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun bind(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindCoursesFragment(): CoursesFragment

    @ContributesAndroidInjector
    abstract fun bindCourseFragment(): CourseFragment

}