package com.lemonade.listingapp.di

import com.lemonade.listingapp.BuildConfig
import com.lemonade.listingapp.BuildConfig.BASE_URL
import com.lemonade.listingapp.IdlingResource
import com.lemonade.listingapp.api.ListingService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideListingService(retrofit: Retrofit): ListingService {
        return retrofit
            .create(ListingService::class.java)
    }

    @Provides
    fun provideClient(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)  // Flavor specific urls
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()

    @Provides
    fun provideHttpInterceptor(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

        if (BuildConfig.INSTRUMENTATION) {
            //Add idling resource on Instrumentation builds
            builder.addInterceptor {
                IdlingResource.increment()
                val res = it.proceed(it.request())
                IdlingResource.decrement()
                res
            }
        }
        return builder.build()
    }


    @Provides
    fun providesMoshiFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}
