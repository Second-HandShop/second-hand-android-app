package com.android.secondhand.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.secondhand.HomePageActivity
import com.google.firebase.auth.FirebaseAuth
import com.android.secondhand.R

class LoginActivity : AppCompatActivity() {

    //Declare an instance of FirebaseAuth
    private lateinit var auth: FirebaseAuth

    internal lateinit var userNameET: EditText
    internal lateinit var passwordET: EditText


    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Initialize an instance of FirebaseAuth
        auth = FirebaseAuth.getInstance()

        userNameET = findViewById(R.id.edit_text_email) as EditText
        passwordET = findViewById(R.id.edit_text_password) as EditText

        val login = findViewById(R.id.login) as Button
        login.setOnClickListener { login() }

        val signUpButton = findViewById(R.id.button) as Button
        signUpButton.setOnClickListener { onClickSignUp() }
    }

    // create a new user in Firebase
    fun createUser() {
        val user = auth.currentUser
        if (user != null) {
            if(user.isEmailVerified) {
                auth.createUserWithEmailAndPassword(userNameET.text.toString(), passwordET.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            goToHomePage()
                        } else {
                            Log.w("aaa", "createUserWithEmail:failure", task.exception)
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed: "+task.exception,
                                Toast.LENGTH_SHORT).show()
                        }
                        // ...
                    }
            } else {
                Toast.makeText(baseContext, "User not Verified. Please verify your email address by clicking on the link sent to the email provided.",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    //login
    fun login() {
        auth.signInWithEmailAndPassword(userNameET.text.toString(), passwordET.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    goToHomePage()
                } else {
                    Log.w("aaa", "signInWithEmail:failure", task.exception)
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed: "+task.exception,
                        Toast.LENGTH_SHORT).show()
                }
                // ...
            }
    }

    fun onClickSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
    fun goToHomePage(){
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
    }
}
