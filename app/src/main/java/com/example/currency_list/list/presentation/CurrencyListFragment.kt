package com.example.currency_list.list.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.currency_list.R
import kotlinx.android.synthetic.main.currency_list.view.*

class CurrencyListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vh = ViewHolder(inflater.inflate(R.layout.currency_list, container))

        val layoutManager = LinearLayoutManager(context)
        val viewModel = currencyListViewModel()
        val adapter = CurrencyListAdapter()

        vh.currencyList.layoutManager = layoutManager
        vh.currencyList.adapter = adapter
        vh.currencyList.onScrolled { _, _ ->
            viewModel.setVisibleItems(layoutManager.getVisibleCurrencies(adapter))
        }

        vh.swipeToRefresh.setOnRefreshListener { viewModel.onRefresh() }

        viewModel.isRefreshing.observe(this) { vh.swipeToRefresh.isRefreshing = it }
        viewModel.currenciesList.observe(this) { adapter.submitList(it) }

        return vh.root
    }

    private fun LinearLayoutManager.getVisibleCurrencies(adapter: CurrencyListAdapter) =
        adapter.sliceData(findFirstVisibleItemPosition(), findLastVisibleItemPosition())

    private class ViewHolder(val root: View) {
        val swipeToRefresh: SwipeRefreshLayout = root.currency_list_swipe_to_refresh
        val currencyList: RecyclerView = root.currency_list
    }
}
