package com.aaqibkhan.dottchallengeaaqib.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaqibkhan.dottchallengeaaqib.cache.CacheManager
import com.aaqibkhan.dottchallengeaaqib.model.RestaurantSimple
import com.aaqibkhan.dottchallengeaaqib.model.ResultWrapper
import com.aaqibkhan.dottchallengeaaqib.model.Status
import com.aaqibkhan.dottchallengeaaqib.network.FoursquareClient
import com.aaqibkhan.dottchallengeaaqib.network.data.ResponseGetRestaurants
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A view model for holding [LiveData] of list of [RestaurantSimple] contained under [ResultWrapper]
 * This is used in plotting markers on the map based on map's viewport.
 */
class MapResultViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val cacheManager: CacheManager = CacheManager
) :
    ViewModel() {

    private val _restaurants = MutableLiveData<ResultWrapper<List<RestaurantSimple>>>()

    val restaurants: LiveData<ResultWrapper<List<RestaurantSimple>>> = _restaurants

    /**
     * 1. Gets list of [RestaurantSimple] from cache [CacheManager] limited to bounds
     *   [southWestLatLng] and [northEastLatLng] which in our use case is the viewport of the map.
     * 2. Makes an api call using [FoursquareClient] that gets list of restaurants within the bounds
     *  [southWestLatLng] and [northEastLatLng]
     */
    fun loadRestaurants(southWestLatLng: LatLng, northEastLatLng: LatLng) =
        viewModelScope.launch(dispatcher) {
            _restaurants.postValue(ResultWrapper(Status.LOADING))

            val cacheResult =
                cacheManager.getRestaurantsWithinBounds(southWestLatLng, northEastLatLng)
            _restaurants.postValue(ResultWrapper(Status.SUCCESS, cacheResult))

            val response = FoursquareClient.getRestaurants(southWestLatLng, northEastLatLng)
            handleResponse(response)
        }

    private fun handleResponse(response: ResultWrapper<ResponseGetRestaurants>) {
        if (response.status == Status.SUCCESS) {
            _restaurants.postValue(ResultWrapper(Status.SUCCESS, response.data?.venues))
        } else if (response.status == Status.ERROR) {
            _restaurants.postValue(ResultWrapper(status = Status.ERROR, message = response.message))
        }
    }

}