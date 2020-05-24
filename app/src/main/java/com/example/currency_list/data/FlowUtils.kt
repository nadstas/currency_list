package com.example.currency_list.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

val io = Dispatchers.IO

fun interval(periodMillis: Long) = flow {
    while (true) {
        emit(Unit)
        delay(periodMillis)
    }
}