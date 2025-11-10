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

        // Xử lý khi chọn radio button ở hàng 1
        rgRow1.setOnCheckedChangeListener { _, checkedId ->
            if (suppressChange) return@setOnCheckedChangeListener
            if (checkedId != -1) {
                suppressChange = true
                rgRow2.clearCheck() // Khi chọn ở hàng 1 thì bỏ chọn ở hàng 2
                suppressChange = false
                updateList()
            }
        }

        // Xử lý khi chọn radio button ở hàng 2
        rgRow2.setOnCheckedChangeListener { _, checkedId ->
            if (suppressChange) return@setOnCheckedChangeListener
            if (checkedId != -1) {
                suppressChange = true
                rgRow1.clearCheck() // Khi chọn ở hàng 2 thì bỏ chọn ở hàng 1
                suppressChange = false
                updateList()
            }
        }

        // Khi người dùng nhập số thì tự động cập nhật danh sách
        etNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = updateList()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    //Hàm chính để cập nhật danh sách kết quả theo loại số và giá trị nhập
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

        // Duyệt tất cả các số nhỏ hơn n và lọc theo loại được chọn
        val numbers = mutableListOf<Int>()
        for (i in 1 until n) {
            when (selectedId) {
                R.id.rbOdd -> if (i % 2 != 0) numbers.add(i) // Số lẻ
                R.id.rbEven -> if (i % 2 == 0) numbers.add(i) // Số chẵn
                R.id.rbPrime -> if (isPrime(i)) numbers.add(i) // Số nguyên tố
                R.id.rbPerfect -> if (isPerfect(i)) numbers.add(i) // Số hoàn hảo
                R.id.rbSquare -> if (isSquare(i)) numbers.add(i) // Số chính phương
                R.id.rbFibo -> if (isFibonacci(i)) numbers.add(i) // Số Fibonacci
            }
        }

        // Nếu không có số nào phù hợp thì hiển thị thông báo
        if (numbers.isEmpty()) {
            showMessage("Không có số nào thỏa mãn")
        } else {
            tvMessage.visibility = TextView.GONE // Ẩn TextView thông báo
            adapter.clear()
            adapter.addAll(numbers) //Hiển thị danh sách mới
        }
    }

    //Lấy ID của RadioButton được chọn giữa 2 hàng
    private fun getSelectedRadioButtonId(): Int {
        return if (rgRow1.checkedRadioButtonId != -1) rgRow1.checkedRadioButtonId
        else rgRow2.checkedRadioButtonId
    }

    // Hiển thị thông báo thay vì danh sách
    private fun showMessage(msg: String) {
        adapter.clear()
        tvMessage.text = msg
        tvMessage.visibility = TextView.VISIBLE
    }

    //Các hàm kiểm tra số

    // Kiểm tra số nguyên tố
    private fun isPrime(n: Int): Boolean {
        if (n < 2) return false
        for (i in 2..sqrt(n.toDouble()).toInt()) if (n % i == 0) return false
        return true
    }

    //Kiểm tra số hoàn hảo
    private fun isPerfect(n: Int): Boolean {
        if (n < 2) return false
        var sum = 1
        for (i in 2..n / 2) if (n % i == 0) sum += i
        return sum == n
    }

    //Kiểm tra số chính phương
    private fun isSquare(n: Int): Boolean {
        val root = sqrt(n.toDouble()).toInt()
        return root * root == n
    }

    // Kiểm tra số Fibonacci
    private fun isFibonacci(n: Int): Boolean {
        val test1 = 5 * n * n + 4
        val test2 = 5 * n * n - 4
        return isSquare(test1) || isSquare(test2)
    }
}
