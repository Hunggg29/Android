package com.example.myapplication.filemanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class FileManagerActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter
    private lateinit var emptyMessage: android.widget.TextView
    private var currentDirectory: File? = null
    private var selectedItem: FileItem? = null
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d("FileManagerActivity", "Permission result: $isGranted")
        if (isGranted) {
            loadDirectory(getStorageDirectory())
        } else {
            Toast.makeText(this, "Cần quyền truy cập bộ nhớ", Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Log.d("FileManagerActivity", "onCreate started")
        
        setContentView(R.layout.activity_file_manager)
        
        Log.d("FileManagerActivity", "Layout set")
        
        // Setup Toolbar
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Quản lý File"
        
        // Setup navigation button
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        
        recyclerView = findViewById(R.id.recyclerViewFiles)
        emptyMessage = findViewById(R.id.tvEmptyMessage)
        
        Log.d("FileManagerActivity", "RecyclerView found: ${recyclerView != null}")
        
        setupRecyclerView()

    }
    
    private fun setupRecyclerView() {
        adapter = FileAdapter(
            items = emptyList(),
            onItemClick = { item -> handleItemClick(item) },
            onItemLongClick = { item, view ->
                selectedItem = item
                registerForContextMenu(view)
                openContextMenu(view)
                unregisterForContextMenu(view)
                true
            }
        )
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FileManagerActivity)
            adapter = this@FileManagerActivity.adapter
        }
    }

    private fun getStorageDirectory(): File {
        return Environment.getExternalStorageDirectory()
    }
    
    private fun loadDirectory(directory: File?) {
        Log.d("FileManagerActivity", "loadDirectory called with: ${directory?.absolutePath}")
        
        if (directory == null || !directory.exists() || !directory.isDirectory) {
            Toast.makeText(this, "Không thể truy cập thư mục", Toast.LENGTH_SHORT).show()
            Log.e("FileManagerActivity", "Invalid directory")
            return
        }
        
        currentDirectory = directory
        
        // Update title and subtitle
        supportActionBar?.apply {
            title = directory.name.ifEmpty { "Bộ nhớ" }
            subtitle = directory.absolutePath
        }
        
        try {
            val files = directory.listFiles()?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
            Log.d("FileManagerActivity", "Found ${files?.size ?: 0} files")
            val fileItems = files?.map { FileItem(it) } ?: emptyList()
            adapter.updateItems(fileItems)
            
            // Hiển thị thông báo nếu thư mục trống
            if (fileItems.isEmpty()) {
                emptyMessage.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyMessage.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
            Log.d("FileManagerActivity", "Adapter updated with ${fileItems.size} items")
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi đọc thư mục: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("FileManagerActivity", "Error loading directory", e)
        }
    }
    
    private fun handleItemClick(item: FileItem) {
        when {
            item.isDirectory -> {
                loadDirectory(item.file)
            }
            item.isTextFile() -> {
                openTextFile(item.file)
            }
            item.isImageFile() -> {
                openImageFile(item.file)
            }
            else -> {
                Toast.makeText(this, "Không hỗ trợ loại file này", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun openTextFile(file: File) {
        val intent = Intent(this, TextViewerActivity::class.java)
        intent.putExtra("FILE_PATH", file.absolutePath)
        startActivity(intent)
    }
    
    private fun openImageFile(file: File) {
        val intent = Intent(this, ImageViewerActivity::class.java)
        intent.putExtra("FILE_PATH", file.absolutePath)
        startActivity(intent)
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.file_manager_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_more_options -> {
                showCreateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun showCreateOptionsMenu() {
        // Tạo popup menu cho toolbar
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        val popupMenu = PopupMenu(this, toolbar)
        popupMenu.menuInflater.inflate(R.menu.create_options_menu, popupMenu.menu)
        
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_create_folder -> {
                    showCreateFolderDialog()
                    true
                }
                R.id.action_create_file -> {
                    showCreateFileDialog()
                    true
                }
                else -> false
            }
        }
        
        popupMenu.show()
    }
    
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        
        selectedItem?.let { item ->
            if (item.isDirectory) {
                menuInflater.inflate(R.menu.folder_context_menu, menu)
            } else {
                menuInflater.inflate(R.menu.file_context_menu, menu)
            }
        }
    }
    
    override fun onContextItemSelected(item: MenuItem): Boolean {
        selectedItem?.let { fileItem ->
            when (item.itemId) {
                R.id.action_rename -> {
                    showRenameDialog(fileItem)
                    return true
                }
                R.id.action_delete -> {
                    showDeleteDialog(fileItem)
                    return true
                }
                R.id.action_copy -> {
                    showCopyDialog(fileItem)
                    return true
                }
            }
        }
        return super.onContextItemSelected(item)
    }
    
    private fun showCreateFolderDialog() {
        val input = EditText(this).apply {
            hint = "Tên thư mục"
        }
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Tạo thư mục mới")
            .setView(input)
            .setPositiveButton("Tạo") { _, _ ->
                val folderName = input.text.toString().trim()
                if (folderName.isNotEmpty()) {
                    createFolder(folderName)
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun showCreateFileDialog() {
        val input = EditText(this).apply {
            hint = "Tên file (ví dụ: note.txt)"
        }
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Tạo file văn bản mới")
            .setView(input)
            .setPositiveButton("Tạo") { _, _ ->
                val fileName = input.text.toString().trim()
                if (fileName.isNotEmpty()) {
                    createFile(fileName)
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun showRenameDialog(fileItem: FileItem) {
        val input = EditText(this).apply {
            setText(fileItem.name)
            selectAll()
        }
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Đổi tên")
            .setView(input)
            .setPositiveButton("Đổi tên") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty() && newName != fileItem.name) {
                    renameItem(fileItem, newName)
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun showDeleteDialog(fileItem: FileItem) {
        val type = if (fileItem.isDirectory) "thư mục" else "file"
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa $type \"${fileItem.name}\"?")
            .setPositiveButton("Xóa") { _, _ ->
                deleteItem(fileItem)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun showCopyDialog(fileItem: FileItem) {
        val input = EditText(this).apply {
            hint = "Đường dẫn thư mục đích"
            setText(currentDirectory?.absolutePath)
        }
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Sao chép file")
            .setMessage("Nhập đường dẫn thư mục đích:")
            .setView(input)
            .setPositiveButton("Sao chép") { _, _ ->
                val destPath = input.text.toString().trim()
                if (destPath.isNotEmpty()) {
                    copyFile(fileItem, destPath)
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun createFolder(name: String) {
        currentDirectory?.let { dir ->
            val newFolder = File(dir, name)
            if (newFolder.exists()) {
                Toast.makeText(this, "Thư mục đã tồn tại", Toast.LENGTH_SHORT).show()
            } else {
                if (newFolder.mkdir()) {
                    Toast.makeText(this, "Đã tạo thư mục", Toast.LENGTH_SHORT).show()
                    loadDirectory(currentDirectory)
                } else {
                    Toast.makeText(this, "Không thể tạo thư mục", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun createFile(name: String) {
        currentDirectory?.let { dir ->
            val newFile = File(dir, name)
            if (newFile.exists()) {
                Toast.makeText(this, "File đã tồn tại", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    if (newFile.createNewFile()) {
                        Toast.makeText(this, "Đã tạo file", Toast.LENGTH_SHORT).show()
                        loadDirectory(currentDirectory)
                    } else {
                        Toast.makeText(this, "Không thể tạo file", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun renameItem(fileItem: FileItem, newName: String) {
        val newFile = File(fileItem.file.parent, newName)
        if (fileItem.file.renameTo(newFile)) {
            Toast.makeText(this, "Đã đổi tên", Toast.LENGTH_SHORT).show()
            loadDirectory(currentDirectory)
        } else {
            Toast.makeText(this, "Không thể đổi tên", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun deleteItem(fileItem: FileItem) {
        try {
            if (fileItem.isDirectory) {
                fileItem.file.deleteRecursively()
            } else {
                fileItem.file.delete()
            }
            Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show()
            loadDirectory(currentDirectory)
        } catch (e: Exception) {
            Toast.makeText(this, "Không thể xóa: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun copyFile(fileItem: FileItem, destPath: String) {
        try {
            val destDir = File(destPath)
            if (!destDir.exists() || !destDir.isDirectory) {
                Toast.makeText(this, "Thư mục đích không tồn tại", Toast.LENGTH_SHORT).show()
                return
            }
            
            val destFile = File(destDir, fileItem.name)
            fileItem.file.copyTo(destFile, overwrite = false)
            Toast.makeText(this, "Đã sao chép file", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi sao chép: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onBackPressed() {
        currentDirectory?.let { dir ->
            val parent = dir.parentFile
            if (parent != null && parent.canRead()) {
                loadDirectory(parent)
            } else {
                super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }
}
