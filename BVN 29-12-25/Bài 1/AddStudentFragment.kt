package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentAddStudentBinding
import com.example.myapplication.model.Student
import com.example.myapplication.viewmodel.StudentViewModel
import java.util.UUID

class AddStudentFragment : Fragment() {
    
    private var _binding: FragmentAddStudentBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: StudentViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[StudentViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.btnSave.setOnClickListener {
            saveStudent()
        }
        
        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    
    private fun saveStudent() {
        val name = binding.etName.text.toString().trim()
        val studentId = binding.etStudentId.text.toString().trim()
        val major = binding.etMajor.text.toString().trim()
        val gpaStr = binding.etGpa.text.toString().trim()
        
        if (name.isEmpty() || studentId.isEmpty() || major.isEmpty() || gpaStr.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }
        
        val gpa = gpaStr.toDoubleOrNull()
        if (gpa == null || gpa < 0.0 || gpa > 4.0) {
            Toast.makeText(requireContext(), "GPA phải từ 0.0 đến 4.0", Toast.LENGTH_SHORT).show()
            return
        }
        
        val student = Student(
            id = UUID.randomUUID().toString(),
            name = name,
            studentId = studentId,
            major = major,
            gpa = gpa
        )
        
        viewModel.addStudent(student)
        Toast.makeText(requireContext(), "Đã thêm sinh viên thành công", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
