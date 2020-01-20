package com.example.currency_list.data

import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable.just
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
        every { mockApi.getAllCurrencies() } returns just(listOf(BTC, BMX, HPB))
        every { mockApi.getCurrencies(ids = "BMX") } returns just(listOf(BMX_2))

        repository.setVisibleItems(listOf(BMX))
        repository.currencyListObservable.test().awaitCount(2)
            .assertValueAt(1, listOf(BTC, BMX_2, HPB))
    }

    private fun currencyMock(currencyId: String, mockPrice: String): Currency {
        return mockk {
            every { id } returns currencyId
            every { price } returns mockPrice
        }
    }
}