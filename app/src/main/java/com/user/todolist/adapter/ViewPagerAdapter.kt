package com.user.todolist.adapter

import android.view.ViewGroup
import com.user.todolist.R
import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager.widget.PagerAdapter


class ViewPagerAdapter(private val models: List<Task>, private val context: Context) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    override fun getCount(): Int {
        return models.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater!!.inflate(R.layout.task_item, container, false)

        val title: TextView
        val desc: TextView


        title = view.findViewById(R.id.title)
        desc = view.findViewById(R.id.subtask)

        title.setText(models[position].title)
        desc.setText(models[position].subtask)

        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}