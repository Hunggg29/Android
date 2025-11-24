package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etMSSV: EditText
    private lateinit var etHoTen: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var lvSinhVien: ListView

    private lateinit var adapter: StudentAdapter
    private val dsSinhVien = mutableListOf<Pair<String, String>>()

    // Lưu vị trí của item đang được chọn để update
    private var selectedIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bai1)

        etMSSV = findViewById(R.id.etMSSV)
        etHoTen = findViewById(R.id.etHoTen)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        lvSinhVien = findViewById(R.id.lvSinhVien)

        // adapter có callback để xóa sinh viên
        adapter = StudentAdapter(this, dsSinhVien) { index ->
            dsSinhVien.removeAt(index)
            adapter.notifyDataSetChanged()
        }

        lvSinhVien.adapter = adapter

        // Add sinh viên
        btnAdd.setOnClickListener {
            val mssv = etMSSV.text.toString().trim()
            val hoTen = etHoTen.text.toString().trim()

            if (mssv.isEmpty() || hoTen.isEmpty()) {
                Toast.makeText(this, "Nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dsSinhVien.add(Pair(mssv, hoTen))
            adapter.notifyDataSetChanged()
            etMSSV.text.clear()
            etHoTen.text.clear()
        }

        // Chọn sinh viên để sửa
        lvSinhVien.setOnItemClickListener { _, _, position, _ ->
            selectedIndex = position
            val sv = dsSinhVien[position]
            etMSSV.setText(sv.first)
            etHoTen.setText(sv.second)
        }

        // Update sinh viên
        btnUpdate.setOnClickListener {
            if (selectedIndex == -1) {
                Toast.makeText(this, "Chọn sinh viên cần sửa!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hoTen = etHoTen.text.toString().trim()
            if (hoTen.isEmpty()) {
                Toast.makeText(this, "Họ tên rỗng!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mssv = dsSinhVien[selectedIndex].first
            dsSinhVien[selectedIndex] = Pair(mssv, hoTen)
            adapter.notifyDataSetChanged()
        }
    }
}
