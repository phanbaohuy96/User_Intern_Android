package com.user.todolist.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Suppress("UNREACHABLE_CODE")
class Task constructor(name:String, stt: String) : Parcelable {
    @SerializedName("taskID")
    var taskID: String = ""
    @SerializedName("taskName")
    var taskName: String = name
    @SerializedName("status")
    var status: String = stt

    constructor(parcel: Parcel) : this(
        TODO("name"),
        TODO("stt")
    ) {
        taskID = parcel.readString().toString()
        taskName = parcel.readString().toString()
        status = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(taskID)
        parcel.writeString(taskName)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}