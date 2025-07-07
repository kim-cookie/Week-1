package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

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
                view.findViewById<ImageView>(R.id.itemImage).setImageResource(p.image)

                val unitPrice = p.price.replace("₩", "").replace(",", "").toInt()
                val total = unitPrice * p.quantity
                view.findViewById<TextView>(R.id.itemTotalPrice).text = "총 ₩%,d".format(total)

                view
            }
        }
    }
}
