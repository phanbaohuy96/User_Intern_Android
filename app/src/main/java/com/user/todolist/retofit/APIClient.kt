package com.user.todolist.retofit

import android.text.BoringLayout
import android.util.Log
import com.user.todolist.model.Board
import com.user.todolist.model.Task
import com.user.todolist.model.User
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class APIClient {
    private val BASE_URL = "http://103.221.223.126:400"
    private val client: APIInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        client = retrofit.create(APIInterface::class.java)
    }

    fun getUSER(token: String): Observable<User> {
        return client.getUser(token)
    }

    fun createUSER(tokens: String, user: User): Observable<User> {
        return client.createUser(tokens, user)
    }

    fun createBOARD(token: String, board: Board): Observable<Board> {
        return client.createBoard(token, board)
    }

    fun getBOARDS(token: String): Observable<List<Board>> {
        return client.getBoards(token)
    }

    fun deleteBOARD(token: String, boardID: String): Observable<Response<Void>> {
        return client.deleteBoard(token, boardID)
    }

    fun renameBOARD(token: String, boardID: String, board: Board): Observable<Board> {
        return client.renameBoard(boardID, token, board)
    }

    fun getTASKS(token: String, boardID: String): Observable<List<Task>> {
        return client.getTasks(boardID, token)
    }

    fun createTASK(token: String, boardID: String, task: Task): Observable<Task> {
        return client.createTask(boardID, token, task)
    }

    fun deleteTASK(
        token: String,
        boardID: String,
        task: Task
    ): Observable<Response<Void>> {
        Log.d("Taskdelete", task.status + " " + task.taskName)
        return client.deleteTask(boardID, token, task)
    }

    fun renameTASK(token: String, boardID: String, task: Task): Observable<Task> {
        return client.renameTask(boardID, token, task)
    }
}