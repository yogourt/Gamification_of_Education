package com.blogspot.android_czy_java.apps.mgr.main.di.view

import com.blogspot.android_czy_java.apps.mgr.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun bind(): MainActivity

}