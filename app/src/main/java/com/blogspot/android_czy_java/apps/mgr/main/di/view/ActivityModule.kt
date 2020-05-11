package com.blogspot.android_czy_java.apps.mgr.main.di.view

import com.blogspot.android_czy_java.apps.mgr.main.MainActivity
import com.blogspot.android_czy_java.apps.mgr.main.chat.ChatFragment
import com.blogspot.android_czy_java.apps.mgr.main.course.CourseFragment
import com.blogspot.android_czy_java.apps.mgr.main.courses.CoursesFragment
import com.blogspot.android_czy_java.apps.mgr.main.login.LoginActivity
import com.blogspot.android_czy_java.apps.mgr.main.profile.ProfileFragment
import com.blogspot.android_czy_java.apps.mgr.main.task.TaskFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun bind(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindCoursesFragment(): CoursesFragment

    @ContributesAndroidInjector
    abstract fun bindCourseFragment(): CourseFragment

    @ContributesAndroidInjector
    abstract fun bindChatFragment(): ChatFragment

    @ContributesAndroidInjector
    abstract fun bindProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun bindTaskFragment(): TaskFragment

}