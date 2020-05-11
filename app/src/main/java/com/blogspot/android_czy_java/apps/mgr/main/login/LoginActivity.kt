package com.blogspot.android_czy_java.apps.mgr.main.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
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
                logOutAndLoginUser()
            }
        })
    }

    private fun loginUserAndFetchData() {
        startActivityForResult(signInClient.signInIntent, RC_SIGN_IN)
    }

    private fun logOutAndLoginUser() {
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
                logOutAndLoginUser()
            } else {
                val user = GoogleSignIn.getSignedInAccountFromIntent(data).result
                val authCode = user?.serverAuthCode
                if (authCode != null) {
                    saveAuthCodeAndProcess(authCode)
                    user.idToken?.let { authenticateFirebase(it) }
                } else {
                    makeSnackbar(R.string.login_error)
                    logOutAndLoginUser()
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun authenticateFirebase(idToken: String) {
        val credential = presenter.getCredentialForFirebase(idToken)
        val auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && auth.currentUser != null) {
                    auth.currentUser?.let {
                        presenter.saveUser(it)
                    }
                } else {
                    logOutAndLoginUser()
                }
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