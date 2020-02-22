package com.aaqibkhan.dottchallengeaaqib.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RestaurantDetail(
    val id: String,
    val name: String,
    val location: FoursquareLocation? = null,
    val contact: FoursquareContact? = null,
    val categories: List<FoursquareCategory>? = null,
    val url: String? = null,
    val verified: Boolean = false,
    val price: FoursquarePrice? = null,
    val likes: FoursquareLikes? = null,
    val rating: Double = 0.0,
    val ratingColor: String? = null,
    val menu: FoursquareMenu? = null,
    val description: String? = null,
    val hours: FoursquareHours? = null,
    val bestPhoto: FoursquareImage? = null,
    val attributes: FoursquareAttribute? = null
): Serializable

data class FoursquareLocation(
    val lat: Double,
    val lng: Double,
    val distance: Long,
    val address: String?,
    val postalCode: String?,
    val formattedAddress: List<String>?
): Serializable {
    fun getFullAddress() = formattedAddress?.joinToString(separator = "\n")
}

data class FoursquareContact(
    val phone: String?,
    val formattedPhone: String?
): Serializable

data class FoursquareCategory(
    val id: String,
    val name: String,
    val shortName: String,
    val primary: Boolean = false,
    val icon: FoursquareImage? = null
): Serializable

data class FoursquarePrice(
    val tier: Int,
    val message: String,
    val currency: String
): Serializable {
    fun getPriceToDisplay() = currency.repeat(tier)
}

data class FoursquareLikes(
    val count: Int,
    val summary: String
): Serializable

data class FoursquareMenu(
    val type: String,
    val label: String,
    val anchor: String,
    val url: String,
    val mobileUrl: String?,
    val externalUrl: String?
): Serializable

data class FoursquareHours(
    val status: String,

    @SerializedName("isOpen")
    val open: Boolean
): Serializable

data class FoursquareImage(
    val prefix: String,
    val suffix: String,
    val width: Int,
    val height: Int
): Serializable {
    fun getImageUrl() = if (width > 0) (prefix + "original" + suffix) else (prefix + "512" + suffix)
}

data class FoursquareAttribute(
    val groups: List<FoursquareGroup>?
): Serializable

data class FoursquareGroup(
    val type: String,
    val name: String,
    val summary: String,
    val items: List<FoursquareItem>
): Serializable

data class FoursquareItem(
    val displayName: String,
    val displayValue: String
): Serializable