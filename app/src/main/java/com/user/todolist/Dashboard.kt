package com.user.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.user.todolist.adapter.RecyclerViewAdapter
import com.user.todolist.adapter.Table


class Dashboard : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {
    override fun onItemClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: RecyclerViewAdapter
    private lateinit var mRecyclerList:ArrayList<Table>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mRecyclerList = ArrayList()
        getTable()

    }

    fun getTable(){
        mRecyclerList.add(Table("Movie"))
        mRecyclerList.add(Table("Travel"))
        mRecyclerList.add(Table("Food"))
        mRecyclerList.add(Table("Animal"))
        mRecyclerList.add(Table("Job"))
        mRecyclerList.add(Table("Work"))
        initRecyclerView()
    }

    fun initRecyclerView() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.setLayoutManager(layoutManager)
        mRecyclerAdapter = RecyclerViewAdapter(this, mRecyclerList)
        mRecyclerView.setAdapter(mRecyclerAdapter)
        mRecyclerAdapter.setOnItemClickListener(this@Dashboard)
    }
}
