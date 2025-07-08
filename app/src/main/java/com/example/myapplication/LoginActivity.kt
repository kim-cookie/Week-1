package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Compose UI만 사용할 것이므로 XML 제거
        setContent {
            LoginScreen(
                onLoginClick = { showLoginDialog() },
                onSignUpClick = { showSignUpDialog() }
            )
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

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "앱 로고",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 40.dp)
        )

        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED6B27))
        ) {
            Text(text = "로그인", color = Color.White)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onSignUpClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED6B27))
        ) {
            Text(text = "회원가입", color = Color.White)
        }
    }
}
