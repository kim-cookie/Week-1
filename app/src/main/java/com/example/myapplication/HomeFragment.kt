package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val cities = arrayListOf("아우터", "상의", "바지", "치마", "원피스", "양말", "모자", "신발", "잡화")
        val listView = view.findViewById<ListView>(R.id.myListView)
        listView.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)

        listView.setOnItemClickListener { _, _, position, _ ->
            if (cities[position] == "아우터") {
                val intent = Intent(requireContext(), OuterActivity::class.java)
                startActivity(intent)
            }
        }

        return view
    }
}
