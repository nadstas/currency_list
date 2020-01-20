package com.example.currency_list.list.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.currency_list.R
import com.example.currency_list.data.Currency

class CurrencyListAdapter : ListAdapter<Currency, CurrencyViewHolder>(CurrencyItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CurrencyViewHolder(inflater.inflate(R.layout.currency_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = getItem(position)
        holder.name.text = if (currency.name.isEmpty()) currency.id else currency.name
        holder.price.text = currency.price
    }

}

private class CurrencyItemDiffCallback : DiffUtil.ItemCallback<Currency>() {

    override fun areItemsTheSame(oldItem: Currency, newItem: Currency) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.name == newItem.name && oldItem.price == newItem.price
    }
}
