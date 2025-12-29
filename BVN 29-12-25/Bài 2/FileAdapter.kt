package com.example.myapplication.filemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.util.*

class FileAdapter(
    private var items: List<FileItem>,
    private val onItemClick: (FileItem) -> Unit,
    private val onItemLongClick: (FileItem, View) -> Boolean
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val tvFileName: TextView = itemView.findViewById(R.id.tvFileName)
        val tvFileInfo: TextView = itemView.findViewById(R.id.tvFileInfo)
        val btnMore: ImageButton = itemView.findViewById(R.id.btnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item = items[position]
        
        holder.tvFileName.text = item.name
        
        // Set icon based on type
        val iconRes = when {
            item.isDirectory -> android.R.drawable.ic_menu_view
            item.isImageFile() -> android.R.drawable.ic_menu_gallery
            item.isTextFile() -> android.R.drawable.ic_menu_edit
            else -> android.R.drawable.ic_menu_info_details
        }
        holder.ivIcon.setImageResource(iconRes)
        
        // Set file info
        val info = if (item.isDirectory) {
            val count = item.file.listFiles()?.size ?: 0
            "$count mục"
        } else {
            "${item.getDisplaySize()} • ${formatDate(item.lastModified)}"
        }
        holder.tvFileInfo.text = info
        
        // Click listeners
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        
        holder.itemView.setOnLongClickListener {
            onItemLongClick(item, holder.itemView)
        }
        
        // More button click listener - show context menu
        holder.btnMore.setOnClickListener {
            onItemLongClick(item, holder.itemView)
        }
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<FileItem>) {
        items = newItems
        notifyDataSetChanged()
    }
    
    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
