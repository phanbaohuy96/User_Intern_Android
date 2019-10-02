package com.user.todolist.retofit

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.user.todolist.model.Board
import com.user.todolist.model.Task
import com.user.todolist.model.User
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface APIInterface {
    @POST("/api/user")
    fun createUser(
        @Header("tokenIDS") tokens: String,
        @Body user: User
    ): Observable<User>

    @GET("/api/user")
    fun getUser(
        @Header("tokenID") token: String
    ): Observable<User>

    @POST("/api/user/board")
    fun createBoard(
        @Header("tokenID") token: String,
        @Body board: Board
    ): Observable<Board>

    @GET("/api/user/boards")
    fun getBoards(
        @Header("tokenID") token: String
    ): Observable<List<Board>>

    @HTTP(method = "DELETE", path = "api/user/board/{boardID}", hasBody = true)
    fun deleteBoard(
        @Header("tokenID") token: String,
        //@Body board: Board
        @Path("boardID") boardID: String
    ): Observable<Response<Void>>

    @HTTP(method = "PUT", path = "api/user/board/{boardID}", hasBody = true)
    fun renameBoard(
        @Path("boardID") boardID: String,
        @Header("tokenID") token: String,
        @Body board: Board
    ): Observable<Board>

    @GET("/api/user/board/{boardID}/tasks")
    fun getTasks(
        @Path("boardID") boardID: String,
        @Header("tokenID") token: String
    ): Observable<List<Task>>

    @POST("/api/user/board/{boardID}/task")
    fun createTask(
        @Path("boardID") boardID: String,
        @Header("tokenID") token: String,
        @Body task: Task
    ): Observable<Task>

    @HTTP(method = "DELETE", path = "api/user/board/{boardID}/task", hasBody = true)
    fun deleteTask(
        @Path("boardID") boardID: String,
        //@Path("taskID") taskID: String,
        @Header("tokenID") token: String,
        @Body task: Task
    ): Observable<Response<Void>>

    @HTTP(method = "PUT", path = "api/user/board/{boardID}/task", hasBody = true)
    fun renameTask(
        @Path("boardID") boardID: String,
        @Header("tokenID") token: String,
        @Body task: Task
    ): Observable<Task>


}