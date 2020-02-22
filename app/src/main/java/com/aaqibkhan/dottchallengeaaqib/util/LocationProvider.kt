package com.aaqibkhan.dottchallengeaaqib.util

import com.aaqibkhan.dottchallengeaaqib.App
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

object LocationProvider {

    fun getCurrentLocation(call: (location: LatLng) -> Unit) {
        LocationServices.getFusedLocationProviderClient(App.appContext)
            .lastLocation.addOnSuccessListener { call.invoke(LatLng(it.latitude, it.longitude)) }
    }

}