package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.view.View

class MainActivity : AppCompatActivity() {
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var etBirthday: EditText
    private lateinit var btnSelect: Button
    private lateinit var calendarView: CalendarView
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnRegister: Button
    private lateinit var calendarContainer: android.widget.FrameLayout
    private lateinit var scrollView: ScrollView
    private var isCalendarVisible = false
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bai2)

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        etBirthday = findViewById(R.id.etBirthday)
        btnSelect = findViewById(R.id.btnSelect)
        calendarView = findViewById(R.id.calendarView)
        etAddress = findViewById(R.id.etAddress)
        etEmail = findViewById(R.id.etEmail)
        cbTerms = findViewById(R.id.cbTerms)
        btnRegister = findViewById(R.id.btnRegister)
        calendarContainer = findViewById(R.id.calendarContainer)
        scrollView = findViewById(R.id.scrollView)

        // Ẩn CalendarView ban đầu
        calendarContainer.visibility = View.GONE
        // Set ngày mặc định cho CalendarView
        calendarView.date = System.currentTimeMillis()

        // Ẩn/hiện CalendarView khi bấm Select
        btnSelect.setOnClickListener {
            toggleCalendar()
        }

        // Xử lý khi chọn ngày trên CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val selectedDate = dateFormat.format(calendar.time)
            etBirthday.setText(selectedDate)
            // Ẩn CalendarView sau khi chọn ngày
            toggleCalendar()
        }

        // Khi nhấn Register
        btnRegister.setOnClickListener {
            validateForm()
        }
    }

    private fun toggleCalendar() {
        isCalendarVisible = !isCalendarVisible
        if (isCalendarVisible) {
            calendarContainer.visibility = View.VISIBLE
            // Force invalidate để đảm bảo CalendarView được render
            calendarContainer.invalidate()
            calendarContainer.requestLayout()
            // Đợi layout render xong rồi mới scroll đến CalendarView
            scrollView.postDelayed({
                // Scroll để CalendarView hiển thị trong viewport
                val scrollY = scrollView.scrollY
                val calendarTop = calendarContainer.top
                val targetScroll = scrollY + calendarTop - scrollView.paddingTop
                scrollView.smoothScrollTo(0, targetScroll)
            }, 150)
        } else {
            calendarContainer.visibility = View.GONE
        }
    }

    private fun validateForm() {
        var valid = true
        val defaultColor = Color.WHITE

        // Reset
        etFirstName.setBackgroundColor(defaultColor)
        etLastName.setBackgroundColor(defaultColor)
        etBirthday.setBackgroundColor(defaultColor)
        etAddress.setBackgroundColor(defaultColor)
        etEmail.setBackgroundColor(defaultColor)
        cbTerms.setBackgroundColor(Color.TRANSPARENT)

        if (etFirstName.text.isNullOrEmpty()) {
            etFirstName.setBackgroundColor(Color.parseColor("#FFCDD2"))
            valid = false
        }
        if (etLastName.text.isNullOrEmpty()) {
            etLastName.setBackgroundColor(Color.parseColor("#FFCDD2"))
            valid = false
        }
        if (!rbMale.isChecked && !rbFemale.isChecked) {
            Toast.makeText(this, "Hãy chọn giới tính", Toast.LENGTH_SHORT).show()
            valid = false
        }
        if (etBirthday.text.isNullOrEmpty()) {
            etBirthday.setBackgroundColor(Color.parseColor("#FFCDD2"))
            valid = false
        }
        if (etAddress.text.isNullOrEmpty()) {
            etAddress.setBackgroundColor(Color.parseColor("#FFCDD2"))
            valid = false
        }
        if (etEmail.text.isNullOrEmpty()) {
            etEmail.setBackgroundColor(Color.parseColor("#FFCDD2"))
            valid = false
        }
        if (!cbTerms.isChecked) {
            cbTerms.setBackgroundColor(Color.parseColor("#FFCDD2"))
            valid = false
        }

        if (valid) {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Hãy điền vào tất cả các trường", Toast.LENGTH_SHORT).show()
        }
    }
}
