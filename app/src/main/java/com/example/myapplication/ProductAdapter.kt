package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.bumptech.glide.Glide

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

        Glide.with(context).load(product.image).into(imageView)
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
            val btnShowReviews = dialog.findViewById<Button>(R.id.btnShowReviews)

            val sizes = arrayOf("S", "M", "L", "XL")
            sizeSpinner.adapter =
                ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, sizes)

            var quantity = 1
            quantityText.text = quantity.toString()
            btnShowReviews.text = "리뷰 보기"

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

            Glide.with(context).load(product.image).into(image)
            name.text = product.name
            price.text = product.price

            btnShowReviews.setOnClickListener {
                showReviewDialog(product, false)  // 또는 product.id 등 item 고유 ID
            }

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

    private fun showReviewDialog(product: Product, allowWrite: Boolean) {
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
