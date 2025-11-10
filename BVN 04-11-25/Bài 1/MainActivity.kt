package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var etFromAmount: EditText
    private lateinit var etToAmount: EditText

    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.92,
        "VND" to 24500.0,
        "JPY" to 151.0,
        "GBP" to 0.79,
        "AUD" to 1.54,
        "CAD" to 1.37,
        "CHF" to 0.89,
        "KRW" to 1350.0,
        "SGD" to 1.36
    )

    private var isEditing = false // tránh vòng lặp sự kiện

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        etFromAmount = findViewById(R.id.etFromAmount)
        etToAmount = findViewById(R.id.etToAmount)

        val currencies = exchangeRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        spinnerFrom.setSelection(0)
        spinnerTo.setSelection(2) // để loại tiền mặc định: USD -> VND

        // Theo dõi thay đổi EditText đầu vào
        etFromAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isEditing) {
                    convertCurrency(true)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Khi người dùng đổi loại tiền
        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                convertCurrency(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                convertCurrency(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun convertCurrency(fromToTo: Boolean) {
        if (isEditing) return

        try {
            isEditing = true
            val fromCurrency = spinnerFrom.selectedItem.toString()
            val toCurrency = spinnerTo.selectedItem.toString()
            val fromRate = exchangeRates[fromCurrency] ?: 1.0
            val toRate = exchangeRates[toCurrency] ?: 1.0

            val amountText = etFromAmount.text.toString()
            val amount = amountText.toDoubleOrNull() ?: 0.0

            val result = amount * (toRate / fromRate)
            etToAmount.setText(String.format("%.2f", result))
        } finally {
            isEditing = false
        }
    }
}
