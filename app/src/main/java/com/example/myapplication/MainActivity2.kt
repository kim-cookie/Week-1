package com.example.myapplication

import android.os.Bundle
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        val selectedCategory = intent.getStringExtra("category")

        val outerwearList = arrayListOf(
            Product("사과", "₩1,000", R.drawable.apple)
        )
        val topsList = arrayListOf(
            Product("사과", "₩1,000", R.drawable.apple)
        )
        val pantsList = arrayListOf(
            Product("사과", "₩1,000", R.drawable.apple)
        )
        val skirtsList = arrayListOf(
            Product("사과", "₩1,000", R.drawable.apple)
        )
        val dressesList = arrayListOf(
            Product("사과", "₩1,000", R.drawable.apple)
        )
        val socksList = arrayListOf(
            Product("사과", "₩1,000", R.drawable.apple)
        )
        val hatsList = arrayListOf(
            Product("사과", "₩1,000", R.drawable.apple)
        )
        val shoesList = arrayListOf(
            Product("사과", "₩1,000", R.drawable.apple)
        )
        val accessoriesList = arrayListOf(
            Product("사과", "₩1,000", R.drawable.apple),
            Product("오렌지", "₩1,400", R.drawable.orange),
            Product("바지", "₩1,400", R.drawable.pants)
        )

        val productList = when (selectedCategory) {
            "아우터" -> outerwearList
            "상의" -> topsList
            "바지" -> pantsList
            "치마" -> skirtsList
            "원피스" -> dressesList
            "양말" -> socksList
            "모자" -> hatsList
            "신발" -> shoesList
            "잡화" -> accessoriesList
            else -> arrayListOf()
        }

        val gridView = findViewById<GridView>(R.id.gridView)
        gridView.adapter = ProductAdapter(this, productList)

        

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}