package com.example.currency_list.list.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.currency_list_item.view.*

class CurrencyViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    val name: TextView = root.currency_list_item_name
    val price: TextView = root.currency_list_item_price
}
