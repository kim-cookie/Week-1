package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class ProductAdapter(
    private val context: Context,
    private val productList: MutableList<Product>
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

        productView.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.product_detail_popup)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val image = dialog.findViewById<ImageView>(R.id.popupImage)
            val name = dialog.findViewById<TextView>(R.id.popupName)
            val price = dialog.findViewById<TextView>(R.id.popupPrice)
            val sizeSpinner = dialog.findViewById<Spinner>(R.id.popupSizeSpinner)
            val quantityText = dialog.findViewById<TextView>(R.id.quantityText)
            val btnDecrease = dialog.findViewById<Button>(R.id.btnDecrease)
            val btnIncrease = dialog.findViewById<Button>(R.id.btnIncrease)
            val totalPriceText = dialog.findViewById<TextView>(R.id.totalPriceText)
            val btnAddToCart = dialog.findViewById<Button>(R.id.btnAddToCart)

            val sizes = arrayOf("S", "M", "L", "XL")
            sizeSpinner.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, sizes)

            var quantity = 1
            quantityText.text = quantity.toString()

            fun updateTotalPrice() {
                // ₩ 기호 제거 후 숫자로 변환
                val unitPrice = product.price.replace("₩", "").replace(",", "").toInt()
                val total = unitPrice * quantity
                totalPriceText.text = "총 ₩%,d".format(total)
            }

            updateTotalPrice()

            btnDecrease.setOnClickListener {
                if (quantity > 1) {
                    quantity--
                    quantityText.text = quantity.toString()
                    updateTotalPrice()
                }
            }

            btnIncrease.setOnClickListener {
                quantity++
                quantityText.text = quantity.toString()
                updateTotalPrice()
            }

            image.setImageResource(product.image)
            name.text = product.name
            price.text = product.price

            btnAddToCart.setOnClickListener {
                val selectedSize = sizeSpinner.selectedItem.toString()

                // 예시: 장바구니에 저장할 데이터
                val cartItem = CartItem(
                    name = product.name,
                    price = product.price,
                    image = product.image,
                    size = selectedSize,
                    quantity = quantity
                )

                val userId = UserManager.getLoggedInUser(context)?.id
                if (userId != null) {
                    val currentCart = CartManager.getCart(context, userId).toMutableList()
                    currentCart.add(cartItem)
                    CartManager.saveCart(context, userId, currentCart)

                    Toast.makeText(context, "장바구니에 추가되었습니다!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            }

            dialog.show()

        }

        val heartIcon = productView.findViewById<ImageView>(R.id.heartIcon)

// 초기 아이콘 상태 설정
        heartIcon.setImageResource(
            if (product.isLiked) R.drawable.heart_filled
            else R.drawable.heart_outline
        )

// 클릭 이벤트: 찜 toggle
        heartIcon.setOnClickListener {
            val userId = UserManager.getLoggedInUser(context)?.id

            if (userId == null) {
                Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            product.isLiked = !product.isLiked

            // 현재 위시리스트 불러오기
            val wishlist = WishlistManager.getWishlist(context, userId)

            if (product.isLiked) {
                if (!wishlist.contains(product)) wishlist.add(product)
            } else {
                wishlist.remove(product)
            }

            WishlistManager.saveWishlist(context, userId, wishlist)

            notifyDataSetChanged()
        }
        return productView
    }
}
