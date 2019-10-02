package com.user.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.user.todolist.adapter.BoardAdapter
import com.user.todolist.model.Board

class BoardViewModel : ViewModel(){
    lateinit var layoutManagerBoard: LinearLayoutManager
    lateinit var mRecyclerAdapterBoard: BoardAdapter
    lateinit var mRecyclerListBoard: ArrayList<Board>
}