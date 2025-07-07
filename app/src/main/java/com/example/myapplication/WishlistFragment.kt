package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class WishlistFragment : Fragment() {

    private lateinit var adapter: ProductAdapter
    private lateinit var wishlist: MutableList<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)

        val gridView = view.findViewById<GridView>(R.id.gridViewWishlist)
        val emptyText = view.findViewById<TextView>(R.id.emptyTextView)
        val backButton = view.findViewById<ImageView>(R.id.backButton)

        val userId = UserManager.getLoggedInUser(requireContext())?.id
        if (userId == null) {
            emptyText.text = "로그인이 필요합니다."
            emptyText.visibility = View.VISIBLE
            gridView.visibility = View.GONE
            return view
        }

        wishlist = WishlistManager.getWishlist(requireContext(), userId)

        adapter = ProductAdapter(requireContext(), wishlist, {
            // 콜백: 위시리스트가 변경되었을 때 실행됨
            adapter.notifyDataSetChanged()
            updateEmptyTextVisibility(emptyText, gridView)
        }, isWishlistScreen = true)

        gridView.adapter = adapter
        updateEmptyTextVisibility(emptyText, gridView)

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun updateEmptyTextVisibility(emptyText: TextView, gridView: GridView) {
        if (wishlist.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            gridView.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            gridView.visibility = View.VISIBLE
        }
    }
}
