package com.example.currency_list.list.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

val ViewGroup.inflater: LayoutInflater get() = LayoutInflater.from(context)
fun ViewGroup.inflate(@LayoutRes layout: Int, attach: Boolean = false): View =
    inflater.inflate(layout, this, attach)

inline fun <reified T : ViewModel> Fragment.viewModel(crossinline create: (Fragment) -> T) =
    ViewModelProvider(this, factory(create))[T::class.java]

inline fun <VM : ViewModel> Fragment.factory(crossinline create: (Fragment) -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>) = create(this@factory) as T
    }
}