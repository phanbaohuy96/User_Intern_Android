package com.user.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.user.todolist.databinding.ActivitySignUpBinding
import com.user.todolist.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*


class SignUpActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(this, R.layout.activity_sign_up)
            .apply {
                this.lifecycleOwner = this@SignUpActivity
            }
        mAuth = FirebaseAuth.getInstance()

        button2.setOnClickListener {

            var email = editTextUSU.text.toString()
            var password = editTextPSU.text.toString()
            var confirmPassword = editTextCPSU.text.toString()

            if (password != confirmPassword)
                Toast.makeText(this, "Confirm password does not match", Toast.LENGTH_SHORT).show()
            else
                signUp(email, password)
//            val intent = Intent(this, DashboardActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun signUp(email: String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth!!.currentUser
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    // ...
                }
            }
    }


