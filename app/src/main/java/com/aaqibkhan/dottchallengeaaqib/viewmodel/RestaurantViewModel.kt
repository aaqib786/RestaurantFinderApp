package com.aaqibkhan.dottchallengeaaqib.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaqibkhan.dottchallengeaaqib.cache.CacheManager
import com.aaqibkhan.dottchallengeaaqib.model.RestaurantDetail
import com.aaqibkhan.dottchallengeaaqib.model.ResultWrapper
import com.aaqibkhan.dottchallengeaaqib.model.Status
import com.aaqibkhan.dottchallengeaaqib.network.FoursquareClient
import com.aaqibkhan.dottchallengeaaqib.network.data.ResponseGetRestaurant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This view model holds [RestaurantDetail] inside a [ResultWrapper] that contains
 * full information of a restaurant.
 * This is used when user clicks on a restaurant marker on map and
 * we show a bottom sheet with the details.
 */
class RestaurantViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val cacheManager: CacheManager = CacheManager
) :
    ViewModel() {

    private val _restaurant = MutableLiveData<ResultWrapper<RestaurantDetail>>()

    val restaurant: LiveData<ResultWrapper<RestaurantDetail>> = _restaurant

    /**
     * 1. Gets [RestaurantDetail] from the cache [CacheManager]
     * 2. If not found in cache, then makes network call using [FoursquareClient]
     */
    fun loadRestaurant(id: String?) {
        if (id.isNullOrBlank()) {
            return
        }
        viewModelScope.launch(dispatcher) {
            _restaurant.postValue(ResultWrapper(status = Status.LOADING))
            val cacheResult = cacheManager.getRestaurant(id)
            if (cacheResult != null) {
                _restaurant.postValue(ResultWrapper(Status.SUCCESS, cacheResult))
            } else {
                val response = FoursquareClient.getRestaurant(id)
                handleResponse(response)
            }
        }
    }

    private fun handleResponse(response: ResultWrapper<ResponseGetRestaurant>) {
        if (response.status == Status.SUCCESS) {
            _restaurant.postValue(ResultWrapper(Status.SUCCESS, response.data?.venue))
        } else if (response.status == Status.ERROR) {
            _restaurant.postValue(ResultWrapper(status = Status.ERROR, message = response.message))
        }
    }

}