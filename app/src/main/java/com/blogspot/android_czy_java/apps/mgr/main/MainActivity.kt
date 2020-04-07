package com.blogspot.android_czy_java.apps.mgr.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.classroom_api.ClassroomService
import com.blogspot.android_czy_java.apps.mgr.main.classroom_api.FetchCoursesDataUseCase
import com.blogspot.android_czy_java.apps.mgr.main.login.usecase.GetRefreshAndAccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.classroom.ClassroomScopes
import com.google.android.gms.common.api.ApiException
import dagger.android.AndroidInjection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(R.layout.activity_main) {

}