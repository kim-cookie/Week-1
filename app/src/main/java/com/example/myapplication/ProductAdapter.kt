package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.BaseAdapter
import android.widget.Toast

class ProductAdapter(
    private val context: Context,
    private val productList: MutableList<Product>,
    private val onWishlistChanged: (() -> Unit)? = null,
    private val isWishlistScreen: Boolean
) : BaseAdapter() {

    private val userId: String? = UserManager.getLoggedInUser(context)?.id

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
        val heartIcon = productView.findViewById<ImageView>(R.id.heartIcon)

        imageView.setImageResource(product.image)
        textViewName.text = product.name
        textViewPrice.text = product.price

        if (userId != null && WishlistManager.isLiked(context, userId, product)) {
            heartIcon.setImageResource(R.drawable.heart_filled)
        } else {
            heartIcon.setImageResource(R.drawable.heart_outline)
        }

        heartIcon.setOnClickListener {
            if (userId == null) {
                Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isLikedNow = WishlistManager.isLiked(context, userId, product)
            WishlistManager.toggleWishlist(context, userId, product)

            heartIcon.setImageResource(
                if (!isLikedNow) R.drawable.heart_filled else R.drawable.heart_outline
            )

            if (isLikedNow && isWishlistScreen) {
                productList.remove(product)
                onWishlistChanged?.invoke()
            }

            notifyDataSetChanged()
        }

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
            sizeSpinner.adapter =
                ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, sizes)

            var quantity = 1
            quantityText.text = quantity.toString()

            fun updateTotalPrice() {
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

        return productView
    }
}
