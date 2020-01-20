package com.example.currency_list.list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currency_list.data.Currency
import com.example.currency_list.data.CurrencyListRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

class CurrencyListViewModel(
    private val repository: CurrencyListRepository = CurrencyListRepository(),
    mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel() {

    private val _isRefreshing = MutableLiveData<Boolean>(true)

    val isRefreshing = _isRefreshing as LiveData<Boolean>
    val currenciesList: Observable<List<Currency>> = repository.currencyListObservable
        .observeOn(mainThreadScheduler)
        .doOnNext { _isRefreshing.value = false }

    fun onRefresh() {
        _isRefreshing.value = true
        repository.forceUpdate()
    }

    fun setVisibleItems(visibleCurrencies: List<Currency>) {
        repository.setVisibleItems(visibleCurrencies)
    }
}
