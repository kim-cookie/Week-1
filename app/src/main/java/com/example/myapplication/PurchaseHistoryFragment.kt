package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment

class PurchaseHistoryFragment : Fragment() {
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_purchase_history, container, false)

        listView = view.findViewById(R.id.purchaseListView)

        val displayList = createDisplayList()
        val adapter = PurchaseGroupedAdapter(requireContext(), displayList)
        listView.adapter = adapter

        return view
    }

    private fun createDisplayList(): List<PurchaseDisplayItem> {
        val grouped = PurchaseManager.getAll()
            .groupBy { it.purchaseDate ?: "날짜 없음" }
            .toSortedMap(reverseOrder())

        val result = mutableListOf<PurchaseDisplayItem>()
        for ((date, items) in grouped) {
            result.add(PurchaseDisplayItem.Header(date))
            result.addAll(items.map { PurchaseDisplayItem.Product(it) })
        }
        return result
    }
}