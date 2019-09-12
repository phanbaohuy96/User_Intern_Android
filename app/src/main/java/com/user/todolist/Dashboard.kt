package com.user.todolist

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.user.todolist.adapter.*
import kotlinx.android.synthetic.main.activity_dashboard.*


class Dashboard : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {
    override fun onItemClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: RecyclerViewAdapter
    private lateinit var mRecyclerList:ArrayList<Table>

    private lateinit var mViewPager: ViewPager
    private lateinit var mViewPagerAdapter: ViewPagerAdapter
    private lateinit var mViewPagerList: ArrayList<Task>
    var colors: Array<Int>? = null
    var argbEvaluator = ArgbEvaluator()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mRecyclerList = ArrayList()
        mViewPagerList = ArrayList()
        getTable()
        getTask()


    }

    private fun getTask() {
        mViewPagerList.add(Task("TITLE 1", "Subtask 1"))
        mViewPagerList.add(Task("TITLE 2", "Subtask 2"))
        mViewPagerList.add(Task("TITLE 3", "Subtask 3"))
        mViewPagerList.add(Task("TITLE 4", "Subtask 4"))
        mViewPagerList.add(Task("TITLE 5", "Subtask 5"))
        initViewPager()
    }
    private fun initViewPager() {
        mViewPagerAdapter = ViewPagerAdapter(mViewPagerList, this)
        mViewPager = findViewById(R.id.viewPager)
        mViewPager.setAdapter(mViewPagerAdapter)
        mViewPager.setPadding(130,0,130,0)

        /*val colors_temp = arrayOf<Int>(getResources().getColor(R.color.color1), getResources().getColor(R.color.color2), getResources().getColor(R.color.color3), getResources().getColor(R.color.color4))
        colors = colors_temp
        viewPager.setOnPageChangeListener(object:ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position:Int, positionOffset:Float, positionOffsetPixels:Int) {
                if (position < (mViewPagerAdapter.getCount() - 1) && position < (colors!!.size - 1))
                {
                    viewPager.setBackgroundColor(
                        argbEvaluator.evaluate(
                            positionOffset,
                            colors!![position],
                            colors!![position + 1]
                        ) as Int
                    )
                }
                else
                {
                    viewPager.setBackgroundColor(colors!![colors!!.size - 1])
                }
            }
            override fun onPageSelected(position:Int) {
            }
            override fun onPageScrollStateChanged(state:Int) {
            }
        })*/
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

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.setLayoutManager(layoutManager)
        mRecyclerAdapter = RecyclerViewAdapter(this, mRecyclerList)
        mRecyclerView.setAdapter(mRecyclerAdapter)
        mRecyclerAdapter.setOnItemClickListener(this@Dashboard)
    }


}
