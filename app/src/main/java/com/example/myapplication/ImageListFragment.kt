package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment

class ImageListFragment : Fragment() {

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): ImageListFragment {
            val fragment = ImageListFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_image_list, container, false)

        val selectedCategory = arguments?.getString(ARG_CATEGORY)

        val productList = when (selectedCategory) {
            "아우터" -> ProductData.outerwearList
            "상의" -> ProductData.topList
            "바지" -> ProductData.pantsList
//            "치마" -> ProductData.skirtsList
//            "원피스" -> ProductData.dressesList
//            "양말" -> ProductData.socksList
//            "모자" -> ProductData.hatsList
//            "신발" -> ProductData.shoesList
//            "잡화" -> ProductData.accessoriesList
            else -> arrayListOf()
        }

        // 보여줄 리스트는 mutableList로 복사
        val currentList = productList.toMutableList()
        val adapter = ProductAdapter(requireContext(), currentList)

        val gridView = view.findViewById<GridView>(R.id.gridView)
        gridView.adapter = adapter

        val searchEditText = view.findViewById<android.widget.EditText>(R.id.searchEditText)
        val searchIcon = view.findViewById<android.widget.ImageView>(R.id.searchIcon)

        searchIcon.setOnClickListener {
            val query = searchEditText.text.toString().trim()

            val filtered = if (query.isEmpty()) {
                productList
            } else {
                productList.filter { it.name.contains(query, ignoreCase = true) }
            }

            currentList.clear()
            currentList.addAll(filtered)
            adapter.notifyDataSetChanged()
        }

        return view
    }
}
