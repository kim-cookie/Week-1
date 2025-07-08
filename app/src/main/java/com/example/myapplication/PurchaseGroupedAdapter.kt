package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PurchaseGroupedAdapter(
    private val context: Context,
    private val items: List<PurchaseDisplayItem>
) : BaseAdapter() {

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): Any = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val item = items[position]

        return when (item) {
            is PurchaseDisplayItem.Header -> {
                val view = inflater.inflate(R.layout.purchase_header, parent, false)
                val title = view.findViewById<TextView>(R.id.dateHeaderText)
                title.text = item.date
                view
            }
            is PurchaseDisplayItem.Product -> {
                val view = inflater.inflate(R.layout.purchase_item, parent, false)
                val p = item.item

                view.findViewById<TextView>(R.id.itemName).text = p.name
                view.findViewById<TextView>(R.id.itemQuantity).text = "수량: ${p.quantity}"
                view.findViewById<TextView>(R.id.itemSize).text = "사이즈: ${p.size}"
                view.findViewById<TextView>(R.id.itemPrice).text = p.price
                Glide.with(context).load(p.image).into(view.findViewById(R.id.itemImage))

                val unitPrice = p.price.replace("₩", "").replace(",", "").toInt()
                val total = unitPrice * p.quantity
                view.findViewById<TextView>(R.id.itemTotalPrice).text = "총 ₩%,d".format(total)

                view.setOnClickListener {
                    val product = Product(name = p.name, price = p.price, image = p.image)
                    showReviewDialog(product, allowWrite = true)
                }

                view
            }
        }
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

            Toast.makeText(context, "리뷰 등록 시도 중...", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}
