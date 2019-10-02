package com.user.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.user.todolist.adapter.RecyclerViewAdapter
import com.user.todolist.adapter.ViewPagerAdapter
import com.user.todolist.model.Table
import com.user.todolist.model.Task
import java.text.FieldPosition
import java.util.*

class DashboardViewModel : ViewModel() {
    lateinit var layoutManagerTable: LinearLayoutManager
    lateinit var mRecyclerAdapterTable: RecyclerViewAdapter
    lateinit var mRecyclerListTable: ArrayList<Table>

    lateinit var mListTasks: ArrayList<Task>



    fun renameTask(task: Task, newTaskName: String) {
        for (it in mListTasks)
            if (it.taskID == task.taskID)
                it.taskName = newTaskName
    }

    fun renameTable(table: Table, stt: String) {
        for (it in mListTasks)
            if (it.status == table.status)
                it.status = stt
    }




}