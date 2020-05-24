package com.example.currency_list.data

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CurrencyListRepositoryTest {

    private val BTC = currencyMock("BTC", "1")
    private val HPB = currencyMock("HPB", "1")
    private val BMX = currencyMock("BMX", "1")
    private val BMX_2 = currencyMock("BMX", "2")

    private val mockApi: WebApi = mockk()
    private val webApiProvider: WebApiProvider = mockk { every { webApi } returns mockApi }
    private val repository = CurrencyListRepository(webApiProvider)

    @Test
    fun `correct merge visible elements`() {
        coEvery { mockApi.getAllCurrencies() } returns listOf(BTC, BMX, HPB)
        coEvery { mockApi.getCurrencies(ids = "BMX") } returns listOf(BMX_2)

        repository.setVisibleItems(listOf(BMX))
        runBlocking {
            val result = repository.getCurrencies().take(2).toList().last()
            assertEquals(result, listOf(BTC, BMX_2, HPB))
        }
    }

    private fun currencyMock(currencyId: String, mockPrice: String): Currency {
        return mockk {
            every { id } returns currencyId
            every { price } returns mockPrice
        }
    }
}