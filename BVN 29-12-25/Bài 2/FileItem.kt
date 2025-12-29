package com.example.myapplication.filemanager

import java.io.File

data class FileItem(
    val file: File,
    val name: String = file.name,
    val path: String = file.absolutePath,
    val isDirectory: Boolean = file.isDirectory,
    val size: Long = if (file.isFile) file.length() else 0,
    val lastModified: Long = file.lastModified()
) {
    fun getDisplaySize(): String {
        if (isDirectory) return ""
        return when {
            size < 1024 -> "$size B"
            size < 1024 * 1024 -> "${size / 1024} KB"
            size < 1024 * 1024 * 1024 -> "${size / (1024 * 1024)} MB"
            else -> "${size / (1024 * 1024 * 1024)} GB"
        }
    }
    
    fun getExtension(): String {
        return if (!isDirectory && name.contains(".")) {
            name.substringAfterLast(".")
        } else ""
    }
    
    fun isTextFile(): Boolean {
        val ext = getExtension().lowercase()
        return ext in listOf("txt", "log", "json", "xml", "md")
    }
    
    fun isImageFile(): Boolean {
        val ext = getExtension().lowercase()
        return ext in listOf("jpg", "jpeg", "png", "bmp", "gif", "webp")
    }
}
