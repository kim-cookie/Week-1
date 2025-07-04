package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)

        val cities = arrayListOf(
            "아우터", "상의", "바지", "치마", "원피스", "양말", "모자", "신발", "잡화"
        )

        val citiesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cities)
        val myListView = findViewById<ListView>(R.id.myListView)
        myListView.adapter = citiesAdapter

        myListView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = cities[position]
            if (selectedItem == "아우터") {
                val intent = Intent(this, OuterActivity::class.java)
                startActivity(intent)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // 현재 ListActivity이므로 아무것도 하지 않음
                    true
                }
                R.id.nav_cart -> {
                    // 새로운 CartActivity로 전환
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
