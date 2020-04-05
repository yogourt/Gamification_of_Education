package com.blogspot.android_czy_java.apps.mgr.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.classroom_api.ClassroomService
import com.blogspot.android_czy_java.apps.mgr.main.classroom_api.FetchCoursesDataOperation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.classroom.ClassroomScopes
import com.google.android.gms.common.api.ApiException
import dagger.android.AndroidInjection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 100
        private const val RC_COURSES_PERMISSIONS = 400
    }

    @Inject
    lateinit var fetchCoursesDataOperation: FetchCoursesDataOperation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)

        if (userHasPermissions()) {
            button_sign_in.visibility = View.GONE
            initClassroomService(GoogleSignIn.getLastSignedInAccount(this)?.serverAuthCode)
        }
    }

    fun signIn(view: View) = signIn()
    private fun signIn() {
        val lastSignedIn = GoogleSignIn.getLastSignedInAccount(this)
        if (lastSignedIn != null) {
            GoogleSignIn.requestPermissions(
                this,
                RC_COURSES_PERMISSIONS,
                lastSignedIn,
                Scope(ClassroomScopes.CLASSROOM_COURSES_READONLY)
            )
        } else {
            val signInClient = GoogleSignIn.getClient(
                this,
                createGoogleSignInOptions()
            )
            startActivityForResult(signInClient.signInIntent, RC_SIGN_IN)
        }
    }

    private fun userHasPermissions() =
        GoogleSignIn.getLastSignedInAccount(this) != null && GoogleSignIn.hasPermissions(
            GoogleSignIn.getLastSignedInAccount(this),
            Scope(ClassroomScopes.CLASSROOM_COURSES_READONLY)
        )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            try {
                val authCode =
                    GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)?.serverAuthCode
                initClassroomService(authCode)
            } catch (e: ApiException) {

            }
        }
        if (requestCode == RC_COURSES_PERMISSIONS && resultCode == Activity.RESULT_OK) {
            GoogleSignIn.getLastSignedInAccount(this)?.serverAuthCode?.apply { initClassroomService(this) }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(ClassroomScopes.CLASSROOM_COURSES_READONLY))
            .requestServerAuthCode(resources.getString(R.string.web_client_id))
            .build()
    }

    private fun initClassroomService(authCode: String?) {
        authCode?.let {
            ClassroomService.init(it, getString(R.string.web_client_id), getString(R.string.web_client_secret))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        fetchCoursesDataOperation.execute()
                        button_sign_in.visibility = View.GONE
                    },
                    {
                        signOut()
                        button_sign_in.visibility = View.VISIBLE
                    })
        }
    }

    private fun signOut() {
        GoogleSignIn.getClient(
            this,
            createGoogleSignInOptions()
        ).signOut()
    }

}