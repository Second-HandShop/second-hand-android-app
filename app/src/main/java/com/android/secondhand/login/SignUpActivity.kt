package com.android.secondhand.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.secondhand.homepage.HomePageActivity
import com.android.secondhand.R
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity(), EmailVerificationFragment.ClickListener, CreateUserFragment.ClickListener {

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Initialize an instance of FirebaseAuth
        auth = FirebaseAuth.getInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, EmailVerificationFragment())
            .commit()

    }

    override fun onSendEmailVerification(emailId: String) {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { anonymousSingInTask ->
                if(anonymousSingInTask.isSuccessful) {
                    val user = auth.currentUser
                    user?.updateEmail(emailId)?.addOnCompleteListener(this) { updateEmailTask ->
                        if(updateEmailTask.isSuccessful) {
                            user.sendEmailVerification().addOnCompleteListener(this) { verifyEmailTask ->
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainerView, CreateUserFragment.newInstance(emailId))
                                    .commit()
                                if(verifyEmailTask.isSuccessful) {
                                    Toast.makeText(baseContext, "Verification email sent.",
                                        Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(baseContext, "Verification email already sent. Check your Inbox for the latest email from noreply@second-hand.",
                                        Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, CreateUserFragment.newInstance(emailId))
                                .commit()
                            Toast.makeText(baseContext, "Verification email already sent. Check your Inbox for the latest email from noreply@second-hand.",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
    }

    override fun onCreateAccount(password: String) {
        auth.currentUser?.reload()?.addOnSuccessListener { Void ->
            val user = auth.currentUser
            if (user != null) {
                if(user.isEmailVerified) {
                    user.updatePassword(password).addOnCompleteListener(this) { updatePasswordTask ->
                        if(updatePasswordTask.isSuccessful) {
                            goToHomePage()
                            //login to app
                            Toast.makeText(baseContext, "User Account Created",
                                Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(baseContext, "There was a problem creating your account.",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(baseContext, "Please verify your email first, by clicking on the link sent.",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun goToHomePage(){
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
    }
}