package com.example.currency_list.list.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

inline fun <T> LiveData<T>.observe(fragment: Fragment, crossinline observer: (T) -> Unit) {
    observe(fragment.viewLifecycleOwner, Observer { observer.invoke(it) })
}

inline fun RecyclerView.onScrolled(crossinline onScrolled: (dx: Int, dy: Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            onScrolled(dx, dy)
        }
    })
}