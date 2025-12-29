package com.example.myapplication.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.model.Student

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentDatabase.db"
        private const val DATABASE_VERSION = 1
        
        // Table name
        private const val TABLE_STUDENTS = "students"
        
        // Column names
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_STUDENT_ID = "student_id"
        private const val COLUMN_MAJOR = "major"
        private const val COLUMN_GPA = "gpa"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_STUDENTS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_STUDENT_ID TEXT NOT NULL,
                $COLUMN_MAJOR TEXT NOT NULL,
                $COLUMN_GPA REAL NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTable)
        
        // Insert sample data
        insertSampleData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }
    
    private fun insertSampleData(db: SQLiteDatabase?) {
        val students = listOf(
            Student(
                id = java.util.UUID.randomUUID().toString(),
                name = "Nguyễn Văn A",
                studentId = "SV001",
                major = "Công nghệ thông tin",
                gpa = 3.5
            ),
            Student(
                id = java.util.UUID.randomUUID().toString(),
                name = "Trần Thị B",
                studentId = "SV002",
                major = "Kỹ thuật phần mềm",
                gpa = 3.8
            )
        )
        
        students.forEach { student ->
            val values = ContentValues().apply {
                put(COLUMN_ID, student.id)
                put(COLUMN_NAME, student.name)
                put(COLUMN_STUDENT_ID, student.studentId)
                put(COLUMN_MAJOR, student.major)
                put(COLUMN_GPA, student.gpa)
            }
            db?.insert(TABLE_STUDENTS, null, values)
        }
    }

    // Insert student
    fun insertStudent(student: Student): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, student.id)
            put(COLUMN_NAME, student.name)
            put(COLUMN_STUDENT_ID, student.studentId)
            put(COLUMN_MAJOR, student.major)
            put(COLUMN_GPA, student.gpa)
        }
        return db.insert(TABLE_STUDENTS, null, values)
    }

    // Get all students
    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_STUDENTS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val student = Student(
                    id = getString(getColumnIndexOrThrow(COLUMN_ID)),
                    name = getString(getColumnIndexOrThrow(COLUMN_NAME)),
                    studentId = getString(getColumnIndexOrThrow(COLUMN_STUDENT_ID)),
                    major = getString(getColumnIndexOrThrow(COLUMN_MAJOR)),
                    gpa = getDouble(getColumnIndexOrThrow(COLUMN_GPA))
                )
                students.add(student)
            }
            close()
        }
        return students
    }

    // Update student
    fun updateStudent(student: Student): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_STUDENT_ID, student.studentId)
            put(COLUMN_MAJOR, student.major)
            put(COLUMN_GPA, student.gpa)
        }
        return db.update(
            TABLE_STUDENTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(student.id)
        )
    }

    // Delete student
    fun deleteStudent(studentId: String): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_STUDENTS,
            "$COLUMN_ID = ?",
            arrayOf(studentId)
        )
    }

    // Get student by ID
    fun getStudentById(studentId: String): Student? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_STUDENTS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(studentId),
            null,
            null,
            null
        )

        var student: Student? = null
        if (cursor.moveToFirst()) {
            student = Student(
                id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                studentId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID)),
                major = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MAJOR)),
                gpa = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GPA))
            )
        }
        cursor.close()
        return student
    }
    
    // Check if database is empty
    fun isDatabaseEmpty(): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_STUDENTS", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count == 0
    }
}
