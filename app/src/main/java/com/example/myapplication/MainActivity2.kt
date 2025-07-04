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

        val productList = arrayListOf(
            Product("사과", "₩1,000", R.drawable.apple),
            Product("바나나", "₩1,200", R.drawable.banana),
            Product("오렌지", "₩1,400", R.drawable.orange),
            Product("사과", "₩1,000", R.drawable.apple),
            Product("바나나", "₩1,200", R.drawable.banana),
            Product("오렌지", "₩1,400", R.drawable.orange2),
            Product("사과", "₩1,000", R.drawable.apple),
            Product("바나나", "₩1,200", R.drawable.banana),
            Product("오렌지", "₩1,400", R.drawable.orange2),
            Product("사과", "₩1,000", R.drawable.apple),
            Product("바나나", "₩1,200", R.drawable.banana),
            Product("오렌지", "₩1,400", R.drawable.orange),
            Product("바지", "₩1,400", R.drawable.pants)
            )

        val gridView = findViewById<GridView>(R.id.gridView)
        gridView.adapter = ProductAdapter(this, productList)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}