package com.lemonade.listingapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lemonade.listingapp.models.ListingItem
import com.lemonade.listingapp.repository.ListingRepository
import com.lemonade.listingapp.repository.LoadFailure
import com.lemonade.listingapp.repository.LoadSuccess

class ListingViewModel @ViewModelInject constructor(
    private val repository: ListingRepository
) : ViewModel() {

    private var _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _isError: MutableLiveData<Boolean> = MutableLiveData()
    val isError: LiveData<Boolean> get() = _isError

    suspend fun readListingData(): LiveData<List<ListingItem>> {
        _isLoading.value = true
        _isError.value = false
        return Transformations.map(repository.loadData()) {
            _isLoading.value = false
            when (it) {
                is LoadSuccess -> it.data.results
                is LoadFailure -> {
                    _isError.value = true
                    emptyList()
                }
            }
        }
    }
}