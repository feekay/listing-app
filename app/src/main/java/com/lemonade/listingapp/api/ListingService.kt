package com.lemonade.listingapp.api

import com.lemonade.listingapp.models.ListingResponse
import retrofit2.http.GET

interface ListingService {
    @GET("default/dynamodb-writer")
    suspend fun getListing(): ListingResponse
}
