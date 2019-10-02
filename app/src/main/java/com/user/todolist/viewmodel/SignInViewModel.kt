package com.user.todolist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GetTokenResult

class SignInViewModel: ViewModel() {
    fun callAPI(task: Task<GetTokenResult>, context: Context) {
//        if (task.isSuccessful) {
//            val idToken = task.result!!.token
//            Log.d("Token", "signInWithEmail:success :$idToken")
//            idToken?.let {
//                val intent = Intent(this, DashboardActivity::class.java)
//                startActivity(intent)
//                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//
//        }
    }
}