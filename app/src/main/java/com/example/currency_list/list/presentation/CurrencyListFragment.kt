package com.example.currency_list.list.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.currency_list.R
import com.example.currency_list.data.Currency
import kotlinx.android.synthetic.main.currency_list.view.*

class CurrencyListFragment : Fragment() {

    private var vh: ViewHolder? = null

    // I don't use delegate properties because they are very slow (use a lot of reflection).
    private val layoutManager = lazy { LinearLayoutManager(context) }
    private val adapter = lazy { CurrencyListAdapter() }
    private val viewModel = lazy {
        ViewModelProviders.of(this, CurrencyListViewModelFactory())
            .get(CurrencyListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vh = ViewHolder(inflater.inflate(R.layout.currency_list, container)).apply {
            swipeToRefresh.setOnRefreshListener { viewModel.value.onRefresh() }
            currencyList.layoutManager = layoutManager.value
            currencyList.adapter = adapter.value
            currencyList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    viewModel.value.setVisibleItems(
                        getVisibleCurrencies(layoutManager.value, adapter.value)
                    )
                }
            })
        }

        viewModel.value.isRefreshing
            .observe(this, Observer { vh?.swipeToRefresh?.isRefreshing = it })
        viewModel.value.currenciesList
            .subscribeWithLifecycle(this,
                onNext = { adapter.value.submitList(it) })

        return vh?.root
    }

    private fun getVisibleCurrencies(
        layoutManager: LinearLayoutManager,
        adapter: CurrencyListAdapter
    ): List<Currency> {
        return adapter.currentList.slice(
            layoutManager.findFirstVisibleItemPosition()..
                    layoutManager.findLastVisibleItemPosition()
        )
    }

    private class ViewHolder(val root: View) {
        val swipeToRefresh: SwipeRefreshLayout = root.currency_list_swipe_to_refresh
        val currencyList: RecyclerView = root.currency_list
    }
}
