package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class CartItemAdapter(
    private val context: Context,
    private val cartItems: MutableList<CartItem>,
    private val onCartUpdated: () -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = cartItems.size

    override fun getItem(position: Int): Any = cartItems[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = cartItems[position]
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.cart_item, parent, false)

        val imageView = view.findViewById<ImageView>(R.id.cartItemImage)
        val nameText = view.findViewById<TextView>(R.id.cartItemName)
        val sizeText = view.findViewById<TextView>(R.id.cartItemSize)
        val quantityText = view.findViewById<TextView>(R.id.cartItemQuantity)
        val priceText = view.findViewById<TextView>(R.id.cartItemPrice)
        val deleteButton = view.findViewById<Button>(R.id.btnDelete)
        val checkBox = view.findViewById<CheckBox>(R.id.itemCheckBox)

        imageView.setImageResource(item.image)
        nameText.text = item.name
        sizeText.text = "사이즈: ${item.size}"
        quantityText.text = "수량: ${item.quantity}"
        priceText.text = item.price
        checkBox.isChecked = item.isSelected

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            item.isSelected = isChecked
            onCartUpdated()
        }

        deleteButton.setOnClickListener {
            val userId = UserManager.getLoggedInUser(context)?.id
            if (userId != null) {
                CartManager.removeItem(item)
                cartItems.removeAt(position)
                CartManager.saveCart(context, userId, cartItems)
                notifyDataSetChanged()
                onCartUpdated()
                Toast.makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

        view.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.product_detail_popup)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val popupImage = dialog.findViewById<ImageView>(R.id.popupImage)
            val popupName = dialog.findViewById<TextView>(R.id.popupName)
            val popupPrice = dialog.findViewById<TextView>(R.id.popupPrice)
            val sizeSpinner = dialog.findViewById<Spinner>(R.id.popupSizeSpinner)
            val quantityText = dialog.findViewById<TextView>(R.id.quantityText)
            val btnDecrease = dialog.findViewById<Button>(R.id.btnDecrease)
            val btnIncrease = dialog.findViewById<Button>(R.id.btnIncrease)
            val totalPriceText = dialog.findViewById<TextView>(R.id.totalPriceText)
            val btnAddToCart = dialog.findViewById<Button>(R.id.btnAddToCart)

            val sizes = arrayOf("S", "M", "L", "XL")
            sizeSpinner.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, sizes)

            var quantity = item.quantity
            quantityText.text = quantity.toString()
            sizeSpinner.setSelection(sizes.indexOf(item.size))

            popupImage.setImageResource(item.image)
            popupName.text = item.name
            popupPrice.text = item.price

            fun updateTotalPrice() {
                val unitPrice = item.price.replace("₩", "").replace(",", "").toInt()
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

            btnAddToCart.text = "수정하기"
            btnAddToCart.setOnClickListener {
                val newSize = sizeSpinner.selectedItem.toString()
                item.size = newSize
                item.quantity = quantity

                val userId = UserManager.getLoggedInUser(context)?.id
                if (userId != null) {
                    CartManager.saveCart(context, userId, cartItems)
                }

                notifyDataSetChanged()
                onCartUpdated()
                Toast.makeText(context, "장바구니가 수정되었습니다", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            dialog.show()
        }

        return view
    }
}
