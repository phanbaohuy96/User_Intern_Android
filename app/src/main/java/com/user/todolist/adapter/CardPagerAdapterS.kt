//package com.user.todolist.adapter
//
//import android.widget.TextView
//import android.view.ViewGroup
//import androidx.cardview.widget.CardView
//import android.view.LayoutInflater
//import android.view.View
//import java.nio.file.Files.size
//import androidx.viewpager.widget.PagerAdapter
//import com.example.todolist.R
//import com.example.todolist.adapter.CardAdapter.Companion.MAX_ELEVATION_FACTOR
//
//
//class CardPagerAdapterS : PagerAdapter(), CardAdapter {
//
//    private val mViews: MutableList<CardView>
//    private val mData: MutableList<CardItemString>
//    override var baseElevation: Float = 0.toFloat()
//        private set(value: Float) {
//            super.baseElevation = value
//        }
//
//    init {
//        mData = ArrayList()
//        mViews = ArrayList()
//    }
//
//    fun addCardItemS(item: CardItemString) {
//        mViews.add(null)
//        mData.add(item)
//    }
//
//    override fun getCardViewAt(position: Int): CardView {
//        return mViews[position]
//    }
//
//    override fun getCount(): Int {
//        return mData.size
//    }
//
//    fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view === `object`
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val view = LayoutInflater.from(container.context)
//            .inflate(R.layout.adapter, container, false)
//        container.addView(view)
//        bind(mData[position], view)
//        val cardView = view.findViewById(R.id.cardView) as CardView
//
//        if (baseElevation == 0f) {
//            baseElevation = cardView.cardElevation
//        }
//
//        cardView.maxCardElevation = baseElevation * MAX_ELEVATION_FACTOR
//        mViews[position] = cardView
//        return view
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        container.removeView(`object` as View)
//        mViews.set(position, null)
//    }
//
//    private fun bind(item: CardItemString, view: View) {
////        val titleTextView = view.findViewById(R.id.titleTextView) as TextView
////        val contentTextView = view.findViewById(R.id.contentTextView) as TextView
////        titleTextView.text = item.title
////        contentTextView.text = item.text
//    }
//
//}