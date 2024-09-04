package com.amok.bankapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Greetings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isFirstRun()) {
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_greetings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val enterButton: AppCompatButton = findViewById(R.id.enter_button)
        enterButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.login).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            validateAndLogin(username, password)
        }
    }

    private fun isFirstRun(): Boolean {
        val sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
            showAlertWithChoices(
                "Приложение запущено впервые",
                "Хотите зарегистрироваться или остаться на этом экране?",
                "Регистрация",
                "Остаться",
                { goToRegistration() },
                { /* пояснения */ }
            )
            return true
        }
        return false
    }

    private fun validateAndLogin(username: String, password: String) {
        val sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE)

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Логин и пароль не могут быть пустыми.")
            return
        }

        if (password.length < 8) {
            showAlert("Ошибка", "Пароль должен содержать не менее 8 символов.")
            return
        }

        if (!sharedPreferences.contains(username)) {
            showAlertWithChoices(
                "Логин не найден",
                "Такого логина не существует. Хотите зарегистрироваться?",
                "Регистрация",
                "Попробовать снова",
                { goToRegistration() },
                { /* Ничего не делаем, остаёмся на экране */ }
            )
            return
        }

        val storedPassword = sharedPreferences.getString(username, "")
        if (storedPassword == password) {
            Toast.makeText(this, "Вход выполнен успешно", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,calculate::class.java))
            finish()
        } else {
            showAlert("Ошибка", "Неверный пароль.")
        }
    }

    private fun showAlertWithChoices(
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        positiveAction: () -> Unit,
        negativeAction: () -> Unit
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButtonText) { _, _ -> positiveAction() }
        builder.setNegativeButton(negativeButtonText) { _, _ -> negativeAction() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun goToRegistration() {
        startActivity(Intent(this, reg::class.java))
        finish()
    }
}
