package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ProductAdapter(
    private val context: Context,
    private val productList: ArrayList<Product>
) : BaseAdapter() {

    override fun getCount(): Int = productList.size

    override fun getItem(position: Int): Any = productList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val product = productList[position]
        val inflater = LayoutInflater.from(context)
        val productView = inflater.inflate(R.layout.product_item, parent, false)

        val imageView = productView.findViewById<ImageView>(R.id.imageView)
        val textViewName = productView.findViewById<TextView>(R.id.textViewName)
        val textViewPrice = productView.findViewById<TextView>(R.id.textViewPrice)

        imageView.setImageResource(product.image)
        textViewName.text = product.name
        textViewPrice.text = product.price

//        imageView.setOnClickListener {
//            val intent = Intent(context, ProductDetailActivity::class.java).apply {
//                putExtra("name", product.name)
//                putExtra("price", product.price)
//                putExtra("image", product.image)
//            }
//            context.startActivity(intent)
//        }

        return productView
    }
}
