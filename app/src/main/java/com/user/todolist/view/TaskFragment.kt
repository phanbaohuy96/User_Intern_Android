package com.user.todolist.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.user.todolist.R
import com.user.todolist.adapter.ViewPagerAdapter
import com.user.todolist.databinding.TaskFragmentBinding
import com.user.todolist.model.Task
import com.user.todolist.retofit.APIClient
import com.user.todolist.viewmodel.DashboardViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class TaskFragment : Fragment(), ViewPagerAdapter.OnItemClickListener {
    override fun onItemClick(position: Int) {
    }

    private lateinit var layoutManagerTask: LinearLayoutManager
    lateinit var mRecyclerAdapterTask: ViewPagerAdapter
    private lateinit var mRecyclerListTask: ArrayList<Task>
    lateinit var binding: TaskFragmentBinding
    private var loginToken: String? = ""
    private var boardID: String? = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        for (it in mRecyclerListTask)
            Log.d("list tasks cc: ", it.taskName)
    }

    companion object {
        @JvmStatic
        fun newInstance(listTasks: ArrayList<Task>) = TaskFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("List tasks", listTasks)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.task_fragment, container, false)

        val args = arguments
        mRecyclerListTask = ArrayList()
        if (args != null) {
            mRecyclerListTask = args.getParcelableArrayList("List tasks")!!
            loginToken = args.getString("Login token")
            boardID = args.getString("BoardID")
        }
        initViewPager()
        mRecyclerAdapterTask.getTasks()
        mRecyclerAdapterTask.onMenu = MenuItem.OnMenuItemClickListener { item ->
            when (item.title) {
                "Delete task" -> {
                    (activity as DashboardActivity).deleteTask(mRecyclerListTask[item.groupId])
                    (activity as DashboardActivity).dashboardViewModel.mListTasks.remove(
                        mRecyclerListTask[item.groupId]
                    )
                    mRecyclerAdapterTask.removeItem(item.groupId)
                }
                "Rename task" -> {
                    renameTask(item.groupId)
                }
            }
            true
        }


        return binding.root
    }

    private fun renameTask(position: Int) {
        var renameTaskDialog = Dialog(activity!!)
        renameTaskDialog.setTitle("Save Your Name")
        renameTaskDialog.setContentView(R.layout.dialog_rename_task)
        val renameTaskName = renameTaskDialog.findViewById(R.id.renameTaskName) as EditText
        val renameTaskSaveName = renameTaskDialog.findViewById(R.id.renameTaskSaveName) as Button
        renameTaskSaveName.setOnClickListener {
            (activity as DashboardActivity).dashboardViewModel.renameTask(
                mRecyclerListTask[position],
                renameTaskName.text.toString()
            )
            mRecyclerListTask[position].taskName = renameTaskName.text.toString()
            mRecyclerAdapterTask.getTasks()
            (activity as DashboardActivity).renameTask(mRecyclerListTask[position])
            renameTaskDialog.dismiss()
        }
        renameTaskDialog.show()
    }

    private fun initViewPager() {
        layoutManagerTask =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.viewPager.isNestedScrollingEnabled = false
        binding.viewPager.layoutManager = layoutManagerTask
        mRecyclerAdapterTask =
            ViewPagerAdapter(activity!!, mRecyclerListTask)
        binding.viewPager.adapter = mRecyclerAdapterTask
        mRecyclerAdapterTask.getTasks()

        for (it in mRecyclerListTask)
            Log.d("list tasks: ", it.taskName)
        mRecyclerAdapterTask.setOnItemClickListener(this)
    }
}