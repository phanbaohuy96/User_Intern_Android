package com.user.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.user.todolist.databinding.ActivityMainBinding
import com.user.todolist.viewmodel.SignInViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*


class SignInActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val signInViewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                this.lifecycleOwner = this@SignInActivity
            }

        mAuth = FirebaseAuth.getInstance()

        textViewSU.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        button1.setOnClickListener {
            //signIn(view, editTextUSI.text.toString(), editTextPSI.text.toString())
            var email = editTextUSI.text.toString()
            var password = editTextPSI.text.toString()

            if (email.isEmpty() || password.isEmpty())
                Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show()
            else
                signIn(email, password)
        }
    }


    private fun signIn(email: String, password: String) {
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    var intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
