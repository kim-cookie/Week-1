package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            showLoginDialog()
        }
    }

    private fun showLoginDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_login, null)
        val editId = dialogView.findViewById<EditText>(R.id.editId)
        val editPw = dialogView.findViewById<EditText>(R.id.editPw)

        AlertDialog.Builder(this)
            .setTitle("로그인")
            .setView(dialogView)
            .setPositiveButton("확인") { _, _ ->
                val userId = editId.text.toString()
                val password = editPw.text.toString()

                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

            }
            .setNegativeButton("취소", null)
            .show()
    }
}
