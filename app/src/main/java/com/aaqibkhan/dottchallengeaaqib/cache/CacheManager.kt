package com.aaqibkhan.dottchallengeaaqib.cache

import androidx.collection.LruCache
import com.aaqibkhan.dottchallengeaaqib.model.RestaurantDetail
import com.aaqibkhan.dottchallengeaaqib.model.RestaurantSimple
import com.aaqibkhan.dottchallengeaaqib.util.getSizeInBytes
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

object CacheManager {

    // Available memory size in KB
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    // Get 1/8th of the size of available memory
    private val cacheSize = maxMemory / 8
    // Use 80% of cache size for storing map results which are list of restaurant details
    private val restaurantSimpleCacheSize = (cacheSize * 0.8).toInt()
    // Use rest 20% of cache size for storing single restaurant full details
    private val restaurantDetailCacheSize = (cacheSize * 0.2).toInt()

    private val restaurantSimpleCache =
        object : LruCache<String, RestaurantSimple>(restaurantSimpleCacheSize) {
            // Override size of objects to compute their size in KB
            override fun sizeOf(key: String, value: RestaurantSimple): Int {
                return value.getSizeInBytes() / 1024
            }
        }

    private val restaurantDetailCache =
        object : LruCache<String, RestaurantDetail>(restaurantDetailCacheSize) {
            // Override size of objects to compute their size in KB
            override fun sizeOf(key: String, value: RestaurantDetail): Int {
                return value.getSizeInBytes() / 1024
            }
        }

    fun addToCache(restaurants: List<RestaurantSimple>?) {
        if (restaurants.isNullOrEmpty()) {
            return
        }
        for (restaurant in restaurants) {
            restaurantSimpleCache.put(restaurant.id, restaurant)
        }
    }

    fun addToCache(restaurant: RestaurantDetail?) {
        if (restaurant == null) {
            return
        }
        restaurantDetailCache.put(restaurant.id, restaurant)
    }

    /**
     * Gets list of [RestaurantSimple] that are located within the bounds of
     * [southWestLatLng] and [northEastLatLng]
     */
    fun getRestaurantsWithinBounds(
        southWestLatLng: LatLng,
        northEastLatLng: LatLng
    ): List<RestaurantSimple> {
        val bounds = LatLngBounds.Builder()
            .include(southWestLatLng)
            .include(northEastLatLng)
            .build()
        return restaurantSimpleCache.snapshot().values.filter {
            if (it.location == null) {
                false
            } else {
                val locationLatLng = LatLng(it.location.lat, it.location.lng)
                bounds.contains(locationLatLng)
            }
        }
    }

    fun getRestaurant(id: String): RestaurantDetail? = restaurantDetailCache[id]

}