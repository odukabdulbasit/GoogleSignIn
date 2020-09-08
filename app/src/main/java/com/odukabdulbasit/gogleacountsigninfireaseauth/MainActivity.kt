package com.odukabdulbasit.gogleacountsigninfireaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    var auth : FirebaseAuth? = null

    lateinit var mGoogleSignInClient : GoogleSignInClient
    lateinit var gso : GoogleSignInOptions

    val RC_SIGN_IN = 1
    lateinit var signOut : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val signInBtn = findViewById<View>(R.id.signInBtn) as SignInButton

        // Configure Google Sign In
         gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           // .requestIdToken(getString(R.string.default_web_client_id))
             .requestIdToken("1054622253078-1s5ujedip49goem4rjqutv8i3p2lbja6.apps.googleusercontent.com")
            .requestEmail()
            .build()



        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        signOut = findViewById(R.id.signOutBtn)
        signOut.visibility = View.INVISIBLE

        signInBtn.setOnClickListener {

            signInGoogle()
        }

    }

    private fun signInGoogle() {
        var signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                      //Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                Log.i("userId", "firebaseAuthWithGoogle:" + account.id)
                Log.i("userId", "firebaseAuthWithGoogle:" + account.idToken)
                //firebaseAuthWithGoogle(account.idToken!!)
                firebaseAuthWithGoogle(account)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.i("message", "Google sign in failed", e)
                // ...
            }
        }
    }


    fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    //Login
                   // moveMainPage(task.result?.user)
                    Toast.makeText(this,"giris islemi basarili.",Toast.LENGTH_LONG).show()
                }else{
                    //Show the error message
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
                }
            }
    }
}
