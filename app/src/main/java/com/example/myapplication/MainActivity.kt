package com.example.myapplication

import android.widget.ArrayAdapter
import android.widget.ListView
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val cities = ArrayList<String>() // 도시 이름을 담을 리스트 생성

        // 도시 추가
        cities.add("seoul")
        cities.add("london")
        cities.add("busan")
        cities.add("tokyo")
        cities.add("osaka")
        cities.add("kyoto")
        cities.add("seattle")
        cities.add("LA")
        cities.add("moscow")
        val cities_Adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cities) // adapter 생성 (data와 view를 연결해 주는 관리자)
        val myListView = findViewById<ListView>(R.id.myListView)
        myListView.adapter = cities_Adapter // 어댑터 붙이기



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}