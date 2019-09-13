package com.user.todolist.adapter

import android.content.Context
import android.graphics.Color
import android.view.*
import android.view.ContextMenu;
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.user.todolist.R


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class RecyclerViewAdapter(context:Context, exampleList:ArrayList<Table>):RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {
    private val mContext: Context = context
    private val mRecyclerList:ArrayList<Table> = exampleList
    private lateinit var mListener: OnItemClickListener
    var row_index = -1


    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int){
        val currentTable: Table = mRecyclerList[position]

        holder.buttonTable.text = currentTable.Name

        holder.buttonTable.setOnClickListener {
            row_index = position
            notifyDataSetChanged()
        }
        if (row_index === position) {
            holder.buttonTable.setBackgroundResource(com.user.todolist.R.drawable.myselectedbutton)
            holder.buttonTable.setTextColor(Color.parseColor("#000000"))
        } else {
            holder.buttonTable.setBackgroundResource(com.user.todolist.R.drawable.mybutton)
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

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): RecyclerViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.table_item, parent, false)
        return RecyclerViewHolder(v)
    }


    inner class RecyclerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{
        var buttonTable:Button = itemView.findViewById(com.user.todolist.R.id.buttonTable)

        init{
            buttonTable.setOnCreateContextMenuListener(this)
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position)
                }
            }
        }
        override fun onCreateContextMenu(p0: ContextMenu?, p1: View?, p2: ContextMenu.ContextMenuInfo?) {
            p0?.add(this.adapterPosition,R.menu.menu_main,0,"Delete")
        }
    }

    fun removeItem(position: Int){
        mRecyclerList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }
    fun addItem(position: Int, tableName:String) {
        val table = Table(tableName)
        mRecyclerList.add(position, table)
        notifyItemInserted(position)
    }

}
