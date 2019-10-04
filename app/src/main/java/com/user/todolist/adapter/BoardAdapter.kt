package com.user.todolist.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.user.todolist.R
import com.user.todolist.model.Board
import com.user.todolist.retofit.APIClient
import com.user.todolist.view.DashboardActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.board_item.view.*


class BoardAdapter(context: Context, exampleList: ArrayList<Board>) :
    RecyclerView.Adapter<BoardAdapter.RecyclerViewHolder>() {
    private val mContext: Context = context
    private val mRecyclerList: ArrayList<Board> = exampleList
    private lateinit var mListener: OnItemClickListener
    private var mUser: FirebaseUser? = null
    private var idToken:String = ""
    private var client = APIClient()


    fun deleteCurrentBoard(position: Int){
        mUser = FirebaseAuth.getInstance().currentUser!!
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task: Task<GetTokenResult> ->
                if (task.isSuccessful) {
                    idToken = task.result!!.token.toString()
                    client.deleteBOARD(idToken, mRecyclerList[position].boardID)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                    removeItem(position)
                } else {
                }
            }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentBoard: Board = mRecyclerList[position]

        holder.titleBoard.text = currentBoard.boardName
        lateinit var adapterList: ListViewAdapter
        if (currentBoard.details != null) {
            adapterList = ListViewAdapter(mContext, currentBoard.details!!, currentBoard.totalTasks)
            holder.tablesBoard.adapter = adapterList
        }
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
            .inflate(R.layout.board_item, parent, false)
        return RecyclerViewHolder(v)
    }


    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        var boardViewer: CardView = itemView.findViewById(R.id.cardBoardViewer)
        var titleBoard: TextView = itemView.findViewById(R.id.title)
        var tablesBoard: ListView = itemView.findViewById(R.id.tables)
        var deleteBoard: ImageButton = itemView.findViewById(R.id.deleteButton)


        init {
            boardViewer.setOnCreateContextMenuListener(this)
            deleteBoard.setOnClickListener {
                val position = adapterPosition
                deleteCurrentBoard(position)
            }
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
            p0?.add(this.adapterPosition, R.menu.menu_main, 0, "Delete board")
            p0?.add(this.adapterPosition, R.menu.menu_main, 1, "Rename board")
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