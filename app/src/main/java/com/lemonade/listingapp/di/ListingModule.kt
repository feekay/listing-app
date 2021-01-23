package com.lemonade.listingapp.di

import com.lemonade.listingapp.repository.DefaultListingRepository
import com.lemonade.listingapp.repository.ListingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class ListingModule {
    @Binds
    abstract fun bindListingRepo(impl: DefaultListingRepository): ListingRepository

}