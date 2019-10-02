package com.user.todolist.model

import com.google.gson.annotations.SerializedName

class Board constructor(val name:String) {
    @SerializedName("boardID")
    var boardID: String = ""
    @SerializedName("boardName")
    var boardName: String = name
    @SerializedName("details")
    var details: ArrayList<Table>? = ArrayList()
    @SerializedName("totalTasks")
    var totalTasks: String = ""
}
