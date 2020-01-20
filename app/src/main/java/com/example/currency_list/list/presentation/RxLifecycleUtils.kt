package com.example.currency_list.list.presentation

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

private const val TAG = "ObservableUtils"

fun <T> Observable<T>.subscribeWithLifecycle(
    owner: LifecycleOwner,
    onNext: (T) -> Unit,
    onError: (Throwable) -> Unit = { Log.e(TAG, "Internal error", it) }
) {
    var disposable: Disposable? = null
    owner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStart() {
            if (disposable == null) {
                disposable = subscribe(onNext, onError)
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop() {
            disposable?.dispose()
            disposable = null
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            owner.lifecycle.removeObserver(this)
        }
    })
}
