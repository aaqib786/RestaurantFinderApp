package com.aaqibkhan.dottchallengeaaqib.model

import java.io.Serializable

data class RestaurantSimple(
    val id: String,
    val name: String,
    val location: FoursquareLocation? = null,
    val categories: List<FoursquareCategory>? = null
): Serializable