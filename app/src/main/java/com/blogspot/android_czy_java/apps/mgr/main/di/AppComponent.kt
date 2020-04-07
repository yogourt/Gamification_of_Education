package com.blogspot.android_czy_java.apps.mgr.main.di

import android.content.Context
import com.blogspot.android_czy_java.apps.mgr.main.MyApplication
import com.blogspot.android_czy_java.apps.mgr.main.di.view.ActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DbModule::class,
        ActivityModule::class
    ]
)
@Singleton
interface AppComponent {

    fun inject(app: MyApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: MyApplication): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }


}