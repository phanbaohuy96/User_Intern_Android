package com.user.todolist.adapter

import android.content.Context
import android.content.Intent
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.user.todolist.model.Board
import com.user.todolist.view.DashboardActivity


class BoardAdapter(context: Context, exampleList: ArrayList<Board>) :
    RecyclerView.Adapter<BoardAdapter.RecyclerViewHolder>() {
    private val mContext: Context = context
    private val mRecyclerList: ArrayList<Board> = exampleList
    private lateinit var mListener: OnItemClickListener

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentBoard: Board = mRecyclerList[position]

        holder.titleBoard.hint = currentBoard.boardName
       // for (it in currentBoard.details) {
        lateinit var adapterList: ListViewAdapter
        if (currentBoard.details != null) {
            adapterList = ListViewAdapter(mContext, currentBoard.details!!, currentBoard.totalTasks)
            holder.tablesBoard.adapter = adapterList
        }
        //}
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
        val v = LayoutInflater.from(mContext)
            .inflate(com.user.todolist.R.layout.board_item, parent, false)
        return RecyclerViewHolder(v)
    }


    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        var boardViewer: CardView = itemView.findViewById(com.user.todolist.R.id.cardBoardViewer)
        var titleBoard: TextView = itemView.findViewById(com.user.todolist.R.id.title)
        var tablesBoard: ListView = itemView.findViewById(com.user.todolist.R.id.tables)

        init {
            boardViewer.setOnCreateContextMenuListener(this)
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position)
                }
//                val context = itemView.context
//                val intent = Intent(context, DashboardActivity::class.java)
//                context.startActivity(intent)
            }
        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            p0?.add(this.adapterPosition, com.user.todolist.R.menu.menu_main, 0, "Delete board")
            p0?.add(this.adapterPosition, com.user.todolist.R.menu.menu_main, 1, "Rename board")
        }
    }

    fun removeItem(position: Int) {
        mRecyclerList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun addItem(position: Int, boardName: String) {
        val board = Board(boardName)
        mRecyclerList.add(position, board)
        notifyItemInserted(position)
    }

    fun getBoards() {
        notifyDataSetChanged()
    }

}