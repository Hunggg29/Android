package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.StudentAdapter
import com.example.myapplication.databinding.FragmentStudentListBinding
import com.example.myapplication.viewmodel.StudentViewModel

class StudentListFragment : Fragment() {
    
    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: StudentViewModel
    private lateinit var adapter: StudentAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[StudentViewModel::class.java]
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeViewModel()
        setupFab()
    }
    
    private fun setupRecyclerView() {
        adapter = StudentAdapter(
            students = emptyList(),
            onItemClick = { student ->
                viewModel.selectStudent(student)
                findNavController().navigate(R.id.action_studentListFragment_to_updateStudentFragment)
            },
            onDeleteClick = { student ->
                viewModel.deleteStudent(student)
            }
        )
        
        binding.recyclerViewStudents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@StudentListFragment.adapter
        }
    }
    
    private fun observeViewModel() {
        viewModel.students.observe(viewLifecycleOwner) { students ->
            adapter.updateStudents(students)
        }
    }
    
    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_studentListFragment_to_addStudentFragment)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
