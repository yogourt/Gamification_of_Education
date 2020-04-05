package com.blogspot.android_czy_java.apps.mgr.main

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.blogspot.android_czy_java.apps.mgr.main.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MyApplication : Application(), HasSupportFragmentInjector, HasActivityInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun supportFragmentInjector() = fragmentInjector
    override fun activityInjector() = activityInjector

    override fun onCreate() {
        super.onCreate()


        DaggerAppComponent
            .builder()
            .application(this)
            .context(this)
            .build()
            .inject(this)

    }


}