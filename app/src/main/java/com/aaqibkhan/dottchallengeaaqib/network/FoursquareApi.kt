package com.aaqibkhan.dottchallengeaaqib.network

import com.aaqibkhan.dottchallengeaaqib.network.data.FoursquareResponse
import com.aaqibkhan.dottchallengeaaqib.network.data.ResponseGetRestaurant
import com.aaqibkhan.dottchallengeaaqib.network.data.ResponseGetRestaurants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoursquareApi {

    @GET("venues/search?intent=browse")
    suspend fun getRestaurantsByBoundingBox(
        @Query("categoryId") categoryIds: String,
        @Query("sw") SouthWestCoordinates: String,
        @Query("ne") NorthEastCoordinates: String,
        @Query("limit") limit: Int
    ): FoursquareResponse<ResponseGetRestaurants>

    @GET("venues/{restaurant_id}")
    suspend fun getRestaurant(
        @Path("restaurant_id") id: String
    ): FoursquareResponse<ResponseGetRestaurant>

}