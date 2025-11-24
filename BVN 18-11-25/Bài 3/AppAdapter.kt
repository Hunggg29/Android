package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AppAdapter(
    private val activity: MainActivity,
    private val apps: List<App>
) : BaseAdapter() {

    override fun getCount(): Int = apps.size

    override fun getItem(position: Int): App = apps[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(activity)
            .inflate(R.layout.item_app_vertical, parent, false)

        val app = apps[position]

        val textViewName = view.findViewById<TextView>(R.id.textViewAppName)
        val textViewCategory = view.findViewById<TextView>(R.id.textViewCategory)
        val textViewRating = view.findViewById<TextView>(R.id.textViewRating)
        val textViewSize = view.findViewById<TextView>(R.id.textViewSize)
        val imageViewIcon = view.findViewById<TextView>(R.id.imageViewAppIcon)

        textViewName.text = app.name
        textViewCategory.text = app.category
        textViewRating.text = "${app.rating} ⭐"
        textViewSize.text = app.size

        // Tạo icon mẫu
        imageViewIcon.text = app.iconText
        imageViewIcon.setBackgroundColor(Color.parseColor(app.iconColor))

        return view
    }
}