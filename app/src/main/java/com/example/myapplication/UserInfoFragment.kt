package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Date
import java.util.Locale

class UserInfoFragment : Fragment() {

    private fun formatDateTime(): String {
        val now = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return now.format(Date())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)

        val idText = view.findViewById<TextView>(R.id.idText)
        val nameText = view.findViewById<TextView>(R.id.nameText)
        val addressText = view.findViewById<TextView>(R.id.addressText)
        val signUpDateText = view.findViewById<TextView>(R.id.signUpDateText)
        val lastLoginDateText = view.findViewById<TextView>(R.id.lastLoginDateText)
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        val emailText = view.findViewById<TextView>(R.id.emailText)
        val phoneText = view.findViewById<TextView>(R.id.phoneText)

        val user = UserManager.getLoggedInUser(requireContext())

        if (user != null) {
            idText.text = "아이디: ${user.id}"
            nameText.text = "이름: ${user.name}"
            addressText.text = "주소: ${user.address}"
            signUpDateText.text = "회원등록일: ${user.signUpDate}"
            lastLoginDateText.text = "최근 로그인: ${user.lastLoginDate}"
            emailText.text = "이메일: ${user.email}"
            phoneText.text = "전화번호: ${user.phoneNumber}"
        }

        logoutButton.setOnClickListener {
            UserManager.logout(requireContext())
            Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}
