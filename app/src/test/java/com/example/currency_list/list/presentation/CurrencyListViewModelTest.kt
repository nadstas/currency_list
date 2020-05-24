package com.example.currency_list.list.presentation

import com.example.currency_list.data.Currency
import com.example.currency_list.data.CurrencyListRepository
import com.example.currency_list.extensions.AndroidArchExecutorExtension
import com.example.currency_list.extensions.CoroutinesDispatcherExtension
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(AndroidArchExecutorExtension::class, CoroutinesDispatcherExtension::class)
internal class CurrencyListViewModelTest {

    private val currenciesChannel = Channel<List<Currency>>()
    private val repository: CurrencyListRepository = mockk(relaxed = true) {
        every { getCurrencies() } returns currenciesChannel.receiveAsFlow()
    }
    private val viewModel = CurrencyListViewModel(repository)

    @Test
    fun `refreshing state true by default`() {
        assertTrue(viewModel.isRefreshing.value!!)
    }

    @Test
    fun `reset refreshing state after data received`() {

        viewModel.currenciesList.observeForever {}
        currenciesChannel.offer(listOf(mockk()))

        assertFalse(viewModel.isRefreshing.value!!)
    }

    @Test
    fun `set refreshing state when refresh called`() {

        viewModel.currenciesList.observeForever {}
        currenciesChannel.offer(listOf(mockk()))
        viewModel.onRefresh()

        assertTrue(viewModel.isRefreshing.value!!)
    }

}