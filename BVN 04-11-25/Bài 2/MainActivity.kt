package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var etNumber: EditText
    private lateinit var rgRow1: RadioGroup
    private lateinit var rgRow2: RadioGroup
    private lateinit var tvMessage: TextView
    private lateinit var lvNumbers: ListView
    private lateinit var adapter: ArrayAdapter<Int>

    private var suppressChange = false //  kiểm soát sự kiện clearCheck

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)

        etNumber = findViewById(R.id.etNumber)
        rgRow1 = findViewById(R.id.rgRow1)
        rgRow2 = findViewById(R.id.rgRow2)
        tvMessage = findViewById(R.id.tvMessage)
        lvNumbers = findViewById(R.id.lvNumbers)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        lvNumbers.adapter = adapter

        // Sự kiện chọn ở hàng 1
        rgRow1.setOnCheckedChangeListener { _, checkedId ->
            if (suppressChange) return@setOnCheckedChangeListener
            if (checkedId != -1) {
                suppressChange = true
                rgRow2.clearCheck() // Clear hàng 2 mà không kích hoạt sự kiện
                suppressChange = false
                updateList()
            }
        }

        // Sự kiện chọn ở hàng 2
        rgRow2.setOnCheckedChangeListener { _, checkedId ->
            if (suppressChange) return@setOnCheckedChangeListener
            if (checkedId != -1) {
                suppressChange = true
                rgRow1.clearCheck() // Clear hàng 1 mà không kích hoạt sự kiện
                suppressChange = false
                updateList()
            }
        }

        //khi người dùng nhập số
        etNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = updateList()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateList() {
        val input = etNumber.text.toString()
        if (input.isEmpty()) {
            showMessage("Nhập một số nguyên để xem kết quả")
            return
        }

        val n = input.toIntOrNull()
        if (n == null || n <= 0) {
            showMessage("Giá trị không hợp lệ")
            return
        }

        val selectedId = getSelectedRadioButtonId()
        if (selectedId == -1) {
            showMessage("Vui lòng chọn loại số cần hiển thị")
            return
        }

        val numbers = mutableListOf<Int>()
        for (i in 1 until n) {
            when (selectedId) {
                R.id.rbOdd -> if (i % 2 != 0) numbers.add(i)
                R.id.rbEven -> if (i % 2 == 0) numbers.add(i)
                R.id.rbPrime -> if (isPrime(i)) numbers.add(i)
                R.id.rbPerfect -> if (isPerfect(i)) numbers.add(i)
                R.id.rbSquare -> if (isSquare(i)) numbers.add(i)
                R.id.rbFibo -> if (isFibonacci(i)) numbers.add(i)
            }
        }

        if (numbers.isEmpty()) {
            showMessage("Không có số nào thỏa mãn")
        } else {
            tvMessage.visibility = TextView.GONE
            adapter.clear()
            adapter.addAll(numbers)
        }
    }

    private fun getSelectedRadioButtonId(): Int {
        return if (rgRow1.checkedRadioButtonId != -1) rgRow1.checkedRadioButtonId
        else rgRow2.checkedRadioButtonId
    }

    private fun showMessage(msg: String) {
        adapter.clear()
        tvMessage.text = msg
        tvMessage.visibility = TextView.VISIBLE
    }

    //Các hàm kiểm tra
    private fun isPrime(n: Int): Boolean {
        if (n < 2) return false
        for (i in 2..sqrt(n.toDouble()).toInt()) if (n % i == 0) return false
        return true
    }

    private fun isPerfect(n: Int): Boolean {
        if (n < 2) return false
        var sum = 1
        for (i in 2..n / 2) if (n % i == 0) sum += i
        return sum == n
    }

    private fun isSquare(n: Int): Boolean {
        val root = sqrt(n.toDouble()).toInt()
        return root * root == n
    }

    private fun isFibonacci(n: Int): Boolean {
        val test1 = 5 * n * n + 4
        val test2 = 5 * n * n - 4
        return isSquare(test1) || isSquare(test2)
    }
}
