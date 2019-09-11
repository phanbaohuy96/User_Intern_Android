package com.user.todolist.adapter

import androidx.cardview.widget.CardView


interface CardAdapter {

    val baseElevation: Float

    val count: Int

    fun getCardViewAt(position: Int): CardView

    companion object {

        val MAX_ELEVATION_FACTOR = 8
    }
}
