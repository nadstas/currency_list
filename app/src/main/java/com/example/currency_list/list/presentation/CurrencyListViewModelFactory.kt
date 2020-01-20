package com.example.currency_list.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CurrencyListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyListViewModel::class.java)) {
            return CurrencyListViewModel() as T
        } else {
            throw IllegalStateException("Unknown ViewModel class ${modelClass.simpleName}")
        }
    }
}