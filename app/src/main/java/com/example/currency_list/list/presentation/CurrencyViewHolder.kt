package com.example.currency_list.list.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.currency_list.databinding.CurrencyListItemBinding

class CurrencyViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    private val binding = CurrencyListItemBinding.bind(root)

    val name get() = binding.currencyListItemName
    val price get() = binding.currencyListItemPrice
}
