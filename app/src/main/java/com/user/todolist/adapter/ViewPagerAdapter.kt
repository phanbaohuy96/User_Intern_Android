package com.user.todolist.adapter

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.user.todolist.Dashboard
import com.user.todolist.R


class ViewPagerAdapter(context:Context, exampleList:ArrayList<Task>): RecyclerView.Adapter<ViewPagerAdapter.RecyclerViewHolder>() {
    private val mContext: Context = context
    private val mRecyclerList:ArrayList<Task> = exampleList
    private lateinit var mListener: OnItemClickListener
    var row_index = -1


     override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int){
        val currentTask: Task = mRecyclerList[position]

        holder.titleTask.hint = currentTask.title
        holder.subtask.text = currentTask.subtask


       /* holder.buttonTable.setOnClickListener {
            row_index = position
            notifyDataSetChanged()
        }
        if (row_index === position) {
            holder.buttonTable.setBackgroundResource(com.user.todolist.R.drawable.myselectedbutton)
            holder.buttonTable.setTextColor(Color.parseColor("#000000"))
        } else {
            holder.buttonTable.setBackgroundResource(com.user.todolist.R.drawable.mybutton)
            holder.buttonTable.setTextColor(Color.parseColor("#a3a3a3"))
        }*/
    }
    interface OnItemClickListener {
        fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun getItemCount(): Int {
        return mRecyclerList.size
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): RecyclerViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false)
        return RecyclerViewHolder(v)
    }


    inner class RecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{
        var cardViewPager: CardView = itemView.findViewById(com.user.todolist.R.id.cardViewPager)
        var titleTask: EditText = itemView.findViewById(R.id.title)
        var subtask: TextView = itemView.findViewById(R.id.subtask)

        init{
            cardViewPager.setOnCreateContextMenuListener(this)
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position)
                }
            }
        }
        override fun onCreateContextMenu(p0: ContextMenu?, p1: View?, p2: ContextMenu.ContextMenuInfo?) {
            p0?.add(this.adapterPosition,R.menu.menu_main,0,"Delete task")
        }
    }
    fun removeItem(position: Int){
        mRecyclerList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }
    fun addItem(position: Int, taskName:String) {
        val task = Task(taskName, "")
        mRecyclerList.add(position, task)
        notifyItemInserted(position)
    }


}