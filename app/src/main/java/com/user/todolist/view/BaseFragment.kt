package com.user.todolist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment(), OnBackPressed {

    override fun onBackPressed() {}
}

interface OnBackPressed {
    fun onBackPressed(){

    }
}
