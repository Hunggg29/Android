package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class StudentAdapter(
    private val ctx: Context,
    private val data: MutableList<Pair<String, String>>,
    private val onDelete: (Int) -> Unit // Callback khi nhấn nút xóa
) : ArrayAdapter<Pair<String, String>>(ctx, 0, data) {

    // render từng icon của ListView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(ctx)
            .inflate(R.layout.item_student, parent, false)

        val tvHoTen = view.findViewById<TextView>(R.id.tvHoTen)
        val tvMSSV = view.findViewById<TextView>(R.id.tvMSSV)
        val btnDelete = view.findViewById<ImageView>(R.id.btnDelete)

        val sv = data[position]
        tvHoTen.text = sv.second
        tvMSSV.text = sv.first

        btnDelete.setOnClickListener {
            onDelete(position)
        }

        return view
    }
}
