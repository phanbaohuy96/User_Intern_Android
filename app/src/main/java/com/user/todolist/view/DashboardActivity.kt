package com.user.todolist

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.user.todolist.adapter.*
import java.text.DateFormat
import java.util.Calendar
import android.widget.EditText
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

import com.user.todolist.viewmodel.DashboardViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlin.collections.ArrayList
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.user.todolist.databinding.ActivityDashboardBinding
import com.user.todolist.model.Table
import com.user.todolist.model.Task

@Suppress("DEPRECATION")
class DashboardActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener,
    ViewPagerAdapter.OnItemClickListener {
    lateinit var addTableDialog: Dialog
    lateinit var addTaskDialog: Dialog


    lateinit var binding: ActivityDashboardBinding
    lateinit var dashboardViewModel: DashboardViewModel

    var fbAuth = FirebaseAuth.getInstance()


    override fun onItemClick(position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        setCurrentDate()

        dashboardViewModel.mRecyclerListTable = ArrayList()
        dashboardViewModel.mRecyclerListTask = ArrayList()

        dashboardViewModel.getTable()
        initRecyclerView()
        dashboardViewModel.getTask()
        initViewPager()
        addTable()
        addTask()


        buttonLogout.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            signOut()
        }

    }

    /** Methods for updating the UI **/
    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)

        binding.textViewDate.text = currentDate
    }

    fun initViewPager() {
        dashboardViewModel.layoutManagerTask = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.viewPager.isNestedScrollingEnabled = false
        binding.viewPager.layoutManager = dashboardViewModel.layoutManagerTask
        dashboardViewModel.mRecyclerAdapterTask = ViewPagerAdapter(this, dashboardViewModel.mRecyclerListTask)
        binding.viewPager.adapter = dashboardViewModel.mRecyclerAdapterTask
        dashboardViewModel.mRecyclerAdapterTask.setOnItemClickListener(this@DashboardActivity)
    }

    fun initRecyclerView() {
        dashboardViewModel.layoutManagerTable = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.isNestedScrollingEnabled = false
        binding.recyclerView.layoutManager = dashboardViewModel.layoutManagerTable
        dashboardViewModel.mRecyclerAdapterTable = RecyclerViewAdapter(this, dashboardViewModel.mRecyclerListTable)
        binding.recyclerView.adapter = dashboardViewModel.mRecyclerAdapterTable
        dashboardViewModel.mRecyclerAdapterTable.setOnItemClickListener(this@DashboardActivity)
    }

    fun addTask() {
        binding.buttonAddTask.setOnClickListener {
            addTaskDialog = Dialog(this@DashboardActivity)
            addTaskDialog.setTitle("Save Your Name")
            addTaskDialog.setContentView(R.layout.dialog_add_task)
            val addTaskName = addTaskDialog.findViewById(R.id.addTaskName) as EditText
            val addTaskSaveName = addTaskDialog.findViewById(R.id.addTaskSaveName) as Button

            addTaskName.isEnabled = true
            addTaskSaveName.isEnabled = true

            addTaskSaveName.setOnClickListener {
                dashboardViewModel.mRecyclerAdapterTask.addItem(
                    dashboardViewModel.mRecyclerAdapterTask.itemCount,
                    addTaskName.text.toString()
                )
            }
            addTaskDialog.show()
        }

    }

    fun addTable() {
        buttonAddTable.setOnClickListener {
            addTableDialog = Dialog(this@DashboardActivity)
            addTableDialog.setTitle("Save Your Name")
            addTableDialog.setContentView(R.layout.dialog_add_table)
            val addTableName = addTableDialog.findViewById(R.id.addTableName) as EditText
            val addTableSaveName = addTableDialog.findViewById(R.id.addTableSaveName) as Button

            addTableName.isEnabled = true
            addTableSaveName.isEnabled = true

            addTableSaveName.setOnClickListener {
                dashboardViewModel.mRecyclerAdapterTable.addItem(
                    dashboardViewModel.mRecyclerAdapterTable.itemCount,
                    addTableName.text.toString()
                )
            }
            addTableDialog.show()
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when {
            item.title == "Delete table" -> {
                dashboardViewModel.mRecyclerAdapterTable.removeItem(item.groupId)
                return true
            }
            item.title == "Delete task" -> {
                dashboardViewModel.mRecyclerAdapterTask.removeItem(item.groupId)
                return true
            }
        }
        return true
    }
    fun signOut() {
        fbAuth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        fbAuth.addAuthStateListener {
            if (fbAuth.currentUser == null) {
                this.finish()
            }
        }
    }

}
