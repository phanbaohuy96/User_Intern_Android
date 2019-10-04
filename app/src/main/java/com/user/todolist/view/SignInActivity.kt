package com.user.todolist.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.user.todolist.R
import com.user.todolist.databinding.ActivityMainBinding
import com.user.todolist.model.User
import com.user.todolist.viewmodel.SignInViewModel
import kotlinx.android.synthetic.main.activity_main.*


class SignInActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null
   private var currentUser: User = User()

    private lateinit var signInViewModel:SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        signInViewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
            .apply {
                this.lifecycleOwner = this@SignInActivity
            }

        mAuth = FirebaseAuth.getInstance()


        if (intent.getStringExtra("Type") == "Sign Up" )
            editTextUSI.setText(intent.getStringExtra("Username"))


        binding.user = currentUser

        textViewSU.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        button1.setOnClickListener {
            val password = editTextPSI.text.toString()
            val username = editTextUSI.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show()
            } else
                signIn(username, password)
        }
    }


    private fun signIn(email: String, password: String) {
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mUser = FirebaseAuth.getInstance().currentUser!!
                    mUser!!.getIdToken(true)
                        .addOnCompleteListener { task: Task<GetTokenResult> ->
                            if (task.isSuccessful) {
                                val idToken = task.result!!.token
                                Log.d("Token", "signInWithEmail:success :$idToken")
                                idToken?.let {
                                    val intent = Intent(this, MainBoardActivity::class.java)
                                    intent.putExtra("Token",idToken)
                                    startActivity(intent)
                                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                                }
                            } else {

                            }
                        }

                        }
                else
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

                }
            }
}

