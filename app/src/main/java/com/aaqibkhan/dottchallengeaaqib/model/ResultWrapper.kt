package com.aaqibkhan.dottchallengeaaqib.model

data class ResultWrapper<out T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
)

enum class Status {
    LOADING, ERROR, SUCCESS
}