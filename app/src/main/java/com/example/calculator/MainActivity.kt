package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var firstNumber = ""
    private var secondNumber = ""
    private var operator = ""
    private var result = ""
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        // الأزرار الرقمية
        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnDot
        )

        numberButtons.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                val text = it.text.toString()
                appendNumber(text)
            }
        }

        // أزرار العمليات
        findViewById<Button>(R.id.btnAdd).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { setOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperator("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperator("÷") }

        // أزرار أخرى
        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculate() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { deleteLast() }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { percent() }
        findViewById<Button>(R.id.btnSquare).setOnClickListener { square() }
        findViewById<Button>(R.id.btnSqrt).setOnClickListener { sqrt() }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { negate() }
    }

    private fun appendNumber(num: String) {
        if (isNewOperation) {
            display.text = ""
            isNewOperation = false
        }

        if (num == ".") {
            val current = display.text.toString()
            if (current.contains(".")) return
            if (current.isEmpty()) {
                display.text = "0."
                return
            }
        }

        display.text = display.text.toString() + num
    }

    private fun setOperator(op: String) {
        if (operator.isNotEmpty() && !isNewOperation) {
            calculate()
        }
        firstNumber = display.text.toString()
        operator = op
        isNewOperation = true
    }

    private fun calculate() {
        if (operator.isEmpty() || isNewOperation) return

        val second = display.text.toString()
        if (firstNumber.isEmpty() || second.isEmpty()) return

        val num1 = firstNumber.toDoubleOrNull() ?: return
        val num2 = second.toDoubleOrNull() ?: return

        when (operator) {
            "+" -> result = (num1 + num2).toString()
            "-" -> result = (num1 - num2).toString()
            "×" -> result = (num1 * num2).toString()
            "÷" -> {
                if (num2 == 0.0) {
                    display.text = "Error"
                    isNewOperation = true
                    return
                }
                result = (num1 / num2).toString()
            }
        }

        display.text = formatResult(result)
        firstNumber = display.text.toString()
        operator = ""
        isNewOperation = true
    }

    private fun clearAll() {
        display.text = "0"
        firstNumber = ""
        secondNumber = ""
        operator = ""
        result = ""
        isNewOperation = true
    }

    private fun deleteLast() {
        val text = display.text.toString()
        if (text.length <= 1) {
            display.text = "0"
        } else {
            display.text = text.dropLast(1)
        }
    }

    private fun percent() {
        val current = display.text.toString().toDoubleOrNull()
        if (current != null) {
            display.text = (current / 100).toString()
        }
    }

    private fun square() {
        val current = display.text.toString().toDoubleOrNull()
        if (current != null) {
            display.text = (current * current).toString()
        }
    }

    private fun sqrt() {
        val current = display.text.toString().toDoubleOrNull()
        if (current != null && current >= 0) {
            display.text = Math.sqrt(current).toString()
        } else if (current != null && current < 0) {
            display.text = "Error"
        }
    }

    private fun negate() {
        val current = display.text.toString()
        if (current.startsWith("-")) {
            display.text = current.drop(1)
        } else if (current != "0") {
            display.text = "-" + current
        }
    }

    private fun formatResult(result: String): String {
        return try {
            val value = result.toDouble()
            if (value == value.toLong().toDouble()) {
                value.toLong().toString()
            } else {
                result
            }
        } catch (e: Exception) {
            result
        }
    }
}
