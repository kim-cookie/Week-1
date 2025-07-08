package com.example.myapplication

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CartFragment : Fragment() {

    private lateinit var cartListView: ListView
    private lateinit var totalPriceText: TextView
    private lateinit var btnCheckout: Button
    private lateinit var cartAdapter: CartItemAdapter

    private var userId: String? = null
    private var cartItems: MutableList<CartItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        cartListView = view.findViewById(R.id.cartListView)
        totalPriceText = view.findViewById(R.id.totalPriceText)
        btnCheckout = view.findViewById(R.id.btnCheckout)

        // 장바구니 어댑터 설정
        userId = UserManager.getLoggedInUser(requireContext())?.id
        if (userId == null) return view // 로그인 안된 경우 예외 처리
        cartItems = CartManager.getCart(requireContext(), userId!!).toMutableList()

        cartAdapter = CartItemAdapter(requireContext(), cartItems) {
            updateTotalPrice()
        }
        cartListView.adapter = cartAdapter

        // 총 가격 계산 및 표시
        updateTotalPrice()

        val toggleBtn = view.findViewById<Button>(R.id.btnToggleSelectAll)

        toggleBtn.setOnClickListener {
            val allSelected = cartItems.all { it.isSelected }

            if (allSelected) {
                // 모두 선택되어 있는 경우 → 전체 해제
                cartItems.forEach { it.isSelected = false }
                toggleBtn.text = "전체"
            } else {
                // 일부 또는 전부 선택 안 되어 있는 경우 → 전체 선택
                cartItems.forEach { it.isSelected = true }
                toggleBtn.text = "전체"
            }

            cartListView.adapter = cartAdapter
            updateTotalPrice()
        }


        btnCheckout.setOnClickListener {
            val selectedItems = cartItems.filter { it.isSelected }
            if (selectedItems.isEmpty()) {
                Toast.makeText(requireContext(), "선택된 상품이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                showPurchasePopup(selectedItems)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        userId?.let {
            cartItems = CartManager.getCart(requireContext(), it).toMutableList()
            cartAdapter = CartItemAdapter(requireContext(), cartItems) {
                updateTotalPrice()
            }
            cartListView.adapter = cartAdapter
            updateTotalPrice()
        }
    }


    private fun updateTotalPrice() {
        var total = 0
        for (item in cartItems) {
            if (item.isSelected) {
                val price = item.price.replace("₩", "").replace(",", "").toInt()
                total += price * item.quantity
            }
        }
        totalPriceText.text = "총 가격: ₩%,d".format(total)
    }

    fun showPurchasePopup(selectedItems: List<CartItem>) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.purchase_popup)

        val listView = dialog.findViewById<ListView>(R.id.selectedItemListView)
        val totalPriceText = dialog.findViewById<TextView>(R.id.totalPriceText)
        val finalPriceText = dialog.findViewById<TextView>(R.id.finalPriceText)
        val discountSpinner = dialog.findViewById<Spinner>(R.id.discountSpinner)
        val confirmBtn = dialog.findViewById<Button>(R.id.btnConfirmPurchase)

        // 어댑터 설정
        val adapter = object : ArrayAdapter<CartItem>(
            requireContext(),
            R.layout.purchase_item,
            selectedItems
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context)
                    .inflate(R.layout.purchase_item, parent, false)
                val item = getItem(position)

                val nameText = view.findViewById<TextView>(R.id.itemName)
                val sizeText = view.findViewById<TextView>(R.id.itemSize)
                val quantityText = view.findViewById<TextView>(R.id.itemQuantity)
                val totalPricePerItemText = view.findViewById<TextView>(R.id.itemTotalPrice)
                val imageView = view.findViewById<ImageView>(R.id.itemImage)
                val priceText = view.findViewById<TextView>(R.id.itemPrice)

                nameText.text = item?.name
                sizeText.text = "사이즈: ${item?.size}"
                quantityText.text = "수량: ${item?.quantity}"
                priceText.text = item?.price


                item?.let {
                    val totalPrice =
                        it.price.replace("₩", "").replace(",", "").toInt() * it.quantity
                    totalPricePerItemText.text = "₩%,d".format(totalPrice)
                    Glide.with(context).load(it.image).into(imageView)
                }


                return view
            }
        }
        listView.adapter = adapter

        // 총 금액 계산
        val total =
            selectedItems.sumOf { it.price.replace("₩", "").replace(",", "").toInt() * it.quantity }
        totalPriceText.text = "총 금액: ₩%,d".format(total)

        var finalPrice = total
        finalPriceText.text = "최종 금액: ₩%,d".format(finalPrice)

        // 할인 옵션 설정
        val discountOptions = listOf("선택 안 함", "5% 할인", "10% 할인", "₩10,000 할인")
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            discountOptions
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        discountSpinner.adapter = spinnerAdapter

        // Spinner 선택 이벤트 처리
        discountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                finalPrice = when (position) {
                    1 -> (total * 0.95).toInt()
                    2 -> (total * 0.9).toInt()
                    3 -> (total - 10000).coerceAtLeast(0)
                    else -> total
                }
                finalPriceText.text = "최종 금액: ₩%,d".format(finalPrice)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                finalPrice = total
                finalPriceText.text = "최종 금액: ₩%,d".format(finalPrice)
            }
        }

        // 결제 버튼 동작 (예시)
        confirmBtn.setOnClickListener {
            Toast.makeText(requireContext(), "결제 완료!", Toast.LENGTH_SHORT).show()

            userId?.let { uid ->
                CartManager.removeAll(selectedItems)
                cartItems.removeAll(selectedItems)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val now = dateFormat.format(Date())

                selectedItems.forEach {
                    it.purchaseDate = now
                }
                PurchaseManager.addPurchases(requireContext(), uid, selectedItems)
                CartManager.saveCart(requireContext(), uid, cartItems)
            }

            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            val updatedList = CartManager.getCart(requireContext(), userId!!).toMutableList()
            cartAdapter = CartItemAdapter(requireContext(), updatedList) {
                updateTotalPrice()
            }
            cartItems = updatedList
            cartListView.adapter = cartAdapter
            CartManager.saveCart(requireContext(), userId!!, cartItems)
            updateTotalPrice()
        }

        dialog.show()

        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            (resources.displayMetrics.heightPixels * 0.8).toInt(),
        )
    }


}
