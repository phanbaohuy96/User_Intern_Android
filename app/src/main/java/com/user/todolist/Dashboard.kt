package com.user.todolist

import android.animation.ArgbEvaluator
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.user.todolist.adapter.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import android.widget.TextView
import java.text.DateFormat;
import java.util.Calendar;
import java.util.*
import android.R.string.cancel
import android.widget.EditText
import android.content.SharedPreferences
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.table_item.*


@Suppress("DEPRECATION")
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

    private lateinit var buttonAddTable: Button
    private lateinit var addTableDialog: Dialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard)

        setCurrentDate()

        mRecyclerList = ArrayList()
        mViewPagerList = ArrayList()

        this.getTable()
        this.getTask()
        this.addTable()

    }

    private fun addTable() {
        buttonAddTable = findViewById(R.id.buttonAddTable)
        buttonAddTable.setOnClickListener {
            addTableDialog = Dialog(this@Dashboard)
            addTableDialog.setTitle("Save Your Name")
            addTableDialog.setContentView(R.layout.dialog_add_table)
            val addTableName = addTableDialog.findViewById(R.id.addTableName) as EditText
            val addTableSaveName = addTableDialog.findViewById(R.id.addTableSaveName) as Button

            addTableName.isEnabled = true
            addTableSaveName.isEnabled = true

            addTableSaveName.setOnClickListener { mRecyclerAdapter.addItem(mRecyclerAdapter.itemCount,addTableName.text.toString()) }
            addTableDialog.show()
        }

    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime())

        val textViewDate:TextView = findViewById(R.id.textViewDate)
        textViewDate.text = currentDate
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
        mViewPager.adapter = mViewPagerAdapter
        mViewPager.setPadding(130,0,130,0)

        val colors_temp = arrayOf(resources.getColor(R.color.color1), resources.getColor(R.color.color2), resources.getColor(R.color.color3), resources.getColor(R.color.color4))
        colors = colors_temp
        viewPager.setOnPageChangeListener(object:ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position:Int, positionOffset:Float, positionOffsetPixels:Int) {
                if (position < (mViewPagerAdapter.count - 1) && position < (colors!!.size - 1))
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
        })
    }

    private fun getTable(){
        mRecyclerList.add(Table("Movie"))
        mRecyclerList.add(Table("Travel"))
        mRecyclerList.add(Table("Food"))
        mRecyclerList.add(Table("Animal"))
        mRecyclerList.add(Table("Job"))
        mRecyclerList.add(Table("Work"))
        mRecyclerList.add(Table("Fun"))
        mRecyclerList.add(Table("Chill"))
        mRecyclerList.add(Table("Friends"))
        mRecyclerList.add(Table("Tips"))
        mRecyclerList.add(Table("Calendar"))
        initRecyclerView()
    }

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.layoutManager = layoutManager
        mRecyclerAdapter = RecyclerViewAdapter(this, mRecyclerList)
        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerAdapter.setOnItemClickListener(this@Dashboard)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        mRecyclerAdapter.removeItem(item.groupId)
        return true
    }
}
