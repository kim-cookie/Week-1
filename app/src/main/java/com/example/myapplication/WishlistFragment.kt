package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class WishlistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)

        val gridView = view.findViewById<GridView>(R.id.gridViewWishlist)
        val emptyText = view.findViewById<TextView>(R.id.emptyTextView)

        val wishlist = ProductData.wishlist

        if (wishlist.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            gridView.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            gridView.visibility = View.VISIBLE
            val adapter = ProductAdapter(requireContext(), wishlist)
            gridView.adapter = adapter
        }

        val backButton = view.findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()  // ← 이전 Fragment(ImageListFragment)로 되돌아감
        }

        return view
    }
}
