package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Student

class StudentAdapter(
    private var students: List<Student>,
    private val onItemClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvStudentName)
        val tvStudentId: TextView = itemView.findViewById(R.id.tvStudentId)
        val tvMajor: TextView = itemView.findViewById(R.id.tvMajor)
        val tvGpa: TextView = itemView.findViewById(R.id.tvGpa)
        val btnDelete: View = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.tvName.text = student.name
        holder.tvStudentId.text = "MSSV: ${student.studentId}"
        holder.tvMajor.text = "Ngành: ${student.major}"
        holder.tvGpa.text = "GPA: ${student.gpa}"
        
        holder.itemView.setOnClickListener {
            onItemClick(student)
        }
        
        holder.btnDelete.setOnClickListener {
            onDeleteClick(student)
        }
    }

    override fun getItemCount() = students.size

    fun updateStudents(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }
}
