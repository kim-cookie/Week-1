package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class OuterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outer)

        val recyclerView = findViewById<RecyclerView>(R.id.outerRecyclerView)

//        val images = listOf(
//            R.drawable.outer/outer1, R.drawable.outer2, R.drawable.outer3, ..., R.drawable.outer20
//        )
//
//        recyclerView.layoutManager = GridLayoutManager(this, 2)
//        recyclerView.adapter = OuterAdapter(images)
    }
}
