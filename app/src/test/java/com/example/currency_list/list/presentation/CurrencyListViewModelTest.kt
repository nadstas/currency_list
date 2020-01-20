package com.example.currency_list.list.presentation

import com.example.currency_list.data.Currency
import com.example.currency_list.data.CurrencyListRepository
import com.example.currency_list.extensions.AndroidArchExecutorExcension
import io.mockk.every
import io.mockk.mockk
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(AndroidArchExecutorExcension::class)
internal class CurrencyListViewModelTest {

    private val currenciesTestObservable = BehaviorSubject.create<List<Currency>>()
    private val repository: CurrencyListRepository = mockk(relaxed = true) {
        every { currencyListObservable } returns currenciesTestObservable
    }
    private val viewModel = CurrencyListViewModel(repository, Schedulers.trampoline())

    @Test
    fun `refreshing state true by default`() {
        assertTrue(viewModel.isRefreshing.value!!)
    }

    @Test
    fun `reset refreshing state after date received`() {

        currenciesTestObservable.onNext(listOf(mockk()))
        viewModel.currenciesList.blockingFirst()

        assertFalse(viewModel.isRefreshing.value!!)
    }

    @Test
    fun `set refreshing state when refresh called`() {

        currenciesTestObservable.onNext(listOf(mockk()))
        viewModel.currenciesList.blockingFirst()
        viewModel.onRefresh()

        assertTrue(viewModel.isRefreshing.value!!)
    }

}