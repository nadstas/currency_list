package com.example.currency_list.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currency_list.data.CurrencyListRepository

internal object CurrencyListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyListViewModel::class.java)) {
            return CurrencyListViewModel(CurrencyListRepository()) as T
        } else {
            throw IllegalStateException("Unknown ViewModel class ${modelClass.simpleName}")
        }
    }
}

internal fun CurrencyListFragment.currencyListViewModel() =
    ViewModelProvider(this, CurrencyListViewModelFactory)[CurrencyListViewModel::class.java]