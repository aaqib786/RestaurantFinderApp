package com.aaqibkhan.dottchallengeaaqib.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aaqibkhan.dottchallengeaaqib.cache.CacheManager
import com.aaqibkhan.dottchallengeaaqib.data.TestData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RestaurantViewModelTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RestaurantViewModel

    @Before
    fun setUp() {
        val myMock = mock<CacheManager> {
            on { getRestaurant(TestData.restaurantDetail.id) } doReturn TestData.restaurantDetail
        }
        viewModel = RestaurantViewModel(TestCoroutineDispatcher(), myMock)
    }

    @Test
    fun loadRestaurant() {
        runBlockingTest {
            viewModel.loadRestaurant(TestData.restaurantDetail.id)
        }
        assertEquals(TestData.restaurantDetail, viewModel.restaurant.value?.data)
    }

}