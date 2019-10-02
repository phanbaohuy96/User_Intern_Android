package com.user.todolist.model

import com.google.gson.annotations.SerializedName

//class User (var username:ObservableField<String>) {
//    constructor( username:ObservableField<String>,
//        firstName: ObservableField<String>,
//                 lastName: ObservableField<String>,
//                 userPhone: ObservableField<String>,
//                 birthDay: ObservableField<String>,
//                 avatarURL: ObservableField<String>) : this(username)
//    constructor(username: ObservableField<String>, password: ObservableField<String>):this(username)
//}

data class User(
//    var username: ObservableField<String>,
//    var password: ObservableField<String>,
    @SerializedName("firstName")
    var firstName: String = "",
    @SerializedName("lastName")
    var lastName: String = "",
    @SerializedName("userPhone")
    var userPhone: String ="",
    @SerializedName("birthDay")
    var birthDay: String="",
    @SerializedName("avatarURL")
    var avatarURL: String =""
)
//data class User @JvmOverloads constructor(
//    var username: String,
//    var password: String,
//    var firstName: String = "",
//    var lastName: String= "",
//    var userPhone: String= "",
//    var birthDay: String= "",
//    var avatarURL: String= ""
//)