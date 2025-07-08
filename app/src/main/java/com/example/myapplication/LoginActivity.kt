package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signupButton)

        loginButton.setOnClickListener {
            showLoginDialog()
        }

        signUpButton.setOnClickListener {
            showSignUpDialog()
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
                val id = editId.text.toString()
                val pw = editPw.text.toString()

                val success = UserManager.login(this, id, pw)
                if (success) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainAppActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "아이디 또는 비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showSignUpDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_signup, null)
        val editId = dialogView.findViewById<EditText>(R.id.editId)
        val editPw = dialogView.findViewById<EditText>(R.id.editPw)
        val editName = dialogView.findViewById<EditText>(R.id.editName)
        val editAddress = dialogView.findViewById<EditText>(R.id.editAddress)
        val editEmail = dialogView.findViewById<EditText>(R.id.editEmail)
        val editPhone = dialogView.findViewById<EditText>(R.id.editPhone)

        AlertDialog.Builder(this)
            .setTitle("회원가입")
            .setView(dialogView)
            .setPositiveButton("가입") { _, _ ->
                val id = editId.text.toString()
                val pw = editPw.text.toString()
                val name = editName.text.toString()
                val address = editAddress.text.toString()
                val email = editEmail.text.toString()
                val phone = editPhone.text.toString()

                val newUser = UserAccount().apply {
                    this.id = id
                    this.password = pw
                    this.name = name
                    this.address = address
                    this.email = email
                    this.phoneNumber = phone
                }

                val success = UserManager.signUp(this, newUser)
                if (success) {
                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

}
