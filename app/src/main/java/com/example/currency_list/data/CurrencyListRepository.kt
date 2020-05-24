package com.example.currency_list.data

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.concurrent.TimeUnit

private typealias CurrencyId = String

private const val TAG = "CurrencyListRepository"
private val ONE_SECOND = TimeUnit.SECONDS.toMillis(1)

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class CurrencyListRepository(
    private val webApiProvider: WebApiProvider = WebApiProvider()
) {
    private val allCurrencies: MutableMap<CurrencyId, Currency> = LinkedHashMap()
    private var visibleCurrencies: List<Currency> = listOf()

    private val forceChannel = Channel<Unit>()
    private val forceFlow = forceChannel.receiveAsFlow()
    private val webApi get() = webApiProvider.webApi

    private val thereIsNoCache get() = allCurrencies.isEmpty()

    fun getCurrencies(): Flow<List<Currency>> {
        return merge(interval(ONE_SECOND), forceFlow.onEach { clearVisibleItems() })
            .flatMapMerge {
                loadCurrencies()
                    .catch {
                        Log.e(TAG, "Load error", it)
                        emit(allCurrencies.valuesList())
                    }
            }
            .flowOn(io)
    }

    private fun loadCurrencies(): Flow<List<Currency>> {
        if (thereIsNoCache || visibleCurrencies.isEmpty()) {
            return flow { emit(webApi.getAllCurrencies()) }
                .onEach {
                    allCurrencies.clear()
                    allCurrencies.putAll(it)
                }
        }

        return loadOnlyVisible()
            .onEach { allCurrencies.putAll(it) }
            .map { allCurrencies.valuesList() }
    }

    private fun loadOnlyVisible(): Flow<List<Currency>> {
        val visibleCurrencyIds = visibleCurrencies.map { it.id }.reduce { acc, s -> "$acc,$s" }
        return flow { emit(webApi.getCurrencies(ids = visibleCurrencyIds)) }
    }

    fun forceUpdate() = forceChannel.offer(Unit)

    fun setVisibleItems(visibleCurrencies: List<Currency>) {
        this.visibleCurrencies = visibleCurrencies
    }

    private fun clearVisibleItems() = setVisibleItems(emptyList())
    private fun MutableMap<CurrencyId, Currency>.valuesList() = values.toList()
    private fun MutableMap<CurrencyId, Currency>.putAll(list: List<Currency>) =
        also { list.forEach { put(it.id, it) } }
}
