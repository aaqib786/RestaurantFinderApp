package com.aaqibkhan.dottchallengeaaqib.network.data

data class FoursquareResponse<T>(
    val meta: FoursquareResponseMeta,
    val response: T
)

data class FoursquareResponseMeta(
    val code: Int,
    val requestId: String,
    val errorType: String,
    val errorDetail: String
)