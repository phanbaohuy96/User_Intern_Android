package com.user.todolist.view

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import com.user.todolist.R
import com.user.todolist.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileBinding
    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        return binding.root
    }
}
