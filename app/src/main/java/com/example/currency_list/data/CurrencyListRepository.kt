package com.example.currency_list.data

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observable.interval
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

private typealias CurrencyId = String

private const val TAG = "CurrencyListRepository"

class CurrencyListRepository(
    private val webApiProvider: WebApiProvider = WebApiProvider()
) {
    private val allCurrencies: MutableMap<CurrencyId, Currency> = LinkedHashMap()
    private var visibleCurrencies: List<Currency> = listOf()

    private val forceSubject = PublishSubject.create<Long>()
    private val webApi get() = webApiProvider.webApi

    private val thereIsNoCache get() = allCurrencies.isEmpty()

    val currencyListObservable: Observable<List<Currency>> =
        interval(0, 1, TimeUnit.SECONDS)
            .mergeWith(forceSubject.doOnNext { clearVisibleItems() })
            .flatMap {
                loadCurrencies()
                    .doOnError { Log.d(TAG, "Load error", it) }
                    .onErrorReturn { allCurrencies.valuesList() }
            }

    private fun loadCurrencies(): Observable<List<Currency>> {
        if (thereIsNoCache || visibleCurrencies.isEmpty()) {
            return webApi.getAllCurrencies()
                .doOnNext {
                    allCurrencies.clear()
                    allCurrencies.putAll(it)
                }
        }

        return loadOnlyVisible()
            .doOnNext { allCurrencies.putAll(it) }
            .map { allCurrencies.valuesList() }
    }

    private fun loadOnlyVisible(): Observable<List<Currency>> {
        val visibleCurrencyIds = visibleCurrencies.map { it.id }.reduce { acc, s -> "$acc,$s" }
        return webApi.getCurrencies(ids = visibleCurrencyIds)
    }

    fun forceUpdate() = forceSubject.onNext(1)

    fun setVisibleItems(visibleCurrencies: List<Currency>) {
        this.visibleCurrencies = visibleCurrencies
    }

    private fun clearVisibleItems() = setVisibleItems(emptyList())
    private fun MutableMap<CurrencyId, Currency>.valuesList() = values.toList()
    private fun MutableMap<CurrencyId, Currency>.putAll(list: List<Currency>) =
        also { list.forEach { put(it.id, it) } }
}
