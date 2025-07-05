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

        val gridView = view.findViewById<GridView>(R.id.gridView)
        gridView.adapter = ProductAdapter(requireContext(), productList)

        return view
    }
}
