package com.user.todolist.adapter

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.user.todolist.R
import com.user.todolist.model.Task
import org.w3c.dom.Text


class ViewPagerAdapter(context: Context, exampleList: ArrayList<Task>) :
    RecyclerView.Adapter<ViewPagerAdapter.RecyclerViewHolder>() {
    private val mContext: Context = context
    private val mRecyclerList: ArrayList<Task> = exampleList
    private lateinit var mListener: OnItemClickListener
    lateinit var onMenu: MenuItem.OnMenuItemClickListener


    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentTask: Task = mRecyclerList[position]

        holder.titleTask.text = currentTask.taskName
        //holder.subtask.text = currentTask.subtask
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun getItemCount(): Int {
        return mRecyclerList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false)
        return RecyclerViewHolder(v)
    }


    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        var cardViewPager: CardView = itemView.findViewById(R.id.cardViewPager)
        var titleTask: TextView = itemView.findViewById(R.id.titleTask)
        //var subtask: TextView = itemView.findViewById(R.id.subtask)

        init {
            cardViewPager.setOnCreateContextMenuListener(this)
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position)
                }
            }
        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            val delete: MenuItem? =
                p0?.add(this.adapterPosition, R.menu.menu_main, 0, "Delete task")
            val rename: MenuItem? =
                p0?.add(this.adapterPosition, R.menu.menu_main, 1, "Rename task")
            delete!!.setOnMenuItemClickListener(onMenu)
            rename!!.setOnMenuItemClickListener(onMenu)
        }

    }

    fun removeItem(position: Int) {
        mRecyclerList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun addItem(position: Int, taskName: String, status: String) {
        val task = Task(taskName, status)
        mRecyclerList.add(position, task)
        notifyItemInserted(position)
    }

    fun getTasks() {
        notifyDataSetChanged()
    }
}