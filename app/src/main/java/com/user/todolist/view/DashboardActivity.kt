package com.user.todolist.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.user.todolist.R
import com.user.todolist.adapter.RecyclerViewAdapter
import com.user.todolist.adapter.ViewPagerAdapter
import com.user.todolist.databinding.ActivityDashboardBinding
import com.user.todolist.model.Table
import com.user.todolist.model.Task
import com.user.todolist.model.User
import com.user.todolist.retofit.APIClient
import com.user.todolist.viewmodel.DashboardViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.text.DateFormat
import java.util.Calendar
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class DashboardActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {
    private lateinit var addTaskDialog: Dialog


    private lateinit var binding: ActivityDashboardBinding
    lateinit var dashboardViewModel: DashboardViewModel

    private var fbAuth = FirebaseAuth.getInstance()
    private var client = APIClient()

    private var loginToken: String? = null
    private var boardID: String? = null
    private var pos: Int = 0

    private var mUser: FirebaseUser? = null


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        loginToken = intent.getStringExtra("Token")
        boardID = intent.getStringExtra("BoardID")
        client.getUSER(loginToken!!).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponseGET)
        Log.d("boardID", boardID)

        setCurrentDate()

        dashboardViewModel.mRecyclerListTable = ArrayList()
        dashboardViewModel.mListTasks = ArrayList()


        getTasks()
        addTask()
        initRecyclerView()


        buttonLogout.setOnClickListener {
//            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
//            signOut()
            logoutHandler()
        }

    }

    /** Methods for updating the UI **/
    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)

        binding.textViewDate.text = currentDate
    }


    private fun initRecyclerView() {
        dashboardViewModel.layoutManagerTable =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.isNestedScrollingEnabled = false
        binding.recyclerView.layoutManager = dashboardViewModel.layoutManagerTable
        dashboardViewModel.mRecyclerAdapterTable =
            RecyclerViewAdapter(this, dashboardViewModel.mRecyclerListTable)
        binding.recyclerView.adapter = dashboardViewModel.mRecyclerAdapterTable
        dashboardViewModel.mRecyclerAdapterTable.setOnItemClickListener(this@DashboardActivity)
    }

    @SuppressLint("CheckResult")
    private fun addTask() {
        binding.buttonAddTask.setOnClickListener {
            addTaskDialog = Dialog(this@DashboardActivity)
            addTaskDialog.setTitle("Save Your Name")
            addTaskDialog.setContentView(R.layout.dialog_add_task)
            val addTaskName = addTaskDialog.findViewById(R.id.addTaskName) as EditText
            val addTaskStatus = addTaskDialog.findViewById(R.id.addTaskStatus) as EditText
            val addTaskSaveName = addTaskDialog.findViewById(R.id.addTaskSaveName) as Button

            addTaskName.isEnabled = true
            addTaskStatus.isEnabled = true
            addTaskSaveName.isEnabled = true


            addTaskSaveName.setOnClickListener {
                val newTaskName = addTaskName.text.toString()
                val newTaskStatus = addTaskStatus.text.toString()

                val task = Task(newTaskName, newTaskStatus)
                //dashboardViewModel.mListTasks.add(task)
                var i = 0
                if (dashboardViewModel.mRecyclerListTable.size == 0)
                    dashboardViewModel.mRecyclerListTable.add(Table(newTaskStatus))
                else
                    for (it in dashboardViewModel.mRecyclerListTable) {
                        if (it.status != newTaskStatus)
                            i += 1
                        if (i == dashboardViewModel.mRecyclerListTable.size) {
                            dashboardViewModel.mRecyclerAdapterTable.addItem(
                                dashboardViewModel
                                    .mRecyclerAdapterTable
                                    .itemCount, newTaskStatus
                            )
                        }
                    }
               // dashboardViewModel.mRecyclerAdapterTable.getTables()
                client.createTASK(loginToken!!, boardID!!, task)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponseCREATETASK, this::handleERRORCREATETASK)
                addTaskDialog.dismiss()
            }
            addTaskDialog.show()
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when {
            item.title == "Delete status" -> {

                for (it in dashboardViewModel.mListTasks)
                    if (it.status == dashboardViewModel.mRecyclerListTable[item.groupId].status) {
                        // dashboardViewModel.removeTask(it)
                        deleteTask(it)
                    }
                dashboardViewModel.mRecyclerAdapterTable.removeItem(item.groupId)
                return true
            }
            item.title == "Rename status" -> {
                renameStatus(item.groupId)
//                for (it in dashboardViewModel.mListTasks)
//                  //  if (it.status == dashboardViewModel.mRecyclerListTable[item.groupId].status)
//                        renameTask(it)
                openFragment(pos)
                return true
            }
        }
        return true
    }

    private fun renameStatus(position: Int) {
        val renameStatusDialog = Dialog(this@DashboardActivity)
        renameStatusDialog.setTitle("Save Your Name")
        renameStatusDialog.setContentView(R.layout.dialog_rename_table)
        val renameStatus = renameStatusDialog.findViewById(R.id.renameStatus) as EditText
        val renameStatusSave = renameStatusDialog.findViewById(R.id.renameStatusSave) as Button
        renameStatusSave.setOnClickListener {
            dashboardViewModel.renameTable(
                dashboardViewModel.mRecyclerListTable[position],
                renameStatus.text.toString()
            )
            var check = false
            for (it in dashboardViewModel.mRecyclerListTable)
                if (it.status == renameStatus.text.toString())
                    check = true
            if (!check)
                dashboardViewModel.mRecyclerListTable[position].status =
                    renameStatus.text.toString()
            else
                dashboardViewModel.mRecyclerListTable.removeAt(position)
            dashboardViewModel.mRecyclerAdapterTable.getTables()
            for (it in dashboardViewModel.mListTasks)
                renameTask(it)
            renameStatusDialog.dismiss()
        }
        renameStatusDialog.show()
    }

    private fun signOut() {
        fbAuth.signOut()
        fbAuth.addAuthStateListener {
            if (fbAuth.currentUser == null) {
                this.finish()
            }
        }
        Toast.makeText(this,"Logging out...",Toast.LENGTH_SHORT).show()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    fun deleteTask(task: Task) {
        Log.d("Task deleted: ", task.status + task.taskName)
        client.deleteTASK(loginToken!!, boardID!!, task)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    @SuppressLint("CheckResult")
    fun renameTask(task: Task) {
        client.renameTASK(loginToken!!, boardID!!, task)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponseRENAMETASK)
    }

    private fun handleResponseRENAMETASK(task: Task) {

    }


    @SuppressLint("CheckResult")
    private fun getTasks() {
        client.getTASKS(loginToken!!, boardID!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponseGETTASKS)
    }

    private fun handleResponseGET(user: User) {
        binding.user = user
        Log.d("User: ", user.firstName)
    }

    private fun handleResponseGETTASKS(tasks: List<Task>) {
        if (tasks.isEmpty())
            addTask()
        for (it in tasks) {
            dashboardViewModel.mListTasks.add(it)
            //dashboardViewModel.mRecyclerListTable.add(Table(it.status))
            for (it1 in dashboardViewModel.mListTasks) {
                var check = false
                for (it2 in dashboardViewModel.mRecyclerListTable)
                    if (it2.status == it1.status)
                        check = true
                if (!check)
                    dashboardViewModel.mRecyclerListTable.add(Table(it.status))
            }
            dashboardViewModel.mRecyclerAdapterTable.getTables()
            //dashboardViewModel.mRecyclerAdapterTask.getTasks()
        }
    }

    private fun handleResponseCREATETASK(task: Task) {
        // Log.d("create task",task.taskName)
        dashboardViewModel.mListTasks.add(task)
        openFragment(pos)
    }

    private fun handleERRORCREATETASK(e: Throwable) {
        Log.d("error", e.toString())
    }

    override fun onItemClick(position: Int) {
        Log.d("aaa", "a")
        pos = position

        dashboardViewModel.mRecyclerAdapterTable.rowIndex = position
        dashboardViewModel.mRecyclerAdapterTable.getTables()

        if (dashboardViewModel.mListTasks.isNotEmpty())
            openFragment(position)
    }


    private fun openFragment(position: Int) {
        val listTasks: ArrayList<Task> = ArrayList()
        val bundle = Bundle()
        for (it in dashboardViewModel.mListTasks)
            if (dashboardViewModel.mRecyclerListTable[position].status == it.status)
                listTasks.add(it)
        bundle.putParcelableArrayList("List tasks", listTasks)
        bundle.putString("Login token", loginToken!!)
        bundle.putString("BoardID", boardID!!)

        for (it in listTasks)
            Log.d("list task dashboard", it.taskName + " " + it.taskID)
        val fragment = TaskFragment.newInstance(listTasks)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.taskFragment, fragment)
        fragmentTransaction.commit()
    }
    private fun logoutHandler() {
        val logoutDialog = Dialog(this@DashboardActivity)
        logoutDialog.setTitle("!!! Leave application?")
        logoutDialog.setContentView(R.layout.dialog_logout)
        val no = logoutDialog.findViewById(R.id.NO) as Button
        val yes = logoutDialog.findViewById(R.id.YES) as Button

        no.isEnabled = true
        yes.isEnabled = true

        no.setOnClickListener {
            logoutDialog.dismiss()
        }
        yes.setOnClickListener {
            signOut()
            logoutDialog.dismiss()
        }

        logoutDialog.show()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainBoardActivity::class.java)
        intent.putExtra("Token",loginToken)
        startActivity(intent)
    }
}
