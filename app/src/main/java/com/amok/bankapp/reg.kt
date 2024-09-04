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

class reg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reg)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val enterButton: AppCompatButton = findViewById(R.id.reg_button)
        enterButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.login).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            validateAndReg(username, password)
        }
    }

    private fun validateAndReg(username: String, password: String) {
        val sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE)

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Логин и пароль не могут быть пустыми.")
            return
        }

        if (password.length < 8) {
            showAlert("Ошибка", "Пароль должен содержать не менее 8 символов.")
            return
        }

        if (sharedPreferences.contains(username)){
            showAlert("Ошибка","Логин уже существует.")
        }else{
            showAlert("Успех", "Вы успешно зарегистрировались.")
            register(username,password,sharedPreferences)
            goToEnter()
        }
    }

    private fun register(username: String,password: String, sharedPreferences: SharedPreferences){
        val editor = sharedPreferences.edit()
        editor.putString(username,password)
        editor.apply()
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun goToEnter() {
        startActivity(Intent(this, Greetings::class.java))
        finish()
    }
}