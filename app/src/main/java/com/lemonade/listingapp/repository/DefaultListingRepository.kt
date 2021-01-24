package com.lemonade.listingapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.lemonade.listingapp.api.ListingService
import com.lemonade.listingapp.models.ListingResponse
import java.lang.Exception
import javax.inject.Inject


class DefaultListingRepository @Inject constructor(private val service: ListingService) :
    ListingRepository {

    override suspend fun loadData(): LiveData<LoadResult<ListingResponse>> = liveData {
        try {
            emit(LoadSuccess(service.getListing()))
        } catch (e: Exception) {
            emit(LoadFailure<ListingResponse>(e.message ?: ""))
        }
    }
}

interface ListingRepository {
    suspend fun loadData(): LiveData<LoadResult<ListingResponse>>
}

sealed class LoadResult<T>
data class LoadSuccess<T>(val data: T) : LoadResult<T>()
data class LoadFailure<T>(val error: String) : LoadResult<T>()