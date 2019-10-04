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
import com.user.todolist.databinding.ActivitySignUpBinding
import com.user.todolist.model.Board
import com.user.todolist.model.User
import com.user.todolist.retofit.APIClient
import com.user.todolist.viewmodel.SignUpViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    //private var currentUser: User = User(ObservableField(""), ObservableField(""))
    //private var currentUser: User = User()
    private var mUser: FirebaseUser? = null
    private lateinit var signUpViewModel: SignUpViewModel
    private var username = ""
    private var client = APIClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(
            this,
            R.layout.activity_sign_up
        )
            .apply {
                this.lifecycleOwner = this@SignUpActivity
            }
        mAuth = FirebaseAuth.getInstance()
        // binding.user = currentUser

        button2.setOnClickListener {

            val confirmPassword = editTextCPSU.text.toString()
            val password = editTextPSU.text.toString()
            val firstname = editTextFNSU.text.toString()
            val lastname = editTextLNSU.text.toString()
            val userphone = editTextUPSU.text.toString()
            val birthday = editTextBDSU.text.toString()
            username = editTextUSU.text.toString()

            if (password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty() || firstname.isEmpty()
                || lastname.isEmpty() || userphone.isEmpty() || birthday.isEmpty())
                Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show()
            else if (password != confirmPassword)
                Toast.makeText(this, "Confirm password does not match", Toast.LENGTH_SHORT).show()
            else
                signUp(username, password, firstname, lastname, userphone, birthday)
        }
    }

    private fun signUp(email: String, password: String, firstname: String, lastname: String, userphone: String, birthday: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mUser = FirebaseAuth.getInstance().currentUser!!
                    mUser!!.getIdToken(true)
                        .addOnCompleteListener { task: Task<GetTokenResult> ->
                            /**this task is different from the task object!!!**/
                            if (task.isSuccessful) {
                                val idToken = task.result!!.token
                                Log.d("Token2", "signUpWithEmail:success :$idToken")

                                idToken?.let {
                                    val newUser =
                                        User(firstname, lastname, userphone, birthday, "mapnhucho")
                                    client.createUSER(idToken, newUser).observeOn(
                                        AndroidSchedulers.mainThread()
                                    )
                                        .subscribeOn(Schedulers.io())
                                        .subscribe {
                                            fun handleResponseCREATEBOARD(board: Board) {
                                                Log.d("Board created: ", board.boardID)
                                            }
                                        }
                                    val intent = Intent(this, SignInActivity::class.java)
                                    intent.putExtra("Type", "Sign Up")
                                    intent.putExtra("Username", username)
                                    startActivity(intent)
                                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                                }

                            } else {
                            }
                        }

                } else {
                    Toast.makeText(
                        this, "Authentication failed. Email as username is invalid",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}


