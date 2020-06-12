package com.example.currency_list.list.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.currency_list.R
import com.example.currency_list.data.Currency

private const val PRICE_CHANGED_PAYLOAD = "price_changed"

class CurrencyListAdapter : ListAdapter<Currency, CurrencyViewHolder>(CurrencyItemDiffCallback()) {

    fun sliceData(firstIndex: Int, lastIndex: Int) = currentList.slice(firstIndex..lastIndex)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(parent.inflate(R.layout.currency_list_item))
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = getItem(position)
        holder.name.text = if (currency.name.isEmpty()) currency.id else currency.name
        holder.price.setCurrentText(currency.price)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int, payloads: List<Any>) {
        val item = getItem(position)
        if (PRICE_CHANGED_PAYLOAD in payloads) {
            holder.price.setText(item.price)
        } else {
            onBindViewHolder(holder, position)
        }
    }
}

private class CurrencyItemDiffCallback : DiffUtil.ItemCallback<Currency>() {

    override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

    override fun getChangePayload(oldItem: Currency, newItem: Currency): Any? {
        if (oldItem.id == oldItem.id && oldItem.price != newItem.price) {
            return PRICE_CHANGED_PAYLOAD
        }
        return null
    }
}
