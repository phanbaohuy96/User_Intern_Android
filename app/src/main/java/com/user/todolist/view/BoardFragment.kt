package com.user.todolist.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.user.todolist.R
import com.user.todolist.adapter.BoardAdapter
import com.user.todolist.databinding.FragmentBoardBinding
import com.user.todolist.model.Board
import com.user.todolist.model.User
import com.user.todolist.retofit.APIClient
import com.user.todolist.viewmodel.BoardViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.DateFormat
import java.util.*

class BoardFragment : BaseFragment(), BoardAdapter.OnItemClickListener {

    lateinit var binding: FragmentBoardBinding

    private lateinit var boardViewModel: BoardViewModel

    private var fbAuth = FirebaseAuth.getInstance()
    private var client = APIClient()

    private var loginToken: String? = null

    private var mUser: FirebaseUser? = null

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false)
        boardViewModel = ViewModelProviders.of(this).get(BoardViewModel::class.java)


        mUser = FirebaseAuth.getInstance().currentUser!!
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task: Task<GetTokenResult> ->
                if (task.isSuccessful) {
                    loginToken = task.result!!.token.toString()
                    client.getUSER(loginToken!!).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponseGET)

                    setCurrentDate()

                    getBoards()
                    initBoardViewer()
                    addBoard()
                } else {
                }
            }
        return binding.root
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)

        binding.textViewDate.text = currentDate
    }

    private fun initBoardViewer() {
        boardViewModel.layoutManagerBoard =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.boardViewer.isNestedScrollingEnabled = false
        binding.boardViewer.layoutManager = boardViewModel.layoutManagerBoard
        boardViewModel.mRecyclerAdapterBoard =
            BoardAdapter(activity!!, boardViewModel.mRecyclerListBoard)
        binding.boardViewer.adapter = boardViewModel.mRecyclerAdapterBoard
        boardViewModel.mRecyclerAdapterBoard.setOnItemClickListener(this)
    }

    private fun addBoard() {
        binding.buttonAddBoard.setOnClickListener {
            val addBoardDialog = Dialog(activity!!)
            addBoardDialog.window?.setLayout(500, 500)
            addBoardDialog.setTitle("Save Your Name")
            addBoardDialog.setContentView(R.layout.dialog_add_board)
            val addBoardName = addBoardDialog.findViewById(R.id.addBoardName) as EditText
            val addBoardSaveName = addBoardDialog.findViewById(R.id.addBoardSaveName) as Button

            addBoardName.isEnabled = true
            addBoardSaveName.isEnabled = true

            addBoardSaveName.setOnClickListener {
                var newBoardName = addBoardName.text.toString()
                if (addBoardName.text.toString() == "")
                    newBoardName = "No name"
                val board = Board(newBoardName)
                client.createBOARD(loginToken!!, board)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponseCREATEBOARD)

                addBoardDialog.dismiss()
            }
            addBoardDialog.show()
        }
    }

    private fun renameBoard(position: Int) {
        val renameBoardDialog = Dialog(activity!!)
        renameBoardDialog.setTitle("Save Your Name")
        renameBoardDialog.setContentView(R.layout.dialog_rename_board)
        val renameBoardName = renameBoardDialog.findViewById(R.id.renameBoardName) as EditText
        val renameBoardSaveName = renameBoardDialog.findViewById(R.id.renameBoardSaveName) as Button
        renameBoardSaveName.setOnClickListener {
            boardViewModel.mRecyclerListBoard[position].boardName = renameBoardName.text.toString()
            boardViewModel.mRecyclerAdapterBoard.getBoards()
            client.renameBOARD(
                loginToken!!, boardViewModel.mRecyclerListBoard[position].boardID,
                boardViewModel.mRecyclerListBoard[position]
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseRENAMEBOARD)
            renameBoardDialog.dismiss()
        }
        renameBoardDialog.show()
    }

    @SuppressLint("CheckResult")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.title == "Delete board") {
            client.deleteBOARD(
                loginToken!!,
                boardViewModel.mRecyclerListBoard[item.groupId].boardID
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
            boardViewModel.mRecyclerAdapterBoard.removeItem(item.groupId)
        } else if (item.title == "Rename board") {
            renameBoard(item.groupId)
        }
        return true
    }

    private fun signOut() {
        fbAuth.signOut()
        fbAuth.addAuthStateListener {
            if (fbAuth.currentUser == null) {
                activity!!.finish()
            }
        }
        Toast.makeText(activity, "Logging out...", Toast.LENGTH_SHORT).show()
        val intent = Intent(activity, SignInActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("CheckResult")
    private fun getBoards() {
        boardViewModel.mRecyclerListBoard = ArrayList()
        client.getBOARDS(loginToken!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponseGETBOARDS)
    }

    private fun handleResponseGET(user: User) {
        binding.user = user
        Log.d("User: ", user.firstName)
    }

    private fun handleResponseCREATE(user: User) {
        //binding.user!!.firstName = user.firstName
        Log.d("User: ", user.firstName)

    }

    private fun handleResponseCREATEBOARD(board: Board) {
        boardViewModel.mRecyclerListBoard.add(board)
        initBoardViewer()
    }

    private fun handleResponseGETBOARDS(boards: List<Board>) {
        for (it in boards) {
            boardViewModel.mRecyclerListBoard.add(it)
            boardViewModel.mRecyclerAdapterBoard.getBoards()
        }
    }

    private fun handleResponseRENAMEBOARD(board: Board) {

    }

    override fun onItemClick(position: Int) {
        val context = activity
        val intent = Intent(context, DashboardActivity::class.java)
        intent.putExtra("Token", loginToken)
        intent.putExtra("BoardID", boardViewModel.mRecyclerListBoard[position].boardID)
        Log.d(
            "clickkkkk0", boardViewModel.mRecyclerListBoard[position].boardID +
                    boardViewModel.mRecyclerListBoard[position].boardName
        )
        context?.startActivity(intent)
    }

    override fun onBackPressed() {
        logoutHandler()
        activity!!.supportFragmentManager.popBackStack()
    }

    private fun logoutHandler() {
        val logoutDialog = Dialog(activity!!)
        logoutDialog.setTitle("!!! Leave application?")
        logoutDialog.setContentView(R.layout.dialog_logout)
        val no = logoutDialog.findViewById(R.id.NO) as Button
        val yes = logoutDialog.findViewById(R.id.YES) as Button

        no.isEnabled = true
        yes.isEnabled = true

        no.setOnClickListener {
            logoutDialog.dismiss()
        }
        yes.setOnClickListener {
            signOut()
            logoutDialog.dismiss()
        }

        logoutDialog.show()
    }
}