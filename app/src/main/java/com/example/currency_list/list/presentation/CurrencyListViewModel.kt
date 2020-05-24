package com.example.currency_list.list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.currency_list.data.Currency
import com.example.currency_list.data.CurrencyListRepository
import kotlinx.coroutines.flow.onEach

class CurrencyListViewModel(private val repository: CurrencyListRepository) : ViewModel() {

    private val _isRefreshing = MutableLiveData(true)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    val currenciesList = repository.getCurrencies()
        .onEach { _isRefreshing.value = false }
        .asLiveData()

    fun onRefresh() {
        _isRefreshing.value = true
        repository.forceUpdate()
    }

    fun setVisibleItems(visibleCurrencies: List<Currency>) {
        repository.setVisibleItems(visibleCurrencies)
    }
}
