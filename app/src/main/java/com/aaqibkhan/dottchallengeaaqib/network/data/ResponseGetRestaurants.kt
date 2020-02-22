package com.aaqibkhan.dottchallengeaaqib.network.data

import com.aaqibkhan.dottchallengeaaqib.model.RestaurantSimple

data class ResponseGetRestaurants(
    val venues: List<RestaurantSimple>?
)