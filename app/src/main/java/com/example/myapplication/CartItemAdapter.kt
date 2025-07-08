package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

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

        Glide.with(context).load(item.image).into(imageView)
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
            val btnShowReviews = dialog.findViewById<Button>(R.id.btnShowReviews)

            val sizes = arrayOf("S", "M", "L", "XL")
            sizeSpinner.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, sizes)

            var quantity = item.quantity
            quantityText.text = quantity.toString()
            sizeSpinner.setSelection(sizes.indexOf(item.size))

            Glide.with(context).load(item.image).into(popupImage)
            popupName.text = item.name
            popupPrice.text = item.price
            btnShowReviews.text = "리뷰 보기"

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

            btnShowReviews.setOnClickListener {
                showReviewDialog(item, false)  // 또는 product.id 등 item 고유 ID
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

    private fun showReviewDialog(product: CartItem, allowWrite: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_review, null)
        val dialog = Dialog(context)
        dialog.setContentView(dialogView)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val reviewEditText = dialogView.findViewById<EditText>(R.id.reviewEditText)
        val submitButton = dialogView.findViewById<Button>(R.id.submitReviewButton)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.reviewRecyclerView)
        val imageView = dialogView.findViewById<ImageView>(R.id.reviewProductImage)
        val nameText = dialogView.findViewById<TextView>(R.id.reviewProductName)
        val priceText = dialogView.findViewById<TextView>(R.id.reviewProductPrice)

        recyclerView.layoutManager = LinearLayoutManager(context)
        Glide.with(context).load(product.image).into(imageView)
        nameText.text = product.name
        priceText.text = product.price

        if (!allowWrite) {
            ratingBar.visibility = View.GONE
            reviewEditText.visibility = View.GONE
            submitButton.visibility = View.GONE
        }

        // 🔽 기존 리뷰 불러오기
        FirebaseFirestore.getInstance()
            .collection("reviews")
            .whereEqualTo("itemId", product.name)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val reviews = result.map { doc ->
                    Review(
                        itemId = doc.getString("itemId") ?: "",
                        userId = doc.getString("userId") ?: "",
                        userName = doc.getString("userName") ?: "",
                        rating = doc.getDouble("rating")?.toFloat() ?: 0f,
                        comment = doc.getString("comment") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                }
                recyclerView.adapter = ReviewAdapter(reviews)
            }

        // 🔼 내 리뷰 작성
        val user = UserManager.getLoggedInUser(context)
        val userId = user?.id ?: "anonymous"
        val userName = user?.name ?: "익명"

        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            val comment = reviewEditText.text.toString()

            if (rating == 0f || comment.isBlank()) {
                Toast.makeText(context, "별점과 리뷰를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val review = Review(
                itemId = product.name,
                userId = userId,
                userName = userName,
                rating = rating,
                comment = comment,
                timestamp = System.currentTimeMillis()
            )

            Log.d("REVIEW", "add() 실행 시도됨")
            FirebaseFirestore.getInstance()
                .collection("reviews")
                .add(review)

            Toast.makeText(context, "리뷰 등록", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}
