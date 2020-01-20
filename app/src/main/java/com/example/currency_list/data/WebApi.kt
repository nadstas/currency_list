package com.example.currency_list.data

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

private const val IDS = "ids"
private const val KEY = "key"
private const val CONVERT = "convert"

interface WebApi {
    /**
     * @return List of all available currencies.
     */
    @GET("currencies/ticker")
    fun getAllCurrencies(
        @Query(KEY) key: String = KeyStorage.apiKey,
        @Query(CONVERT) convertTo: String = "EUR"
    ): Observable<List<Currency>>

    /**
     * @param ids currencies id separate by comma. (BTC,ETH,XRP)
     * @return [List<Currencies>] with given ids.
     */
    @GET("currencies/ticker")
    fun getCurrencies(
        @Query(KEY) key: String = KeyStorage.apiKey,
        @Query(CONVERT) convertTo: String = "EUR",
        @Query(IDS) ids: String
    ): Observable<List<Currency>>
}
