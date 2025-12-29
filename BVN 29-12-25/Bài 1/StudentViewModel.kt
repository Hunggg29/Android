package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.DatabaseHelper
import com.example.myapplication.model.Student
import com.example.myapplication.repository.StudentRepository

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StudentRepository
    
    private val _students = MutableLiveData<MutableList<Student>>(mutableListOf())
    val students: LiveData<MutableList<Student>> = _students
    
    private val _selectedStudent = MutableLiveData<Student?>()
    val selectedStudent: LiveData<Student?> = _selectedStudent
    
    init {
        val dbHelper = DatabaseHelper(application)
        repository = StudentRepository(dbHelper)
        loadStudents()
    }
    
    private fun loadStudents() {
        val studentList = repository.getAllStudents().toMutableList()
        _students.value = studentList
    }
    
    fun addStudent(student: Student) {
        if (repository.insertStudent(student)) {
            val currentList = _students.value ?: mutableListOf()
            currentList.add(student)
            _students.value = currentList
        }
    }
    
    fun updateStudent(updatedStudent: Student) {
        if (repository.updateStudent(updatedStudent)) {
            val currentList = _students.value ?: mutableListOf()
            val index = currentList.indexOfFirst { it.id == updatedStudent.id }
            if (index != -1) {
                currentList[index] = updatedStudent
                _students.value = currentList
            }
        }
    }
    
    fun deleteStudent(student: Student) {
        if (repository.deleteStudent(student.id)) {
            val currentList = _students.value ?: mutableListOf()
            currentList.remove(student)
            _students.value = currentList
        }
    }
    
    fun selectStudent(student: Student) {
        _selectedStudent.value = student
    }
    
    fun clearSelectedStudent() {
        _selectedStudent.value = null
    }
}
