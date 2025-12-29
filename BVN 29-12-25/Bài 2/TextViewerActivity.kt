package com.example.myapplication.filemanager

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.File

class TextViewerActivity : AppCompatActivity() {
    
    private lateinit var tvContent: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_viewer)
        
        tvContent = findViewById(R.id.tvContent)
        
        val filePath = intent.getStringExtra("FILE_PATH")
        if (filePath != null) {
            loadTextFile(File(filePath))
        } else {
            Toast.makeText(this, "Không tìm thấy file", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    private fun loadTextFile(file: File) {
        supportActionBar?.title = file.name
        
        try {
            val content = file.readText()
            tvContent.text = content
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi đọc file: ${e.message}", Toast.LENGTH_SHORT).show()
            tvContent.text = "Không thể đọc file"
        }
    }
}
