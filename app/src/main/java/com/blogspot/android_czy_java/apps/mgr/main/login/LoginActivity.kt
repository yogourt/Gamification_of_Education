package com.blogspot.android_czy_java.apps.mgr.main.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    companion object {
        private const val RC_SIGN_IN = 100
    }

    @Inject
    lateinit var presenter: LoginPresenter

    private lateinit var signInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        signInClient = GoogleSignIn.getClient(
            this,
            presenter.googleSignInOptions
        )

        if (!presenter.isFetchNeeded()) {
            startMainActivity()
        } else {
            observeFetchStateLiveData()

            if (presenter.isNewUser()) {
                loginUserAndFetchData()
            } else {
                fetchDataFromClassroom()
            }
        }
    }

    private fun fetchDataFromClassroom() {
        presenter.fetchFromClassroomApi()
    }

    private fun observeFetchStateLiveData() {
        presenter.fetchState.observe(this, Observer { success ->
            if (success) {
                startMainActivity()
            } else {
                logOutAndloginUser()
            }
        })
    }

    private fun loginUserAndFetchData() {
        startActivityForResult(signInClient.signInIntent, RC_SIGN_IN)
    }

    private fun logOutAndloginUser() {
        signOut()
        loginUserAndFetchData()
    }

    private fun signOut() {
       signInClient.signOut()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            if (!userHasPermissions()) {
                makeSnackbar(R.string.access_needed_msg)
            } else {
                val authCode = GoogleSignIn.getSignedInAccountFromIntent(data).result?.serverAuthCode
                if (authCode != null) {
                    saveAuthCodeAndProcess(authCode)
                } else {
                    makeSnackbar(R.string.login_error)
                    logOutAndloginUser()
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun makeSnackbar(msg: Int) {
        Snackbar.make(layout_login, getString(msg), Snackbar.LENGTH_LONG).show()
    }

    private fun saveAuthCodeAndProcess(authCode: String) {
        presenter.saveAuthCodeAndProcess(authCode)
    }

    private fun userIsLoggedIn() =
        GoogleSignIn.getLastSignedInAccount(this) != null

    private fun userHasPermissions() =
        userIsLoggedIn() && GoogleSignIn.hasPermissions(
            GoogleSignIn.getLastSignedInAccount(this), *LoginPresenter.scopes
        )

}