package com.aaqibkhan.dottchallengeaaqib.network

import com.aaqibkhan.dottchallengeaaqib.App
import com.aaqibkhan.dottchallengeaaqib.R
import com.aaqibkhan.dottchallengeaaqib.cache.CacheManager
import com.aaqibkhan.dottchallengeaaqib.model.ResultWrapper
import com.aaqibkhan.dottchallengeaaqib.model.Status
import com.aaqibkhan.dottchallengeaaqib.network.data.FoursquareResponse
import com.aaqibkhan.dottchallengeaaqib.network.data.ResponseGetRestaurant
import com.aaqibkhan.dottchallengeaaqib.network.data.ResponseGetRestaurants
import com.aaqibkhan.dottchallengeaaqib.util.toCommaSeparatedString
import com.google.android.gms.maps.model.LatLng
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FoursquareClient {

    private const val BASE_URL = "https://api.foursquare.com/v2/"

    private val defaultParamsInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("client_id", FoursquareConstants.FOURSQUARE_CLIENT_ID)
            .addQueryParameter("client_secret", FoursquareConstants.FOURSQUARE_CLIENT_SECRET)
            .addQueryParameter("v", FoursquareConstants.API_VERSION_DATE)
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(defaultParamsInterceptor)
        .build()

    private val foursquareApi = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FoursquareApi::class.java)

    suspend fun getRestaurants(
        southWestLatLng: LatLng,
        northEastLatLng: LatLng
    ): ResultWrapper<ResponseGetRestaurants> =
        safeApiCall {
            foursquareApi.getRestaurantsByBoundingBox(
                FoursquareConstants.FOOD_CATEGORY_ID,
                southWestLatLng.toCommaSeparatedString(),
                northEastLatLng.toCommaSeparatedString(),
                FoursquareConstants.RESULTS_LIMIT
            )
        }.also { CacheManager.addToCache(it.data?.venues) }

    suspend fun getRestaurant(restaurantId: String): ResultWrapper<ResponseGetRestaurant> =
        safeApiCall { foursquareApi.getRestaurant(restaurantId) }
            .also { CacheManager.addToCache(it.data?.venue) }

    private suspend fun <T> safeApiCall(call: suspend () -> FoursquareResponse<T>): ResultWrapper<T> {
        return try {
            val result = call.invoke()
            if (result.meta.code == 200) {
                ResultWrapper(Status.SUCCESS, result.response)
            } else {
                ResultWrapper(
                    status = Status.ERROR,
                    message = result.meta.errorDetail
                )
            }
        } catch (ex: java.lang.Exception) {
            ResultWrapper(
                status = Status.ERROR,
                message = App.appContext.getString(R.string.generic_error_message)
            )
        }
    }

}