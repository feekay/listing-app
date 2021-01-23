package com.lemonade.listingapp.repository

import com.lemonade.listingapp.BaseTest
import com.lemonade.listingapp.api.ListingService
import com.lemonade.listingapp.models.ListingResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class DefaultListingRepositoryTest : BaseTest() {

    @InjectMocks
    private lateinit var repository: DefaultListingRepository

    @Mock
    private lateinit var service: ListingService

    @ExperimentalCoroutinesApi
    @Test
    fun `When api returns data LoadSuccess is returned`() = runBlockingTest {
        `when`(service.getListing()).thenReturn(ListingResponse(emptyList()))
        repository.loadData().observeForever {
            assert(it is LoadSuccess)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When api fails LoadError is returned`() = runBlockingTest {
        `when`(service.getListing()).thenThrow(RuntimeException("API FAILED"))
        repository.loadData().observeForever {
            assert(it is LoadFailure)
        }
    }
}