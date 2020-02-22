package com.aaqibkhan.dottchallengeaaqib.data

import com.aaqibkhan.dottchallengeaaqib.model.*
import com.google.android.gms.maps.model.LatLng

object TestData {

    const val RESTAURANT_ID = "4a27db6ef964a520c6931fe3"
    const val RESTAURANT_NAME = "Steakhouse Pizzeria Osdorp"

    val southWestLatLng = LatLng(52.168878, 4.566193)
    val northEastLatLng = LatLng(52.415823, 5.067444)

    val restaurantLocation = FoursquareLocation(
        52.356567,
        4.792526,
        0,
        null,
        "1069 DX",
        listOf("Tussen Meer 282-a", "1069 DX Amsterdam", "Nederland")
    )

    val restaurantContact = FoursquareContact(
        "+31612345678",
        "+31 612 345 678"
    )

    val restaurantDetail = RestaurantDetail(
        id = RESTAURANT_ID,
        name = RESTAURANT_NAME,
        location = restaurantLocation,
        contact = restaurantContact
    )

    val restaurantSimple = RestaurantSimple(
        id = RESTAURANT_ID,
        name = RESTAURANT_NAME,
        location = restaurantLocation
    )

    val restaurantSimpleList = listOf(restaurantSimple)

    val foursquareCategory1 = FoursquareCategory(
        "4bf58dd8d48988d1cc941735",
        "Steakhouse",
        "Steakhouse"
    )

    val foursquareCategory2 = FoursquareCategory(
        "4bf58dd8d48988d1d2941735",
        "Sushi Restaurant",
        "Sushi"
    )

    val foursquareCategoryList = listOf(foursquareCategory1, foursquareCategory2)

}