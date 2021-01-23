package com.lemonade.listingapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lemonade.listingapp.databinding.ListingFragmentBinding
import com.lemonade.listingapp.models.ListingItem
import com.lemonade.listingapp.viewmodels.ListingViewModel
import com.lemonade.listingapp.views.adapters.ListingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListingFragment : Fragment() {
    private val viewModel: ListingViewModel by viewModels()
    private val adapter: ListingAdapter = ListingAdapter(::moveToNext)
    private var currentJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ListingFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupListingData(binding)
        return binding.root
    }

    private fun setupListingData(binding: ListingFragmentBinding) {
        binding.itemsList.layoutManager = LinearLayoutManager(context)
        binding.itemsList.adapter = adapter
        currentJob?.cancel()
        currentJob = lifecycleScope.launch {
            viewModel.readListingData().observe(viewLifecycleOwner, Observer {
                adapter.submitList(it?.toMutableList())
            })
        }
    }

    private fun moveToNext(item: ListingItem) {
        findNavController().navigate(ListingFragmentDirections.nextAction(item))
    }

}