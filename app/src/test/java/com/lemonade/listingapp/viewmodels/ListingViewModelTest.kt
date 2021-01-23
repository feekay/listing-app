package com.lemonade.listingapp.viewmodels

import androidx.lifecycle.liveData
import com.lemonade.listingapp.BaseTest
import com.lemonade.listingapp.models.ListingItem
import com.lemonade.listingapp.models.ListingResponse
import com.lemonade.listingapp.repository.ListingRepository
import com.lemonade.listingapp.repository.LoadFailure
import com.lemonade.listingapp.repository.LoadResult
import com.lemonade.listingapp.repository.LoadSuccess
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`

class ListingViewModelTest : BaseTest() {

    @Mock
    lateinit var repository: ListingRepository

    lateinit var viewModel: ListingViewModel

    @Before
    fun setup() {
        viewModel = ListingViewModel(repository)
    }

    @Test
    fun `When data is requested, loading is set true, errors are cleared`() = runBlockingTest {
        viewModel.isLoading.observeForever {
            assert(it)
        }
        viewModel.isError.observeForever {
            assert(!it)
        }
        viewModel.readListingData()
    }

    @Test
    fun `When data is loaded successfully, loading is stopped, non empty list is returned`() = runBlockingTest {
        `when`(repository.loadData()).thenReturn(liveData<LoadResult<ListingResponse>> {
            emit(LoadSuccess(getDummySuccess()))
        })
        viewModel.readListingData().observeForever {
            assert(it.isNotEmpty())
        }
        assert(viewModel.isLoading.value == false)
    }

    @Test
    fun `When load fails, error is set, loading is stopped and empty list is returned`() = runBlockingTest {
        `when`(repository.loadData()).thenReturn(liveData<LoadResult<ListingResponse>> {
            emit(LoadFailure(""))
        })
        viewModel.readListingData().observeForever {
            assert(it.isEmpty())
        }
        assert(viewModel.isError.value == true)
        assert(viewModel.isLoading.value == false)
    }

    private fun getDummySuccess() =
        ListingResponse(listOf(ListingItem("", "", "", emptyList(), emptyList())))
}