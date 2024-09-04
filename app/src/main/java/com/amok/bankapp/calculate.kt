package com.amok.bankapp

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class calculate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculate)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val seekBar: SeekBar = findViewById(R.id.seek_bar)
        val valueTextView: TextView = findViewById(R.id.seek_bar_value)
        val creditTimeEditText: EditText = findViewById(R.id.credit_time)
        val calculateButton: Button = findViewById(R.id.calculate_button)
        val monthlyPaymentTextView: TextView = findViewById(R.id.month_payment)
        val backButton: ImageButton = findViewById(R.id.back_button)

        valueTextView.text = seekBar.progress.toString()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                valueTextView.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        calculateButton.setOnClickListener {
            val loanAmount = seekBar.progress.toDouble()
            val creditTimeMonths = creditTimeEditText.text.toString().toIntOrNull()

            if (creditTimeMonths != null && creditTimeMonths > 0) {
                val monthlyPayment = calculateMonthlyPayment(loanAmount, creditTimeMonths)
                monthlyPaymentTextView.text = "Ежемесячный платеж: %.2f тыс. рублей".format(monthlyPayment / 1000)
            } else {
                monthlyPaymentTextView.text = "Введите корректный срок кредита"
            }
        }

        backButton.setOnClickListener {
            startActivity(Intent(this@calculate,Greetings::class.java))
            finish()
        }
    }

    private fun calculateMonthlyPayment(loanAmount: Double, months: Int): Double {
        return when {
            months <= 12 -> {
                loanAmount / months + loanAmount * 0.059
            }
            months <= 24 -> {
                val s1 = loanAmount / 12 + loanAmount * 0.059
                ((s1 + loanAmount) / months) + ((loanAmount - s1) * 0.051)
            }
            else -> {
                val s1 = loanAmount / 12 + loanAmount * 0.059
                ((s1 + loanAmount) / months) + ((loanAmount - s1) * 0.042)
            }
        }
    }
}