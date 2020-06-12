package com.example.currency_list.list.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currency_list.R
import com.example.currency_list.databinding.CurrencyListBinding

class CurrencyListFragment : Fragment(R.layout.currency_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = CurrencyListBinding.bind(view)
        val viewModel = currencyListViewModel()

        val adapter = CurrencyListAdapter()
        val layoutManager = LinearLayoutManager(context)

        binding.currencyList.layoutManager = layoutManager
        binding.currencyList.adapter = adapter
        binding.currencyList.onScrolled { _, _ ->
            viewModel.setVisibleItems(layoutManager.getVisibleCurrencies(adapter))
        }

        binding.swipeToRefresh.setOnRefreshListener { viewModel.onRefresh() }

        viewModel.isRefreshing.observe(this) { binding.swipeToRefresh.isRefreshing = it }
        viewModel.currenciesList.observe(this) { adapter.submitList(it) }
    }

    private fun LinearLayoutManager.getVisibleCurrencies(adapter: CurrencyListAdapter) =
        adapter.sliceData(findFirstVisibleItemPosition(), findLastVisibleItemPosition())
}
