package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val cities = arrayListOf(
            "아우터", "상의", "바지", "치마", "원피스", "양말", "모자", "신발", "잡화"
        )

        val citiesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cities)
        val myListView = findViewById<ListView>(R.id.myListView)
        myListView.adapter = citiesAdapter

        myListView.setOnItemClickListener { parent, view, position, id ->
            val clickedCategory = parent.getItemAtPosition(position).toString()

            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("category", clickedCategory)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
