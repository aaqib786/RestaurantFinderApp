package com.aaqibkhan.dottchallengeaaqib.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aaqibkhan.dottchallengeaaqib.cache.CacheManager
import com.aaqibkhan.dottchallengeaaqib.data.TestData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MapResultViewModelTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MapResultViewModel

    @Before
    fun setUp() {
        val myMock = mock<CacheManager> {
            on { getRestaurantsWithinBounds(TestData.southWestLatLng, TestData.northEastLatLng) } doReturn TestData.restaurantSimpleList
        }
        viewModel = MapResultViewModel(TestCoroutineDispatcher(), myMock)
    }

    @Test
    fun loadRestaurants() {
        runBlockingTest {
            viewModel.loadRestaurants(TestData.southWestLatLng, TestData.northEastLatLng)
            Assert.assertEquals(TestData.restaurantSimpleList, viewModel.restaurants.value?.data)
        }
    }
}