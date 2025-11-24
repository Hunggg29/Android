package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class EmailAdapter(
    private val ctx: Context,
    private val data: List<Email>
) : ArrayAdapter<Email>(ctx, 0, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(ctx)
            .inflate(R.layout.item_email, parent, false)

        val tvAvatar = view.findViewById<TextView>(R.id.tvAvatar)
        val tvSender = view.findViewById<TextView>(R.id.tvSender)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)

        val email = data[position]

        // Avatar lấy ký tự đầu tiên
        tvAvatar.text = email.sender.take(1)

        tvSender.text = email.sender
        tvTime.text = email.time
        tvTitle.text = email.title

        return view
    }
}
