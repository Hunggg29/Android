package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {


    private var operand1: Int? = null
    private var pendingOperation: String? = null
    private var isNewOperation = true

    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bai1)

        tvResult = findViewById(R.id.tvResult)

        // Thiết lập sự kiện click cho các nút số (0-9)
        val numberButtons = listOf<Button>(
            findViewById(R.id.button0), findViewById(R.id.button1), findViewById(R.id.button2),
            findViewById(R.id.button3), findViewById(R.id.button4), findViewById(R.id.button5),
            findViewById(R.id.button6), findViewById(R.id.button7), findViewById(R.id.button8),
            findViewById(R.id.button9)
        )
        numberButtons.forEach { button ->
            button.setOnClickListener { onNumberClick(button.text.toString()) }
        }

        // Thiết lập sự kiện click cho các nút phép toán (+, -, x, /)
        val operationButtons = mapOf(
            R.id.buttonPlus to "+",
            R.id.buttonMinus to "-",
            R.id.buttonMultiply to "*",
            R.id.buttonDivide to "/"
        )
        operationButtons.forEach { (id, opString) ->
            findViewById<Button>(id).setOnClickListener { onOperationClick(opString) }
        }

        // Thiết lập sự kiện click cho các nút chức năng
        findViewById<Button>(R.id.buttonEquals).setOnClickListener { onEqualsClick() }
        findViewById<Button>(R.id.buttonC).setOnClickListener { onCClick() }
        findViewById<Button>(R.id.buttonCE).setOnClickListener { onCEClick() }
        findViewById<Button>(R.id.buttonBS).setOnClickListener { onBSClick() }

        // Đặt lại máy tính về trạng thái ban đầu khi khởi động
        onCClick()
    }

    /**
     * Xử lý khi người dùng nhấn nút số.
     */
    private fun onNumberClick(number: String) {
        if (tvResult.text.toString() == "Error") {
            onCClick() // Nếu đang lỗi, nhấn số sẽ reset lại
        }

        if (isNewOperation || tvResult.text == "0") {
            tvResult.text = number
            isNewOperation = false
        } else {
            tvResult.append(number)
        }
    }

    /**
     * Xử lý khi người dùng nhấn nút phép toán (+, -, *, /).
     */
    private fun onOperationClick(operation: String) {
        if (tvResult.text.toString() == "Error") return // Không làm gì khi đang lỗi

        val currentNumber = tvResult.text.toString().toIntOrNull()
        if (currentNumber != null) {
            // Nếu đã có phép toán đang chờ (ví dụ: 5 * 2 + ), tính 5*2 trước
            if (pendingOperation != null && !isNewOperation) {
                operand1 = performCalculation(operand1!!, currentNumber, pendingOperation!!)
                tvResult.text = operand1.toString()
            } else {
                operand1 = currentNumber
            }
            pendingOperation = operation
            isNewOperation = true
        }
    }

    /**
     * Xử lý khi người dùng nhấn nút bằng (=).
     */
    private fun onEqualsClick() {
        if (tvResult.text.toString() == "Error") return

        val operand2 = tvResult.text.toString().toIntOrNull()
        if (operand1 != null && pendingOperation != null && operand2 != null) {
            try {
                val result = performCalculation(operand1!!, operand2, pendingOperation!!)
                tvResult.text = result.toString()
            } catch (e: ArithmeticException) {
                tvResult.text = "Error"
            }
        }
        isNewOperation = true
        pendingOperation = null
        operand1 = null
    }

    /**
     * Thực hiện phép tính.
     */
    private fun performCalculation(op1: Int, op2: Int, operation: String): Int {
        return when (operation) {
            "+" -> op1 + op2
            "-" -> op1 - op2
            "*" -> op1 * op2
            "/" -> {
                if (op2 == 0) throw ArithmeticException("Division by zero")
                op1 / op2
            }
            else -> 0
        }
    }

    /**
     * Nút C: Xóa toàn bộ phép toán, nhập lại từ đầu.
     */
    private fun onCClick() {
        tvResult.text = "0"
        operand1 = null
        pendingOperation = null
        isNewOperation = true
    }

    /**
     * Nút CE: Xóa giá trị toán hạng hiện tại về 0.
     */
    private fun onCEClick() {
        tvResult.text = "0"
        if (isNewOperation) {
            operand1 = null
            pendingOperation = null
        }
    }

    /**
     * Nút BS: Xóa chữ số hàng đơn vị của toán hạng hiện tại.
     */
    private fun onBSClick() {
        val currentText = tvResult.text.toString()
        if (isNewOperation || currentText == "Error") {
            return
        }

        if (currentText.length > 1) {
            tvResult.text = currentText.substring(0, currentText.length - 1)
        } else if (currentText.length == 1 && currentText != "0") {
            tvResult.text = "0"
        }
    }
}
