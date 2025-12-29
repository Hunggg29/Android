package com.example.myapplication.filemanager

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.File

class ImageViewerActivity : AppCompatActivity() {
    
    private lateinit var ivImage: ImageView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)
        
        ivImage = findViewById(R.id.ivImage)
        
        val filePath = intent.getStringExtra("FILE_PATH")
        if (filePath != null) {
            loadImage(File(filePath))
        } else {
            Toast.makeText(this, "Không tìm thấy file", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    private fun loadImage(file: File) {
        supportActionBar?.title = file.name
        
        try {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (bitmap != null) {
                ivImage.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Không thể tải ảnh", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi đọc ảnh: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
