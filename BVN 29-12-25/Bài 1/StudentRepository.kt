package com.example.myapplication.repository

import com.example.myapplication.database.DatabaseHelper
import com.example.myapplication.model.Student

class StudentRepository(private val dbHelper: DatabaseHelper) {

    fun getAllStudents(): List<Student> {
        return dbHelper.getAllStudents()
    }

    fun insertStudent(student: Student): Boolean {
        return dbHelper.insertStudent(student) > 0
    }

    fun updateStudent(student: Student): Boolean {
        return dbHelper.updateStudent(student) > 0
    }

    fun deleteStudent(studentId: String): Boolean {
        return dbHelper.deleteStudent(studentId) > 0
    }

    fun getStudentById(studentId: String): Student? {
        return dbHelper.getStudentById(studentId)
    }
}
