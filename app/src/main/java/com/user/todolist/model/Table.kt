package com.user.todolist.model

import com.google.gson.annotations.SerializedName

class Table(var name:String) {
    @SerializedName("status")
    var status: String = name
    @SerializedName("count")
    var count: String = ""
}