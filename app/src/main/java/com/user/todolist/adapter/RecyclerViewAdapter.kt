package com.user.todolist.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import com.user.todolist.R


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class RecyclerViewAdapter(context:Context, exampleList:ArrayList<Table>):RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {
    private val mContext: Context
    private val mRecyclerList:ArrayList<Table>
    private lateinit var mListener: OnItemClickListener
    var row_index = -1

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentTable: Table = mRecyclerList.get(position)

        holder.buttonTable.setText(currentTable.Name)

        holder.buttonTable.setOnClickListener {
            row_index = position
            notifyDataSetChanged()
        }
        if (row_index === position) {
            holder.buttonTable.setBackgroundResource(R.drawable.myselectedbutton)
            holder.buttonTable.setTextColor(Color.parseColor("#000000"))
        } else {
            holder.buttonTable.setBackgroundResource(R.drawable.mybutton)
            holder.buttonTable.setTextColor(Color.parseColor("#a3a3a3"))
        }
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
    init{
        mContext = context
        mRecyclerList = exampleList
    }
    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): RecyclerViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.table_item, parent, false)
        return RecyclerViewHolder(v)
    }

    inner class RecyclerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnLongClickListener {
        override fun onLongClick(p0: View?): Boolean {
            Toast.makeText(mContext, "long click", Toast.LENGTH_SHORT).show()
            return true
        }

        var buttonTable:Button
        init{
            itemView.setOnLongClickListener(this)
            buttonTable = itemView.findViewById(R.id.buttonTable)
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v:View) {
                    val position = getAdapterPosition()
                    if (position != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemClick(position)
                    }

                }
            })
        }
    }
}
