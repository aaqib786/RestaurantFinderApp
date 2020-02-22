package com.aaqibkhan.dottchallengeaaqib.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MapClusterItem(
    val restaurantId: String,
    val location: LatLng,
    val heading: String,
    val description: String? = null
) : ClusterItem {
    override fun getPosition(): LatLng = location

    override fun getTitle(): String = heading

    override fun getSnippet(): String? = description
}